package net.csongradyp.badger.provider.unlock;

import java.util.Date;
import java.util.List;
import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.provider.date.DateProvider;
import net.csongradyp.badger.repository.Repository;

public class TimeRangeUnlockedProvider extends UnlockedProvider<TimeRangeAchievementBean> {

    public TimeRangeUnlockedProvider(Repository repository) {
        super(repository);
    }

    @Override
    public AchievementUnlockedEvent getUnlockable(final String userId, final TimeRangeAchievementBean timeAchievement, final Long score) {
        final List<TimeTriggerPair> timeTriggers = timeAchievement.getTrigger();
        for (TimeTriggerPair timeTrigger : timeTriggers) {
            final Date now = DateProvider.currentTime();
            if(timeTrigger.fire(now) && !isUnlocked(userId, timeAchievement.getId())) {
                return UnlockedEventFactory.createEvent(userId, timeAchievement, DateProvider.currentTimeString());
            }
        }
        return null;
    }
}
