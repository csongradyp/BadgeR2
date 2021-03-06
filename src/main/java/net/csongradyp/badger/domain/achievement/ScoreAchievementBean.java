package net.csongradyp.badger.domain.achievement;

import java.util.ArrayList;
import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;

public class ScoreAchievementBean extends AbstractAchievementBean implements ITriggerableAchievementBean<ScoreTrigger> {

    private List<ScoreTrigger> trigger;

    public ScoreAchievementBean() {
        trigger = new ArrayList<>();
    }

    @Override
    public List<ScoreTrigger> getTrigger() {
        return trigger;
    }

    public void setTrigger(List<ScoreTrigger> trigger) {
        this.trigger = trigger;
        setMaxLevel(this.trigger.size());
    }

    @Override
    public AchievementType getType() {
        return AchievementType.SCORE;
    }
}
