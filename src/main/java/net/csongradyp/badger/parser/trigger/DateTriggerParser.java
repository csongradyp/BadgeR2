package net.csongradyp.badger.parser.trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;
import net.csongradyp.badger.provider.date.DateProvider;

public class DateTriggerParser implements ITriggerParser<DateTrigger> {

    @Override
    public List<DateTrigger> parse(final List<String> triggers) {
        final List<DateTrigger> timeTriggers = new ArrayList<>();
        for (String trigger : triggers) {
            final Date date = DateProvider.parseDate(trigger);
            final DateTrigger timeTrigger = new DateTrigger(date);
            timeTriggers.add(timeTrigger);
        }
        return timeTriggers;
    }

}
