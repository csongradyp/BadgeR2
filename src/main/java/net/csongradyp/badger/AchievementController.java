package net.csongradyp.badger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.achievement.IAchievement;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.event.message.ScoreUpdatedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.provider.AchievementUnlockProviderFacade;
import net.csongradyp.badger.repository.BadgerRepository;
import net.csongradyp.badger.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AchievementController {

    private static final Logger LOG = LoggerFactory.getLogger(AchievementController.class);

    private UnlockedEventFactory unlockedEventFactory;
    private AchievementUnlockProviderFacade achievementUnlockFinder;
    private EventBus eventBus;
    private Repository repository;

    private AchievementDefinition achievementDefinition;
    private String internationalizationBaseName;
    private ResourceBundle resourceBundle;

    public AchievementController() {
        repository = new Repository();
        eventBus = new EventBus();
        achievementDefinition = new AchievementDefinition();
        achievementUnlockFinder = new AchievementUnlockProviderFacade(repository);
        unlockedEventFactory = new UnlockedEventFactory();
    }

    public void setAchievementDefinition(final AchievementDefinition achievementDefinition) {
        this.achievementDefinition = achievementDefinition;
        achievementUnlockFinder.setAchievementDefinition(achievementDefinition);
    }

    public void setInternationalizationBaseName(final String internationalizationBaseName) {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(internationalizationBaseName, Locale.ENGLISH);
        setResourceBundle(resourceBundle);
        this.internationalizationBaseName = internationalizationBaseName;
    }

    public void setResourceBundle(final ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        unlockedEventFactory.setResourceBundle(resourceBundle);
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setLocale(final Locale locale) {
        setResourceBundle(ResourceBundle.getBundle(internationalizationBaseName, locale));
    }

    public Collection<IAchievement> getAll() {
        return achievementDefinition.getAll();
    }

    public Collection<IAchievement> getAllUnlocked(final String userId) {
        final Collection<IAchievement> unlocked = new ArrayList<>();
        final Collection<IAchievementBean> unlockedEntities = repository.achievement().getAll(userId);
        for (IAchievementBean entity : unlockedEntities) {
            final IAchievement achievement = achievementDefinition.get(entity.getId());
            if (achievement != null) {
                unlocked.add(achievement);
            }
        }
        return unlocked;
    }

    public Collection<IAchievement> getAllByOwner(final String userId) {
        final Collection<IAchievement> achievementsByOwner = new ArrayList<>();
        final Collection<IAchievementBean> achievementEntities = repository.achievement().getAll(userId);
        for (IAchievementBean achievementEntity : achievementEntities) {
            final IAchievement achievement = achievementDefinition.get(achievementEntity.getId());
            if (achievement != null) {
                achievementsByOwner.add(achievement);
            }
        }
        return achievementsByOwner;
    }

    public IAchievement get(final String id) {
        return achievementDefinition.get(id);
    }

    public Map<String, Set<IAchievement>> getAllByEvents() {
        return achievementDefinition.getAllByEvents();
    }

    public void checkAndUnlock(final String userId) {
        LOG.debug("Checking achievements to unlock");
        final Collection<IAchievementUnlockedEvent> unlockableAchievements = achievementUnlockFinder.findAll(userId);
        for (IAchievementUnlockedEvent event : unlockableAchievements) {
            unlock(event);
        }
    }

    public void triggerEventWithHighScore(final String userId, final String event, final Long score) {
        if (isNewHighScore(userId, event, score)) {
            LOG.debug("New highscore submitted!");
            triggerEvent(userId, event, score);
        }
    }

    private boolean isNewHighScore(final String userId, final String event, final Long score) {
        return repository.event().scoreOf(userId, event) <= score;
    }

    public void triggerEvent(final String userId, final String event, final Long score) {
        if (isDifferentValueAsStored(userId, event, score)) {
            LOG.debug("Achievement event named {} is triggered by owners {} with score: {}", event, score);
            publishUpdatedScore(userId, event, score);
            final Collection<IAchievementUnlockedEvent> unlockables = achievementUnlockFinder.findUnlockables(userId, event, score);
            for (IAchievementUnlockedEvent unlockable : unlockables) {
                unlock(unlockable);
            }
        }
    }

    private void publishUpdatedScore(final String userId, final String event, final Long score) {
        final Long currentValue = repository.event().setScore(userId, event, score);
        final ScoreUpdatedEvent updatedEvent = new ScoreUpdatedEvent(event, currentValue);
        eventBus.publishScoreChanged(updatedEvent);
    }

    private boolean isDifferentValueAsStored(final String userId, final String event, final Long score) {
        final Long currentValue = repository.event().scoreOf(userId, event);
        return !currentValue.equals(score);
    }

    public void triggerEvent(final String userId, final String event) {
        LOG.info("Achievement event triggered: {}", event);
        publishIncremented(userId, event);
        final Collection<IAchievementUnlockedEvent> unlockables = achievementUnlockFinder.findUnlockables(userId, event);
        for (IAchievementUnlockedEvent unlockable : unlockables) {
            unlock(unlockable);
        }
    }

    private Long publishIncremented(final String userId, final String event) {
        final Long currentValue = repository.event().increment(userId, event);
        eventBus.publishScoreChanged(new ScoreUpdatedEvent(event, currentValue));
        return currentValue;
    }

    public void unlock(final String userId, final String achievementId, final String triggerValue) {
        final IAchievement matchingAchievement = achievementDefinition.get(achievementId);
        if (matchingAchievement != null) {
            final AchievementUnlockedEvent achievementUnlockedEvent = UnlockedEventFactory.createEvent(userId, matchingAchievement, triggerValue);
            unlock(achievementUnlockedEvent);
        }
    }

    private void unlock(final IAchievementUnlockedEvent achievement) {
        if (!isLevelUnlocked(achievement.getOwner(), achievement.getId(), achievement.getLevel())) {
            repository.achievement().unlock(achievement.getOwner(), achievement.getId(), achievement.getLevel());
            eventBus.publishUnlocked(achievement);
        }
    }

    private Boolean isLevelUnlocked(final String userId, final String id, final Integer level) {
        return repository.achievement().isUnlocked(userId, id, level);
    }

    public Boolean isUnlocked(final String userId, final String achievementId) {
        return repository.achievement().isUnlocked(userId, achievementId);
    }

    public Boolean isUnlocked(final String userId, final String achievementId, final Integer level) {
        return repository.achievement().isUnlocked(userId, achievementId, level);
    }

    public Long getCurrentScore(final String userId, final String achievementId) {
        return repository.event().scoreOf(userId, achievementId);
    }

    public void reset(final String userId) {
        repository.event().resetCounters(userId);
        repository.achievement().clearAchievements(userId);
    }

    void setAchievementUnlockFinder(AchievementUnlockProviderFacade achievementUnlockFinder) {
        this.achievementUnlockFinder = achievementUnlockFinder;
    }

    void setUnlockedEventFactory(UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }

    void setEventBus(final EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    void setRepository(BadgerRepository repository) {
        this.repository.setAchievementRepository(repository);
        this.repository.setEventRepository(repository);
    }
}
