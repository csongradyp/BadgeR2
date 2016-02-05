package net.csongradyp.badger.provider;

import java.util.Optional;
import net.csongradyp.badger.domain.achievement.IAchievement;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;

public interface IUnlockedProvider<TYPE extends IAchievement> {

    Optional<IAchievementUnlockedEvent> getUnlockable(String userId, TYPE achievement, Long score);
}
