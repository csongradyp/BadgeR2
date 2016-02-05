package net.csongradyp.badger.repository;

import java.util.Collection;
import java.util.Date;
import net.csongradyp.badger.domain.IAchievementBean;

public interface AchievementRepository {

    void unlock(final String userId, final String achievementId);

    void unlock(final String userId, final String achievementId, final Integer level);

     void clearAchievements(final String userId);

    Long getNumberOfUnlocked();

    Collection<IAchievementBean> getAll(final String userId);

    /**
     * Is the achievement with the given name unlocked.
     *
     * @param id name of Achievement
     * @return {@code true} if the achievement is already unlocked.
     */
    Boolean isUnlocked(final String userId, final String id);

    Boolean isUnlocked(final String userId, final String id, final Integer level);

    Date getAcquireDate(final String userId, final String achievementId);

}
