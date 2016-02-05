package net.csongradyp.badger.provider.unlock;

import java.util.Date;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.provider.date.DateProvider;

@Named
public class CompositeUnlockedProvider extends UnlockedProvider<CompositeAchievementBean> {

    @Inject
    private DateProvider dateProvider;
    @Inject
    private UnlockedEventFactory unlockedEventFactory;

    @Override
    public Optional<IAchievementUnlockedEvent> getUnlockable(final String userId, final CompositeAchievementBean compositeAchievement, final Long score) {
        final Date currentDate = dateProvider.currentDate();
        final Date currentTime = dateProvider.currentTime();
        if (compositeAchievement.getRelation().evaluate(score, currentDate, currentTime) & !isUnlocked(userId, compositeAchievement.getId())) {
            final AchievementUnlockedEvent achievementUnlockedEvent = unlockedEventFactory.createEvent(userId, compositeAchievement, score.toString());
            return Optional.of(achievementUnlockedEvent);
        }
        return Optional.empty();
    }

    void setUnlockedEventFactory(final UnlockedEventFactory unlockedEventFactory) {
        this.unlockedEventFactory = unlockedEventFactory;
    }

    public void setDateProvider(final DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }
}
