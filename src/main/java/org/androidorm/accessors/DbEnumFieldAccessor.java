package org.androidorm.accessors;

import org.androidorm.annotations.EnumType;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.DatabaseStateException;
import org.androidorm.exceptions.ReflectionException;
import org.androidorm.types.SimpleType;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents mapping between database property and enumeration property.
 */
public class DbEnumFieldAccessor extends DbPropertyAccessor {

    private EnumType enumRepresentationType = EnumType.ORDINAL;

    /**
     * Sets how enum will be represented in database.
     *
     * @param enumType enum representation type
     */
    public void setEnumRepresentationType(EnumType enumType) {
        this.enumRepresentationType = enumType;
    }

    /**
     * Returns how enum will be represented in database.
     *
     * @return enum representation type
     */
    public EnumType getEnumRepresentationType() {
        return enumRepresentationType;
    }

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
        return Enum.class.isAssignableFrom(type);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object extractValue(Object object) throws ReflectionException {
        try {
            Enum obj = (Enum) mutator.get(object);
            if (obj == null) {
                return null;
            }

            return enumRepresentationType == EnumType.ORDINAL ? obj.ordinal() : obj.name();
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Can't extract %s %s", e, getName(), e.getMessage());
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Can't extract %s %s", e, getName(), e.getMessage());
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Can't extract %s %s", e, getName(), e.getMessage());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public boolean setValue(Object object, Object value) throws AndroidOrmException {
        try {
            Class<Enum> enumClass = (Class<Enum>) getRealType();

            if (enumRepresentationType == EnumType.STRING) {
                mutator.set(object, value == null ? null : Enum.valueOf(enumClass, (String) value));
            } else {
                mutator.set(object, value == null ? null : enumClass.getEnumConstants()[(Integer) value]);
            }
        } catch (IllegalArgumentException e) {
            throw new DatabaseStateException("Can't set %s. Enum with string %s not found", e, getName(), value);
        } catch (ClassCastException e) {
            throw new ReflectionException("Can't set %s %s", e, getName(), e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DatabaseStateException("Can't set %s. Enum with ordinal %s not found", e, getName(), e.getMessage());
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Can't set %s %s", e, getName(), e.getMessage());
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Can't set %s %s", e, getName(), e.getMessage());
        }

        return true;
    }

    @Override
    public SimpleType getType() {
        return enumRepresentationType == EnumType.ORDINAL ? SimpleType.INTEGER : SimpleType.STRING;
    }
}
