package org.androidorm.mutator;

import org.androidorm.types.CollectionType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * Class responsible for setting databse property values to entity class objects.
 */
public interface IMutator {

    /**
     * Gets the type this mutator can set / extract.
     *
     * @return mutator type
     */
    Class<?> getType();

    /**
     * If this mutator supports collections this method will return what types of collections are supported by it.
     *
     * @return type of supported collection or null if this mutator does not support ollections
     */
    CollectionType getCollectionType();

    /**
     * If this mutator supports collections this method will return type that collection holds.
     *
     * @return collection type
     */
    Class<?> getCollectionParameterizedType();

    /**
     * If object used by this mutator is annotated with annotation of given type, than instance of annotation will be returned.
     *
     * @param annotationClass type of annoattion
     * @param <T>             annotation type
     * @return instance of given annotation type or null if mutator's object is not annotated with annotationClass
     */
    <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    /**
     * Return mutator name.
     *
     * @return mutator name
     */
    String getName();

    /**
     * Sets value to object using this mutator.
     *
     * @param target object to be injected with value
     * @param value  value to set
     * @throws IllegalArgumentException  if value cannot be set
     * @throws IllegalAccessException    if value cannot be set
     * @throws InvocationTargetException if value cannot be set
     */
    void set(Object target, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;

    /**
     * Extracts value from object using this mutator.
     *
     * @param target object to extract value from
     * @return extracted value
     * @throws IllegalArgumentException  if value cannot be obtained
     * @throws IllegalAccessException    if value cannot be obtained
     * @throws InvocationTargetException if value cannot be obtained
     */
    Object get(Object target) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
}
