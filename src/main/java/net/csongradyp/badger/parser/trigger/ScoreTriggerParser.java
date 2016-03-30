package net.csongradyp.badger.parser.trigger;

import java.util.ArrayList;
import java.util.List;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTrigger;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;

public class ScoreTriggerParser implements ITriggerParser<ScoreTrigger> {

    private static final String NUMBER_TRIGGER_PATTERN = "(^\\d+$)|(^\\d+(\\+|-)$)";
    private static final String TRIGGER_FORMULA_PATTERN = "^\\d+(\\+|-|\\*)\\d+$";

    @Override
    public List<ScoreTrigger> parse(final List<String> scoreTriggers) {
        final ArrayList<ScoreTrigger> triggers = new ArrayList<>();
        if(scoreTriggers.size() == 1 && scoreTriggers.get(0).matches(TRIGGER_FORMULA_PATTERN)) {
            final String formula = scoreTriggers.get(0);
            final String[] numbers = formula.split("(\\+|-|\\*)");

        } else {
            for (String triggerDefinition : scoreTriggers) {
                validate(triggerDefinition);
                final ScoreTrigger scoreTrigger;
                if (isOperational(triggerDefinition)) {
                    final Long triggerValue = Long.parseLong(triggerDefinition.substring(0, triggerDefinition.length() - 1));
                    final ScoreTrigger.Operation operation = ScoreTrigger.Operation.parse(triggerDefinition.substring(triggerDefinition.length() - 1));
                    scoreTrigger = new ScoreTrigger(triggerValue, operation);
                } else {
                    final long triggerValue = Long.parseLong(triggerDefinition);
                    scoreTrigger = new ScoreTrigger(triggerValue);
                }
                triggers.add(scoreTrigger);
            }
        }
        return triggers;
    }

    private void validate(String triggerDefinition) {
        if (!triggerDefinition.matches(NUMBER_TRIGGER_PATTERN)) {
            throw new MalformedAchievementDefinition("Invalid score trigger: " + triggerDefinition);
        }
    }

    private Boolean isOperational(String triggerDefinition) {
        return triggerDefinition.endsWith(ScoreTrigger.Operation.GREATER_THAN.getOperator()) || triggerDefinition.endsWith(ScoreTrigger.Operation.LESS_THAN.getOperator());
    }
}
