package net.csongradyp.badger.domain.achievement;

import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;

public class TimeRangeAchievementBean extends AbstractAchievementBean implements ITriggerableAchievementBean<TimeTriggerPair> {

    private List<TimeTriggerPair> triggers;

    @Override
    public List<TimeTriggerPair> getTrigger() {
        return triggers;
    }

    @Override
    public void setTrigger(final List<TimeTriggerPair> triggers) {
        this.triggers = triggers;
    }

    @Override
    public AchievementType getType() {
        return AchievementType.TIME_RANGE;
    }
}
