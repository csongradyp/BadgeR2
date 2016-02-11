package net.csongradyp.badger.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.IAchievement;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.provider.unlock.CompositeUnlockedProvider;
import net.csongradyp.badger.provider.unlock.DateUnlockedProvider;
import net.csongradyp.badger.provider.unlock.ScoreRangeUnlockedProvider;
import net.csongradyp.badger.provider.unlock.ScoreUnlockedProvider;
import net.csongradyp.badger.provider.unlock.TimeRangeUnlockedProvider;
import net.csongradyp.badger.provider.unlock.TimeUnlockedProvider;
import net.csongradyp.badger.repository.Repository;

public class AchievementUnlockProviderFacade {

    private final Repository repository;
    private Map<AchievementType, IUnlockedProvider<? extends IAchievement>> unlockedProviders;
    private AchievementDefinition achievementDefinition;

    public AchievementUnlockProviderFacade(final Repository repository) {
        this.repository = repository;
        unlockedProviders = new HashMap<>();
        unlockedProviders.put(AchievementType.COMPOSITE, new CompositeUnlockedProvider(this.repository));
        unlockedProviders.put(AchievementType.DATE, new DateUnlockedProvider(this.repository));
        unlockedProviders.put(AchievementType.TIME, new TimeUnlockedProvider(this.repository));
        unlockedProviders.put(AchievementType.TIME_RANGE, new TimeRangeUnlockedProvider(this.repository));
        unlockedProviders.put(AchievementType.SCORE, new ScoreUnlockedProvider(this.repository));
        unlockedProviders.put(AchievementType.SCORE_RANGE, new ScoreRangeUnlockedProvider(this.repository));
    }

    public Collection<IAchievementUnlockedEvent> findAll(final String userId) {
        final Collection<IAchievementUnlockedEvent> unlockables = new ArrayList<>();
        final Collection<IAchievement> all = achievementDefinition.getAll();
        for (IAchievement achievementBean : all) {
            final AchievementUnlockedEvent achievement = getUnlockable(userId, achievementBean);
            if (achievement != null) {
                unlockables.add(achievement);
            }
        }
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
            final AchievementUnlockedEvent achievement = getUnlockable(userId, achievementBean, score);
            if (achievement != null) {
                unlockables.add(achievement);
            }
        }
        return unlockables;
    }

    private AchievementUnlockedEvent getUnlockable(final String userId, final IAchievement achievementBean, final Long currentValue) {
        final IUnlockedProvider<IAchievement> unlockedProvider = (IUnlockedProvider<IAchievement>) unlockedProviders.get(achievementBean.getType());
        return unlockedProvider.getUnlockable(userId, achievementBean, currentValue);
    }

    public AchievementUnlockedEvent getUnlockable(final String userId, final IAchievement achievementBean) {
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

}
