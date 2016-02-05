package net.csongradyp.badger.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.AchievementDefinition;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.IAchievement;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.repository.Repository;

@Named
public class AchievementUnlockProviderFacade {

    @Inject
    private Repository repository;
    @Resource(name = "unlockedProviders")
    private Map<AchievementType, IUnlockedProvider<IAchievement>> unlockedProviders;
    private AchievementDefinition achievementDefinition;

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
        final IUnlockedProvider<IAchievement> unlockedProvider = unlockedProviders.get(achievementBean.getType());
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
            if(eventScore > bestScore) {
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

    void setUnlockedProviders(final Map<AchievementType, IUnlockedProvider<IAchievement>> unlockedProviders) {
        this.unlockedProviders = unlockedProviders;
    }
}
