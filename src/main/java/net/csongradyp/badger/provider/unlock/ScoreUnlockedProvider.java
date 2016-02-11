package net.csongradyp.badger.provider.unlock;

import java.util.List;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.achievement.ScoreAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import net.csongradyp.badger.factory.UnlockedEventFactory;
import net.csongradyp.badger.repository.Repository;

public class ScoreUnlockedProvider extends UnlockedProvider<ScoreAchievementBean> {

    public ScoreUnlockedProvider(Repository repository) {
        super(repository);
    }

    @Override
    public AchievementUnlockedEvent getUnlockable(final String userId, final ScoreAchievementBean achievementBean, final Long currentValue) {
        final List<ScoreTrigger> triggers = achievementBean.getTrigger();
        for (int i = 0; i < triggers.size(); i++) {
            final Integer level = i + 1;
            if (triggers.get(i).fire(currentValue) && isLevelValid(achievementBean, level) && !isLevelUnlocked(userId, achievementBean.getId(), level)) {
                return UnlockedEventFactory.createEvent(userId, achievementBean, level, currentValue);
            }
        }
        return null;
    }

    private boolean isLevelValid(final IAchievementBean counterAchievement, final Integer triggerIndex) {
        return counterAchievement.getMaxLevel() >= triggerIndex;
    }

    private Boolean isLevelUnlocked(final String userId, final String id, final Integer level) {
        return repository.achievement().isUnlocked(userId, id, level);
    }

}
