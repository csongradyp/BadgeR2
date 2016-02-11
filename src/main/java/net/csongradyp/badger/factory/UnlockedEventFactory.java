package net.csongradyp.badger.factory;

import java.util.ResourceBundle;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.achievement.IAchievement;
import net.csongradyp.badger.event.message.AchievementUnlockedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UnlockedEventFactory {

    private static final Logger LOG = LoggerFactory.getLogger(UnlockedEventFactory.class);

    private static ResourceBundle resourceBundle;

    public static AchievementUnlockedEvent createEvent(final String userId, final IAchievementBean achievementBean, final Integer level, final Long triggeredValue) {
        final AchievementUnlockedEvent achievementUnlockedEvent = createEvent(userId, achievementBean, String.valueOf(triggeredValue));
        achievementUnlockedEvent.setLevel(level);
        LOG.info("Achievement created with id: {} level: {}", achievementBean.getId(), level);
        return achievementUnlockedEvent;
    }

    public static AchievementUnlockedEvent createEvent(final String userId, final IAchievement achievementBean) {
        return createEvent(userId, achievementBean, "");
    }

    public static AchievementUnlockedEvent createEvent(final String userId, final IAchievement achievementBean, final String triggeredValue) {
        final String title;
        final String text;
        if (resourceBundle != null) {
            title = resourceBundle.getString(achievementBean.getTitleKey());
            text = resourceBundle.getString(achievementBean.getTextKey());
        } else {
            title = achievementBean.getTitleKey();
            text = achievementBean.getTextKey();
        }
        LOG.info("Achievement created with id: {}", achievementBean.getId());
        final AchievementUnlockedEvent unlockedEvent = new AchievementUnlockedEvent(userId, achievementBean.getId(), title, text, triggeredValue);
        unlockedEvent.setCategory(achievementBean.getCategory());
        return unlockedEvent;
    }

    public static void setResourceBundle(final ResourceBundle resourceBundle) {
        UnlockedEventFactory.resourceBundle = resourceBundle;
    }

}
