package net.csongradyp.badger.provider.unlock;

import java.util.Date;
import java.util.List;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.provider.date.DateProvider;
import net.csongradyp.badger.repository.Repository;

public class DateUnlockedProvider extends UnlockedProvider<DateAchievementBean> {

    public DateUnlockedProvider(Repository repository) {
        super(repository);
    }

    @Override
    public AchievementUnlockedEvent getUnlockable(final String userId, final DateAchievementBean dateAchievement, final Long score) {
        final List<DateTrigger> dateTriggers = dateAchievement.getTrigger();
        final String nowString = DateProvider.currentDateString();
        final Date now = DateProvider.currentDate();
        for (DateTrigger dateTrigger : dateTriggers) {
            if (dateTrigger.fire(now) && !isUnlocked(userId, dateAchievement.getId())) {
                return UnlockedEventFactory.createEvent(userId, dateAchievement, nowString);
            }
        }
        return null;
    }

}
