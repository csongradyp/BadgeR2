package net.csongradyp.badger.provider;

import net.csongradyp.badger.domain.achievement.IAchievement;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;

public interface IUnlockedProvider<TYPE extends IAchievement> {

    AchievementUnlockedEvent getUnlockable(String userId, TYPE achievement, Long score);
}
