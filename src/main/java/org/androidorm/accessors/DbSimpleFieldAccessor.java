package org.androidorm.accessors;

import org.androidorm.types.SimpleType;

/**
 * Represents mapping between database property and Simple java property.
 *
 * @see SimpleType
 */
public class DbSimpleFieldAccessor extends DbPropertyAccessor {

    @Override
    public boolean isTypeSupported(Class<?> type) {
        return isTypeSupportedStatic(type);
    }

    /**
     * Static method to check if given type is supported by this accessor.
     *
     * @param type type to check
     * @return if given type is supported
     */
    public static boolean isTypeSupportedStatic(Class<?> type) {
        return SimpleType.isSimple(type);
    }
}
