package org.androidorm.utils;

import java.lang.annotation.Annotation;

/**
 * Represents class which object can be annotated.
 */
public interface IAnnotable {

    /**
     * Get instance of given annotationClass.
     *
     * @param annotationClass the class of searched annotation
     * @param <T>             type of the annotation
     * @return instance of given annotationClass or null if none is found
     */
    <T extends Annotation> T getAnnotation(Class<T> annotationClass);
}
