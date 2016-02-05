package net.csongradyp.badger.domain.achievement;

import java.util.ArrayList;
import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;

public class DateAchievementBean extends AbstractAchievementBean implements ITriggerableAchievementBean<DateTrigger> {

    private List<DateTrigger> trigger;

    public DateAchievementBean() {
        trigger = new ArrayList<>();
    }

    @Override
    public List<DateTrigger> getTrigger() {
        return trigger;
    }

    @Override
    public void setTrigger(final List<DateTrigger> trigger) {
        this.trigger = trigger;
    }

    @Override
    public AchievementType getType() {
        return AchievementType.DATE;
    }
}
