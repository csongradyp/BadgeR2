package net.csongradyp.badger.event;

import net.csongradyp.badger.event.message.AchievementUnlockedEvent;

/**
 * Interface to handle unlocked achievement events.
 */
public interface IAchievementUnlockedHandler {

    /**
     * Callback method to receive notification about the unlocked achievements.
     *
     * @param achievementUnlockedEvent Unlocked achievement information as an {@link AchievementUnlockedEvent} instance.
     */
    void onUnlocked(AchievementUnlockedEvent achievementUnlockedEvent);
}
