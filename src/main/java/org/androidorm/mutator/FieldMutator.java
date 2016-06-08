package org.androidorm.mutator;

import org.androidorm.exceptions.MappingException;
import org.androidorm.types.CollectionType;
import org.androidorm.utils.ReflectionHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Mutator that uses fields to set / get values.
 */
public class FieldMutator implements IMutator {

    private final Field field;

    private Class<?> collectionParametrizedType;

    private CollectionType collectionType;

    /**
     * Constructor.
     *
     * @param field field used by this mutator
     * @throws MappingException if mapping is incorrect
     */
    public FieldMutator(Field field) throws MappingException {
        this.field = field;
        this.field.setAccessible(true);

        tryConfigureCollection();
    }

    /**
     * If field is collection field, configures this mutator accordingly.
     *
     * @throws MappingException if mapping is incorrect
     */
    private void tryConfigureCollection() throws MappingException {
        collectionType = CollectionType.getCollectionType(getType());
        if (collectionType != null) {
            Type genType = field.getGenericType();
            if (genType instanceof ParameterizedType) {
                collectionParametrizedType = ReflectionHelper.getActualTypeOfParametrizedType((ParameterizedType) genType);
            } else {
                throw new MappingException("Type of field %s can't be raw type", field.getName());
            }
        }
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    @Override
    public String getName() {
        return field.getName();
    }

    @Override
    public void set(Object target, Object value) throws IllegalArgumentException, IllegalAccessException {
        field.set(target, value);
    }

    @Override
    public Object get(Object target) throws IllegalArgumentException, IllegalAccessException {
        return field.get(target);
    }

    @Override
    public Class<?> getCollectionParameterizedType() {
        return collectionParametrizedType;
    }

    @Override
    public CollectionType getCollectionType() {
        return collectionType;
    }

}
