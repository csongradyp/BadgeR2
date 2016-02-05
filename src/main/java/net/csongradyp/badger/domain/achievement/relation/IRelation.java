package net.csongradyp.badger.domain.achievement.relation;

import java.util.Date;

public interface IRelation {

    Boolean evaluate(Long score, Date date, Date time);
}
