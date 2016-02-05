package net.csongradyp.badger.repository;

public interface EventRepository {

    Long increment(final String userId, final String event);

    Long setScore(final String userId, final String event, final Long newScore);

    Long scoreOf(final String userId, final String event);

    Boolean resetCounters(final String userId);

}
