package org.androidorm.mutator;

import org.androidorm.annotations.AnnotationApplier;
import org.androidorm.exceptions.MappingException;
import org.androidorm.utils.ReflectionHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Util helper for mutators.
 */
public final class MutatorHelper {

    private MutatorHelper() {

    }

    /**
     * Get mutators for given class that are accessible and annotated with any of given annotation.
     *
     * @param clazz       class type
     * @param annotations annotations
     * @return list of mutators
     * @throws MappingException if mapping is incorrect
     */
    public static List<IMutator> getAccesibleMutators(Class<?> clazz, Collection<Class<? extends Annotation>> annotations) throws MappingException {
        List<IMutator> mutators = new ArrayList<IMutator>();

        for (Field field : ReflectionHelper.getAllAccessibleFields(clazz)) {
            if (checkIntersection(field.getAnnotations(), annotations)) {
                mutators.add(new FieldMutator(field));
            }
        }

        for (Method method : getOrmAnnotatedMethods(clazz)) {
            if (checkIntersection(method.getAnnotations(), annotations)) {
                mutators.add(new MethodMutator(method));
            }
        }

        return mutators;
    }

    /**
     * Check if intersection of toCheck annotations and allowedAnnoatations is not empty.
     *
     * @param toCheck            first collection of annotations
     * @param allowedAnnotations second collections of annotations
     * @return if intersection exists
     */
    private static boolean checkIntersection(Annotation[] toCheck, Collection<Class<? extends Annotation>> allowedAnnotations) {
        if (allowedAnnotations == null) {
            return true;
        }

        for (Annotation annotation : toCheck) {
            for (Class<? extends Annotation> allowedClass : allowedAnnotations) {
                if (annotation.annotationType().equals(allowedClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get given class methods annotated with orm annotations.
     *
     * @param clazz class type
     * @return list of orm annotated methods
     */
    private static List<Method> getOrmAnnotatedMethods(Class<?> clazz) {
        List<Method> annotatedMethods = new ArrayList<Method>();
        for (Method method : ReflectionHelper.getAllAccessibleMethods(clazz)) {
            if (AnnotationApplier.containOrmAnnotation(method.getAnnotations())) {
                annotatedMethods.add(method);
            }
        }

        return annotatedMethods;
    }
}
