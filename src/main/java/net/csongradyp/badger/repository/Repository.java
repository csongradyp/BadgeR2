package net.csongradyp.badger.repository;

public class Repository {

    private EventRepository eventRepository;
    private AchievementRepository achievementRepository;

    public EventRepository event() {
        return eventRepository;
    }

    public AchievementRepository achievement() {
        return achievementRepository;
    }

    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void setAchievementRepository(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;
    }
}
