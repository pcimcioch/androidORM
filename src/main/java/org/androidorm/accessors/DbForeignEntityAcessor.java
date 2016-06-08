package org.androidorm.accessors;

import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.ReflectionException;
import org.androidorm.mutator.IMutator;
import org.androidorm.types.SimpleType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * Represents mapping between database foreign key property and java property.
 */
public class DbForeignEntityAcessor extends DbAccessor {

    private IMutator mutator;

    private DbAccessor idAccessor;

    /**
     * Configure accessor.
     *
     * @param mutatorArg    mutator to set and obtain foreign entity object
     * @param idAccessorArg foreign entity id accessor
     * @throws AndroidOrmException the exception if mutator name is incorrect thrown by {@link #setName(String)}
     */
    public void configure(IMutator mutatorArg, DbAccessor idAccessorArg) throws AndroidOrmException {
        setName(mutatorArg.getName() + "_ID");
        this.mutator = mutatorArg;
        this.idAccessor = idAccessorArg;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return mutator.getAnnotation(annotationClass);
    }

    @Override
    public Class<?> getRealType() {
        return mutator.getType();
    }

    @Override
    public SimpleType getType() {
        return idAccessor.getType();
    }

    @Override
    public Object extractValue(Object object) throws ReflectionException {
        if (object == null) {
            return null;
        }

        Object foreignEntity = extractForeignEntity(object);
        if (foreignEntity == null) {
            return null;
        }

        return idAccessor.extractValue(foreignEntity);
    }

    @Override
    public boolean setValue(Object object, Object value) throws ReflectionException {
        return false;
    }

    /**
     * Extracts and returns object of foreign entity class.
     *
     * @param ownerEntity entity object from which value will be extracted
     * @return extracted foreign entity object
     * @throws ReflectionException if extraction went wrong
     */
    public Object extractForeignEntity(Object ownerEntity) throws ReflectionException {
        try {
            return mutator.get(ownerEntity);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Can't extract %s %s", e, getName(), e.getMessage());
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Can't extract %s %s", e, getName(), e.getMessage());
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Can't extract %s %s", e, getName(), e.getMessage());
        }
    }

    /**
     * Sets object of foreign entity class.
     *
     * @param ownerEntity   object to which foreign object should be set
     * @param foreignEntity foreign entity object
     * @throws ReflectionException if setting went wrong
     */
    public void setForeignEntity(Object ownerEntity, Object foreignEntity) throws ReflectionException {
        try {
            mutator.set(ownerEntity, foreignEntity);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Can't set %s %s", e, getName(), e.getMessage());
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Can't set %s %s", e, getName(), e.getMessage());
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Can't set %s %s", e, getName(), e.getMessage());
        }
    }

    @Override
    public String getFieldName() {
        return mutator.getName();
    }
}
