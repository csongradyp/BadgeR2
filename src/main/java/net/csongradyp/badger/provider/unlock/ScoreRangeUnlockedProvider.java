package net.csongradyp.badger.provider.unlock;

import java.util.List;
import net.csongradyp.badger.domain.achievement.ScoreRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.repository.Repository;

public class ScoreRangeUnlockedProvider extends UnlockedProvider<ScoreRangeAchievementBean> {

    public ScoreRangeUnlockedProvider(Repository repository) {
        super(repository);
    }

    @Override
    public AchievementUnlockedEvent getUnlockable(final String userId, final ScoreRangeAchievementBean timeAchievement, final Long score) {
        final List<ScoreTriggerPair> timeTriggers = timeAchievement.getTrigger();
        for (ScoreTriggerPair trigger : timeTriggers) {
            if (trigger.fire(score) && !isUnlocked(userId, timeAchievement.getId())) {
                return UnlockedEventFactory.createEvent(userId, timeAchievement, String.valueOf(score));
            }
        }
        return null;
    }
}
