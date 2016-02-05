package net.csongradyp.badger.event;

import java.util.Date;

public interface IAchievementUnlockedEvent {

    String getId();

    String getCategory();

    String getTitle();

    String getText();

    Date getAcquireDate();

    String getTriggerValue();

    Integer getLevel();

    void setLevel(Integer level);

    AchievementEventType getEventType();

    String getOwner();

}
