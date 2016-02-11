package net.csongradyp.badger.parser.trigger;

import net.csongradyp.badger.domain.AchievementType;

public class TriggerParser {

    private DateTriggerParser dateTriggerParser;
    private TimeTriggerParser timeTriggerParser;
    private ScoreTriggerParser scoreTriggerParser;

    public TriggerParser() {
        dateTriggerParser = new DateTriggerParser();
        timeTriggerParser = new TimeTriggerParser();
        scoreTriggerParser = new ScoreTriggerParser();
    }

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
