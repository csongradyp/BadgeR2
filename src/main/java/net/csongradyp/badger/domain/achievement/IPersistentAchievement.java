package net.csongradyp.badger.domain.achievement;

import java.util.Date;

public interface IPersistentAchievement {

    String getId();

    void setId(String id);

    Date getAcquireDate();

    void setAcquireDate(Date acquireDate);

    Integer getLevel();

    void setLevel(Integer level);

}
