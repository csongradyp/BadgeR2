package net.csongradyp.badger.provider.unlock;

import java.util.Date;
import java.util.List;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.TimeTrigger;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.provider.date.DateProvider;
import net.csongradyp.badger.repository.Repository;

public class TimeUnlockedProvider extends UnlockedProvider<TimeAchievementBean> {

    public TimeUnlockedProvider(Repository repository) {
        super(repository);
    }

    public AchievementUnlockedEvent getUnlockable(final String userId, final TimeAchievementBean timeAchievement, final Long score) {
        final List<TimeTrigger> timeTriggers = timeAchievement.getTrigger();
        final String nowString = DateProvider.currentTimeString();
        final Date now = DateProvider.currentTime();
        for (TimeTrigger timeTrigger : timeTriggers) {
            if (timeTrigger.fire(now) && !isUnlocked(userId, timeAchievement.getId())) {
                return UnlockedEventFactory.createEvent(userId, timeAchievement, nowString);
            }
        }
        return null;
    }
}
