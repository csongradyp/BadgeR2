package net.csongradyp.badger;

import java.io.File;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import net.csongradyp.badger.domain.achievement.IAchievement;
import net.csongradyp.badger.event.EventBus;
import net.csongradyp.badger.event.IAchievementUnlockedHandler;
import net.csongradyp.badger.event.IScoreUpdateHandler;
import net.csongradyp.badger.event.wrapper.AchievementUnlockedHandlerWrapper;
import net.csongradyp.badger.event.wrapper.ScoreUpdateHandlerWrapper;
import net.csongradyp.badger.parser.json.AchievementJsonParser;
import net.csongradyp.badger.repository.BadgerRepository;
import net.csongradyp.badger.repository.Repository;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Badger {

    private static final String CONTEXT_XML_PATH = "META-INF/beans.xml";

    private final AchievementJsonParser parser;
    private final AchievementController controller;
    private final EventBus eventBus;
    private final Repository repository;

    /**
     * Default constructor to set up Spring environment.
     */
    private Badger() {
//        ApplicationContext applicationContext = SpringApplication.run(Badger.class);
        final ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext(CONTEXT_XML_PATH);
        applicationContext.registerShutdownHook();
        parser = applicationContext.getBean(AchievementJsonParser.class);
        controller = applicationContext.getBean(AchievementController.class);
        eventBus = applicationContext.getBean(EventBus.class);
        repository = applicationContext.getBean(Repository.class);
    }

    /**
     * Starts the BadgeR achievement engine without i18n support.
     *
     * @param definitionFile {@link File} instance which represents the achievement definition file.
     */
    public Badger(final File definitionFile, final BadgerRepository badgerRepository) {
        this();
        controller.setAchievementDefinition(parser.parse(definitionFile));
        repository.setAchievementRepository(badgerRepository);
        repository.setEventRepository(badgerRepository);
    }

    /**
     * Starts the BadgeR achievement engine without i18n support.
     *
     * @param definitionFilePath Absolute path of the achievement definition file location.
     */
    public Badger(final String definitionFilePath, final BadgerRepository badgerRepository) {
        this(new File(definitionFilePath), badgerRepository );
    }

    /**
     * @param baseName i18n properties file base name for internationalization support.<br/>
     *                 See more at <a href="http://csongradyp.github.io/badgeR/">BadgeR API documentation</a>.
     */
    public void setInternationalizationBaseName(final String baseName) {
        controller.setInternationalizationBaseName(baseName);
    }

    public void setResourceBundle(final ResourceBundle resourceBundle) {
        controller.setResourceBundle(resourceBundle);
    }

    /**
     * Set the language to use for resolving unlocked achievements title and description.
     *
     * @param locale {@link Locale} instance. Default locale is {@code Locale.ENGLISH} (en)
     */
    public void setLocale(final Locale locale) {
        controller.setLocale(locale);
    }

    /**
     * Returns all defined achievements without any sorting.
     *
     * @return {@link Collection} of {@link IAchievement} instances.
     */
    public Collection<IAchievement> getAllAchievement() {
        return controller.getAll();
    }

    /**
     * Returns all defined achievements belong to the given owner.
     *
     * @param owner Owners of the unlocked achievement.
     * @return {@link Collection} of {@link IAchievement} instances.
     */
    public Collection<IAchievement> getAchievementsByOwner(final String owner) {
        return controller.getAllByOwner(owner);
    }

    /**
     * Returns all defined achievements sorted by event subsciprions.
     *
     * @return {@link Map} of event name and {@link IAchievement} pairs.
     */
    public Map<String, Set<IAchievement>> getAllAchievementByEvent() {
        return controller.getAllByEvents();
    }

    public Optional<IAchievement> getAchievement(final String id) {
        return controller.get(id);
    }

    public Boolean isUnlocked(final String userId, final String id) {
        return controller.isUnlocked(userId, id);
    }

    public Boolean isUnlocked(final String userId, final String id, final Integer level) {
        return controller.isUnlocked(userId, id, level);
    }

    /**
     * Triggers the given event and increment its counter by one.
     *
     * @param event Previously defined event in the achievement definition file.
     */
    public void triggerEvent(final String userId, final String event) {
        controller.triggerEvent(userId, event);
    }

    /**
     * Triggers the given event and sets its counter by the given score.
     *
     * @param event Previously defined event in the achievement definition file.
     * @param score new value of the event counter.
     */
    public void triggerEvent(final String userId, final String event, final Long score) {
        controller.triggerEvent(userId, event, score);
    }

    /**
     * Triggers the given event and sets its counter by the given score.
     *
     * @param event     Previously defined event in the achievement definition file.
     * @param highScore new value of the event counter. New value will be only applied if its greater than the stored one.
     */
    public void triggerEventWithHighScore(final String userId, final String event, final Long highScore) {
        controller.triggerEventWithHighScore(userId, event, highScore);
    }

    /**
     * Returns the current value of the event counter.
     *
     * @param event Previously defined event in the achievement definition file.
     * @return current value of event counter.
     */
    public Long getCurrentScore(final String userId, final String event) {
        return controller.getCurrentScore(userId, event);
    }

    /**
     * Subscribe a handler to receive achievement unlocked events.
     *
     * @param achievementUnlockedHandler {@link IAchievementUnlockedHandler} implementation to be register.
     */
    public void subscribeOnUnlock(final IAchievementUnlockedHandler achievementUnlockedHandler) {
        eventBus.subscribeOnUnlock(new AchievementUnlockedHandlerWrapper(achievementUnlockedHandler));
    }

    /**
     * Unsubscribe registered unlocked event handler.
     *
     * @param achievementUnlockedHandler previously registered {@link IAchievementUnlockedHandler} implementation.
     */
    public void unSubscribeOnUnlock(final IAchievementUnlockedHandler achievementUnlockedHandler) {
        eventBus.unSubscribeOnUnlock(achievementUnlockedHandler);
    }

    /**
     * Subscribe a handler to receive achievement event counter or score update events.
     *
     * @param achievementUpdateHandler {@link IScoreUpdateHandler} implementation to be register.
     */
    public void subscribeOnScoreChanged(final IScoreUpdateHandler achievementUpdateHandler) {
        eventBus.subscribeOnScoreChanged(new ScoreUpdateHandlerWrapper(achievementUpdateHandler));
    }

    /**
     * Unsubscribe registered score update event handler.
     *
     * @param achievementUpdateHandler previously registered {@link IScoreUpdateHandler} implementation.
     */
    public void unSubscribeOnScoreChanged(final IScoreUpdateHandler achievementUpdateHandler) {
        eventBus.unSubscribeOnScoreChanged(achievementUpdateHandler);
    }

    /**
     * Clears all unlocked achievements and counter/event states.
     * Data deletion after calling this method cannot be undone.
     */
    public void reset(final String userId) {
        controller.reset(userId);
    }

    public AchievementController getController() {
        return controller;
    }
}
