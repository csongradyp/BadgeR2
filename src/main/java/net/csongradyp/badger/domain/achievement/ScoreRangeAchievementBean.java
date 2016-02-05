package net.csongradyp.badger.domain.achievement;

import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;

public class ScoreRangeAchievementBean extends AbstractAchievementBean implements ITriggerableAchievementBean<ScoreTriggerPair> {

    private List<ScoreTriggerPair> triggers;

    @Override
    public List<ScoreTriggerPair> getTrigger() {
        return triggers;
    }

    @Override
    public void setTrigger(final List<ScoreTriggerPair> triggers) {
        this.triggers = triggers;
    }

    @Override
    public AchievementType getType() {
        return AchievementType.SCORE_RANGE;
    }
}
