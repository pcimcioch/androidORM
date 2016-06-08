package org.androidorm.accessors;

import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.AssertException;
import org.androidorm.exceptions.ReflectionException;
import org.androidorm.mutator.IMutator;
import org.androidorm.types.SimpleType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * Represents mapping between simple database property and java property.
 */
public abstract class DbPropertyAccessor extends DbAccessor {

    protected IMutator mutator;

    private SimpleType simpleType;

    /**
     * Configures mutator that is used to access java object property.
     *
     * @param mutator mutator to configure
     * @throws AndroidOrmException if the mutator type is not supported by this accessor
     */
    public void setMutator(IMutator mutator) throws AndroidOrmException {
        if (!isTypeSupported(mutator.getType())) {
            throw new AssertException("Type %s is not allowed", mutator.getType());
        }

        this.mutator = mutator;
        this.simpleType = SimpleType.convert(mutator.getType());
        setName(mutator.getName());
    }

    /**
     * Returns if given type is supported by this accessor.
     *
     * @param type type to check
     * @return if given type is supported
     */
    public abstract boolean isTypeSupported(Class<?> type);

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return mutator.getAnnotation(annotationClass);
    }

    @Override
    public Object extractValue(Object target) throws ReflectionException {
        try {
            return mutator.get(target);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Can't extract %s %s", e, getName(), e.getMessage());
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Can't extract %s %s", e, getName(), e.getMessage());
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Can't extract %s %s", e, getName(), e.getMessage());
        }
    }

    @Override
    public boolean setValue(Object target, Object value) throws AndroidOrmException {
        try {
            mutator.set(target, value);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Can't set %s %s", e, getName(), e.getMessage());
        } catch (ClassCastException e) {
            throw new ReflectionException("Can't set %s %s", e, getName(), e.getMessage());
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Can't set %s %s", e, getName(), e.getMessage());
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Can't set %s %s", e, getName(), e.getMessage());
        }

        return true;
    }

    @Override
    public Class<?> getRealType() {
        return mutator.getType();
    }

    @Override
    public SimpleType getType() {
        return simpleType;
    }

    @Override
    public String getFieldName() {
        return mutator.getName();
    }
}
