package net.csongradyp.badger.parser;

import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievementBean;
import net.csongradyp.badger.domain.achievement.CompositeAchievementBean;
import net.csongradyp.badger.domain.achievement.DateAchievementBean;
import net.csongradyp.badger.domain.achievement.ScoreAchievementBean;
import net.csongradyp.badger.domain.achievement.ScoreRangeAchievementBean;
import net.csongradyp.badger.domain.achievement.SingleAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeAchievementBean;
import net.csongradyp.badger.domain.achievement.TimeRangeAchievementBean;

public class AchievementFactory {

    private AchievementFactory() {
    }
    
    public static IAchievementBean create(final AchievementType type) {
        IAchievementBean achievementBean = null;
        switch (type) {
            case DATE:
                achievementBean = new DateAchievementBean();
                break;
            case TIME:
                achievementBean = new TimeAchievementBean();
                break;
            case TIME_RANGE:
                achievementBean = new TimeRangeAchievementBean();
                break;
            case SCORE:
                achievementBean = new ScoreAchievementBean();
                break;
            case SCORE_RANGE:
                achievementBean =  new ScoreRangeAchievementBean();
                break;
            case COMPOSITE:
                achievementBean =  new CompositeAchievementBean();
                break;
            case SINGLE:
                achievementBean = new SingleAchievementBean();
                break;
        }
        return achievementBean;
    }
}
