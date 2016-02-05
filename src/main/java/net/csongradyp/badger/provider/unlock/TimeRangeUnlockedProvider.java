package net.csongradyp.badger.provider.unlock;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.provider.date.DateProvider;

@Named
public class TimeRangeUnlockedProvider extends UnlockedProvider<TimeRangeAchievementBean> {

    @Inject
    private DateProvider dateProvider;
    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    @Override
    public Optional<IAchievementUnlockedEvent> getUnlockable(final String userId, final TimeRangeAchievementBean timeAchievement, final Long score) {
        final List<TimeTriggerPair> timeTriggers = timeAchievement.getTrigger();
        for (TimeTriggerPair timeTrigger : timeTriggers) {
            final Date now = dateProvider.currentTime();
            if(timeTrigger.fire(now) && !isUnlocked(userId, timeAchievement.getId())) {
                final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(userId, timeAchievement, dateProvider.currentTimeString());
                return Optional.of(achievementUnlockedEvent);
            }
        }
        return Optional.empty();
    }

    public void setDateProvider(final DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    void setUnlockedEventFactory(final UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }
}
