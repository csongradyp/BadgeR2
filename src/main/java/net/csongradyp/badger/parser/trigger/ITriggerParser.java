package net.csongradyp.badger.parser.trigger;

import java.util.List;

public interface ITriggerParser<T> {

    List<T> parse(List<String> triggers);

}
