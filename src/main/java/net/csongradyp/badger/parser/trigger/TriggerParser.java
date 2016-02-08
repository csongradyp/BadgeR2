package net.csongradyp.badger.parser.trigger;

import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.AchievementType;

@Named
public class TriggerParser {

    @Inject
    private DateTriggerParser dateTriggerParser;
    @Inject
    private TimeTriggerParser timeTriggerParser;
    @Inject
    private ScoreTriggerParser scoreTriggerParser;

    public ScoreTriggerParser score() {
        return scoreTriggerParser;
    }

    public DateTriggerParser date() {
        return dateTriggerParser;
    }

    public TimeTriggerParser time() {
        return timeTriggerParser;
    }

    public ITriggerParser get(AchievementType type) {
        ITriggerParser parser = null;
        if (type == AchievementType.DATE) {
            parser = dateTriggerParser;
        } else if (type == AchievementType.TIME) {
            parser = timeTriggerParser;
        } else if (type == AchievementType.SCORE) {
            parser = scoreTriggerParser;
        }
        return parser;
    }
}
