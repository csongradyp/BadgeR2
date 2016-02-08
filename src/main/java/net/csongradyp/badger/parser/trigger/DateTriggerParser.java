package net.csongradyp.badger.parser.trigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;
import net.csongradyp.badger.provider.date.DateProvider;

@Named
public class DateTriggerParser implements ITriggerParser<DateTrigger> {

    @Inject
    private DateProvider dateProvider;

    @Override
    public List<DateTrigger> parse(final List<String> triggers) {
        final List<DateTrigger> timeTriggers = new ArrayList<>();
        for (String trigger : triggers) {
            final Date date = dateProvider.parseDate(trigger);
            final DateTrigger timeTrigger = new DateTrigger(date);
            timeTriggers.add(timeTrigger);
        }
        return timeTriggers;
    }

}
