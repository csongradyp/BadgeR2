package net.csongradyp.badger.parser.trigger;

import java.util.ArrayList;
import java.util.List;
import net.csongradyp.badger.domain.achievement.trigger.TimeTrigger;
import net.csongradyp.badger.provider.date.DateProvider;
import org.joda.time.LocalTime;

public class TimeTriggerParser implements ITriggerParser<TimeTrigger> {

    @Override
    public List<TimeTrigger> parse(final List<String> triggers) {
        final List<TimeTrigger> timeTriggers = new ArrayList<>();
        for (String trigger : triggers) {
            final LocalTime date = DateProvider.parseTime(trigger);
            final TimeTrigger timeTrigger = new TimeTrigger(date);
            timeTriggers.add(timeTrigger);
        }
        return timeTriggers;
    }
}
