package net.csongradyp.badger.provider.unlock;

import javax.inject.Inject;
import net.csongradyp.badger.domain.achievement.IAchievement;
import net.csongradyp.badger.provider.IUnlockedProvider;
import net.csongradyp.badger.repository.Repository;

abstract class UnlockedProvider<TYPE extends IAchievement> implements IUnlockedProvider<TYPE> {

    @Inject
    protected Repository repository;

    public Boolean isUnlocked(final String userId, final String achievementId) {
        return repository.achievement().isUnlocked(userId, achievementId);
    }

}
