package net.csongradyp.badger.domain.achievement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.csongradyp.badger.domain.AchievementType;

public class UnlockedAchievement {

    private String id;
    private String category;
    private final List<String> subscriptions;
    private Integer level;
    private AchievementType type;
    private String titleKey;
    private String textKey;
    private Date acquireDate;

    public UnlockedAchievement() {
        subscriptions = new ArrayList<>();
        level = 1;
        category = "default";
    }

    public UnlockedAchievement(String id, String category, Integer level, AchievementType type, String titleKey, String textKey, Date acquireDate) {
        this();
        this.id = id;
        this.category = category;
        this.level = level;
        this.type = type;
        this.titleKey = titleKey;
        this.textKey = textKey;
        this.acquireDate = acquireDate;
    }

    public String getId() {
        return id;
    }

    public Date getAcquireDate() {
        return acquireDate;
    }

    public void setAcquireDate(Date acquireDate) {
        this.acquireDate = acquireDate;
    }

    public Integer getLevel() {
        return level;
    }

    public String getCategory() {
        return category;
    }

    public AchievementType getType() {
        return type;
    }

    public List<String> getSubscriptions() {
        return subscriptions;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public String getTextKey() {
        return textKey;
    }
}

