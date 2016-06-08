package org.androidorm.accessors;

import org.androidorm.exceptions.ReflectionException;
import org.androidorm.types.SimpleType;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Represents mapping between database property and {@link java.util.Date} or {@link java.sql.Date} property.
 */
public class DbDateFieldAccessor extends DbPropertyAccessor {

    @Override
    public boolean isTypeSupported(Class<?> type) {
        return isTypeSupportedStatic(type);
    }

    /**
     * Static method that checks if given type is supported by this accessor.
     *
     * @param type type to check
     * @return if given type is supported
     */
    public static boolean isTypeSupportedStatic(Class<?> type) {
        return type.equals(Date.class) || type.equals(java.sql.Date.class);
    }

    @Override
    public Object extractValue(Object object) throws ReflectionException {
        try {
            Object obj = mutator.get(object);
            if (obj == null) {
                return null;
            }
            return ((Date) obj).getTime();
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Can't extract %s " + e.getMessage(), e, getName());
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Can't extract %s " + e.getMessage(), e, getName());
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Can't extract %s " + e.getMessage(), e, getName());
        }
    }

    @Override
    public boolean setValue(Object object, Object value) throws ReflectionException {
        try {
            if (mutator.getType().equals(Date.class)) {
                mutator.set(object, value == null ? null : new Date((Long) value));
            } else {
                mutator.set(object, value == null ? null : new java.sql.Date((Long) value));
            }
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
    public SimpleType getType() {
        return SimpleType.LONG;
    }
}
