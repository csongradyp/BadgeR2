package net.csongradyp.badger.provider.unlock;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.achievement.ScoreRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;

@Named
public class ScoreRangeUnlockedProvider extends UnlockedProvider<ScoreRangeAchievementBean> {

    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    @Override
    public Optional<IAchievementUnlockedEvent> getUnlockable(final String userId, final ScoreRangeAchievementBean timeAchievement, final Long score) {
        final List<ScoreTriggerPair> timeTriggers = timeAchievement.getTrigger();
        for (ScoreTriggerPair trigger : timeTriggers) {
            if(trigger.fire(score) && !isUnlocked(userId, timeAchievement.getId())) {
                final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(userId, timeAchievement, String.valueOf(score));
                return Optional.of(achievementUnlockedEvent);
            }
        }
        return Optional.empty();
    }

    void setUnlockedEventFactory(final UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }
}
