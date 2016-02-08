package net.csongradyp.badger.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import javax.inject.Named;
import net.csongradyp.badger.event.message.ScoreUpdatedEvent;
import net.csongradyp.badger.event.wrapper.AchievementUnlockedHandlerWrapper;
import net.csongradyp.badger.event.wrapper.ScoreUpdateHandlerWrapper;

@Named
public class EventBus {

    private final Collection<AchievementUnlockedHandlerWrapper> unlockedSubscribers = new ArrayList<>();
    private final Collection<ScoreUpdateHandlerWrapper> scoreUpdateSubscribers = new ArrayList<>();

    public void subscribeOnUnlock(final AchievementUnlockedHandlerWrapper handler) {
        unlockedSubscribers.add(handler);
    }

    public void unSubscribeOnUnlock(final IAchievementUnlockedHandler handler) {
        final Optional<AchievementUnlockedHandlerWrapper> registeredHandler = unlockedSubscribers.stream()
                .filter(wrapper -> wrapper.getWrapped().equals(handler))
                .findAny();
        if (registeredHandler.isPresent()) {
            final AchievementUnlockedHandlerWrapper listener = registeredHandler.get();
            unSubscribe(listener);
        }
    }

    private void unSubscribe(final AchievementUnlockedHandlerWrapper listener) {
        final boolean unsubscribe = unlockedSubscribers.remove(listener);
        if (!unsubscribe) {
            throw new SubscriptionException("Unsubscribe failed for achievement unlocked handler" + listener.getWrapped());
        }
    }

    public void publishUnlocked(final IAchievementUnlockedEvent achievement) {
        unlockedSubscribers.stream().forEach(handler -> handler.onUnlocked(achievement));
    }

    public void subscribeOnScoreChanged(final ScoreUpdateHandlerWrapper handler) {
        scoreUpdateSubscribers.add(handler);
    }

    public void unSubscribeAllOnScoreChanged() {
        scoreUpdateSubscribers.clear();
    }

    public void unSubscribeOnScoreChanged(final IScoreUpdateHandler handler) {
        final Optional<ScoreUpdateHandlerWrapper> registeredHandler = scoreUpdateSubscribers.stream()
                .filter(wrapper -> wrapper.getWrapped().equals(handler))
                .findAny();
        if (registeredHandler.isPresent()) {
            final ScoreUpdateHandlerWrapper listener = registeredHandler.get();
            unSubscribe(listener);
        }
    }

    private void unSubscribe(final ScoreUpdateHandlerWrapper listener) {
        final boolean unsubscribe = scoreUpdateSubscribers.remove(listener);
        if (!unsubscribe) {
            throw new SubscriptionException("Unsubscribe failed for score changed handler" + listener.getWrapped());
        }
    }

    public void publishScoreChanged(final ScoreUpdatedEvent scoreUpdatedEvent) {
        scoreUpdateSubscribers.stream().forEach(handler -> handler.onUpdate(scoreUpdatedEvent));
    }

    public Collection<AchievementUnlockedHandlerWrapper> getUnlockedSubscribers() {
        return unlockedSubscribers;
    }

    public Collection<ScoreUpdateHandlerWrapper> getScoreUpdateSubscribers() {
        return scoreUpdateSubscribers;
    }
}
