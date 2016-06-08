package org.androidorm.annotations;

import org.androidorm.exceptions.MappingException;
import org.androidorm.utils.IAnnotable;

import java.lang.annotation.Annotation;

/**
 * Applier for applying annotation effects on object.
 *
 * @param <T> type of object that is annotated
 * @param <K> type of annotation supported by this applier
 */
public abstract class Applier<T extends IAnnotable, K extends Annotation> {

    /**
     * Apply this applier to annotable object.
     *
     * @param annotable object to apply to
     * @return if annotation was found and applied
     * @throws MappingException if anything went wrong during appling
     */
    public boolean apply(T annotable) throws MappingException {
        K annotation = getAnnotation(annotable);
        if (annotation == null) {
            return false;
        }

        apply(annotable, annotation);
        return true;
    }

    /**
     * Gets annotation from annotable.
     *
     * @param annotable object to extract annotation from
     * @return instance of annottion or null if annotation couldn't be found
     */
    public K getAnnotation(T annotable) {
        return annotable.getAnnotation(getAnnotationType());
    }

    /**
     * Apply this applier to annotable object using concrete instance of supported annotation.
     *
     * @param annotable  annotated object
     * @param annotation instance of annotation
     * @throws MappingException if anything went wrong
     */
    protected abstract void apply(T annotable, K annotation) throws MappingException;

    /**
     * Get annotation type class.
     *
     * @return annotation type class
     */
    public abstract Class<K> getAnnotationType();
}
