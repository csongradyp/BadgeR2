package net.csongradyp.badger.domain.achievement;

import java.util.List;
import net.csongradyp.badger.domain.AchievementType;

public interface IAchievement {

    String getId();

    String getCategory();

    List<String> getSubscriptions();

    String getTitleKey();

    String getTextKey();

    Integer getMaxLevel();

    AchievementType getType();

}
