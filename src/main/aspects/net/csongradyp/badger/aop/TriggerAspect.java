package net.csongradyp.badger.aop;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.inject.Inject;
import net.csongradyp.badger.AchievementController;
import net.csongradyp.badger.annotations.EventTrigger;
import net.csongradyp.badger.annotations.TriggerValue;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class TriggerAspect extends AchievementAspect {

    @Inject
    private AchievementController achievementController;

    @Pointcut(value = "execution(* *(..)) && @annotation(achievementScore)", argNames = "achievementScore")
    public void triggerEntryPoint(final EventTrigger achievementScore) {
    }

    @After(value = "triggerEntryPoint(achievementScore)", argNames = "joinPoint, achievementScore")
    public void trigger(final JoinPoint joinPoint, final EventTrigger achievementScore) {
        final List<String> events = collectEvents(joinPoint, achievementScore.events());
        final String owner = getOwners(joinPoint).get(0);
        final Long score = getScore(joinPoint);
        for (String event : events) {
            if (score != null) {
                achievementController.triggerEvent(owner, event, score);
            } else {
                achievementController.triggerEvent(owner, event);
            }
        }
    }

    protected Long getScore(final JoinPoint joinPoint) {
        Long score = null;
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Object[] parameterValues = joinPoint.getArgs();
        final Class[] parameterTypes = signature.getParameterTypes();

        final Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            final TriggerValue paramAnnotation = getAnnotationByType(parameterAnnotations[i], TriggerValue.class);
            if (paramAnnotation != null) {
                if (paramAnnotation.getter().isEmpty()) {
                    if (Long.class.equals(parameterTypes[i])) {
                        score = (Long) parameterValues[i];
                    }
                } else {
                    score = callGetter(parameterValues[i], paramAnnotation.getter(), Long.class);
                }
            }
        }
        return score;
    }
}
