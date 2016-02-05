package net.csongradyp.badger.domain.achievement;

import java.util.ArrayList;
import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.TimeTrigger;

public class TimeAchievementBean extends AbstractAchievementBean implements ITriggerableAchievementBean<TimeTrigger> {

    private List<TimeTrigger> trigger;

    public TimeAchievementBean() {
        trigger = new ArrayList<>();
    }

    @Override
    public List<TimeTrigger> getTrigger() {
        return trigger;
    }

    public void setTrigger(final List<TimeTrigger> trigger) {
        this.trigger = trigger;
    }

    @Override
    public AchievementType getType() {
        return AchievementType.TIME;
    }
}
