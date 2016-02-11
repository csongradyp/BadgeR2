package net.csongradyp.badger.provider.unlock;

import java.util.Date;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.provider.date.DateProvider;
import net.csongradyp.badger.repository.Repository;

public class CompositeUnlockedProvider extends UnlockedProvider<CompositeAchievementBean> {

    public CompositeUnlockedProvider(Repository repository) {
        super(repository);
    }

    @Override
    public AchievementUnlockedEvent getUnlockable(final String userId, final CompositeAchievementBean compositeAchievement, final Long score) {
        final Date currentDate = DateProvider.currentDate();
        final Date currentTime = DateProvider.currentTime();
        if (compositeAchievement.getRelation().evaluate(score, currentDate, currentTime) & !isUnlocked(userId, compositeAchievement.getId())) {
            return UnlockedEventFactory.createEvent(userId, compositeAchievement, score.toString());
        }
        return null;
    }

}
