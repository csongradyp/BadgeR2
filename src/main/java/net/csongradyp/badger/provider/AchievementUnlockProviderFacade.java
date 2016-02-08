package net.csongradyp.badger.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.IAchievement;
import net.csongradyp.badger.domain.achievement.ScoreAchievementBean;
import net.csongradyp.badger.domain.achievement.ScoreRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.provider.unlock.CompositeUnlockedProvider;
import net.csongradyp.badger.repository.Repository;

@Named
public class AchievementUnlockProviderFacade {

    @Inject
    private Repository repository;
    @Inject
    private CompositeUnlockedProvider compositeUnlockedProvider;
    @Inject
    private IUnlockedProvider<DateAchievementBean> dateUnlockedProvider;
    @Inject
    private IUnlockedProvider<ScoreAchievementBean> scoreUnlockedProvider;
    @Inject
    private IUnlockedProvider<ScoreRangeAchievementBean> scoreRangeUnlockedProvider;
    @Inject
    private IUnlockedProvider<TimeAchievementBean> timeUnlockedProvider;
    @Inject
    private IUnlockedProvider<TimeRangeAchievementBean> timeRangeUnlockedProvider;
    private Map<AchievementType, IUnlockedProvider<? extends IAchievement>> unlockedProviders;
    private AchievementDefinition achievementDefinition;

    public AchievementUnlockProviderFacade() {
        unlockedProviders = new HashMap<>();
        unlockedProviders.put(AchievementType.COMPOSITE, compositeUnlockedProvider);
        unlockedProviders.put(AchievementType.DATE, dateUnlockedProvider);
        unlockedProviders.put(AchievementType.TIME, timeUnlockedProvider);
        unlockedProviders.put(AchievementType.TIME_RANGE, timeRangeUnlockedProvider);
        unlockedProviders.put(AchievementType.SCORE, scoreUnlockedProvider);
        unlockedProviders.put(AchievementType.SCORE_RANGE, scoreRangeUnlockedProvider);
    }

    public Collection<IAchievementUnlockedEvent> findAll(final String userId) {
        final Collection<IAchievementUnlockedEvent> unlockables = new ArrayList<>();
        achievementDefinition.getAll().stream().forEach(achievementBean -> {
            final Optional<IAchievementUnlockedEvent> achievement = getUnlockable(userId, achievementBean);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        });
        return unlockables;
    }

    public Collection<IAchievementUnlockedEvent> findUnlockables(final String userId, final String event) {
        final Long currentValue = repository.event().scoreOf(userId, event);
        return findUnlockables(userId, event, currentValue);
    }

    public Collection<IAchievementUnlockedEvent> findUnlockables(final String userId, final String event, final Long score) {
        final Collection<IAchievementUnlockedEvent> unlockables = new ArrayList<>();
        final Collection<IAchievement> achievementBeans = achievementDefinition.getAchievementsSubscribedFor(event);
        for (IAchievement achievementBean : achievementBeans) {
            final Optional<IAchievementUnlockedEvent> achievement = getUnlockable(userId, achievementBean, score);
            if (achievement.isPresent()) {
                unlockables.add(achievement.get());
            }
        }
        return unlockables;
    }

    private Optional<IAchievementUnlockedEvent> getUnlockable(final String userId,final IAchievement achievementBean, final Long currentValue) {
        final IUnlockedProvider<IAchievement> unlockedProvider = (IUnlockedProvider<IAchievement>) unlockedProviders.get(achievementBean.getType());
        return unlockedProvider.getUnlockable(userId, achievementBean, currentValue);
    }

    public Optional<IAchievementUnlockedEvent> getUnlockable(final String userId, final IAchievement achievementBean) {
        final Long bestScore = getBestScoreOf(userId, achievementBean.getSubscriptions());
        return getUnlockable(userId, achievementBean, bestScore);
    }

    private Long getBestScoreOf(final String userId, final List<String> events) {
        Long bestScore = Long.MIN_VALUE;
        for (String event : events) {
            final Long eventScore = repository.event().scoreOf(userId, event);
            if (eventScore > bestScore) {
                bestScore = eventScore;
            }
        }
        return bestScore;
    }

    public void setAchievementDefinition(final AchievementDefinition achievementDefinition) {
        this.achievementDefinition = achievementDefinition;
    }

    public void setRepository(final Repository repository) {
        this.repository = repository;
    }

}
