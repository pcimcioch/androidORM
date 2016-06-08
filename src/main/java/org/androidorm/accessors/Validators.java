package org.androidorm.accessors;

import org.androidorm.exceptions.MappingException;

import java.util.Collection;

/**
 * Util function for validating {@link DbAccessor}.
 */
final class Validators {

    private Validators() {

    }

    /**
     * Verify collection of accessors corectness.
     *
     * @param accessors collection of acessors to verify
     * @param clazz     clazz this accessors belong to
     * @throws MappingException if collection is not correct
     */
    public static void verifyAccessors(Collection<? extends DbAccessor> accessors, Class<?> clazz) throws MappingException {
        assertAccesorsNonEmpty(accessors, clazz);
        assertAccessorsOneId(accessors, clazz);
    }

    /**
     * Asserts in given accessors there is exactly one id accessor.
     *
     * @param accessors collection of accessors to test
     * @param clazz     class this acessors belong to
     * @throws MappingException if collection doesn't contain exactly one id accessor
     */
    private static void assertAccessorsOneId(Collection<? extends DbAccessor> accessors, Class<?> clazz) throws MappingException {
        int ids = 0;
        for (DbAccessor dbAccessor : accessors) {
            if (dbAccessor.isId()) {
                ++ids;
            }
        }

        if (ids != 1) {
            throw new MappingException("Class %s must have exactly one id column", clazz.getName());
        }
    }

    /**
     * Asserts collection is not empty.
     *
     * @param accessors collection of accessors to test
     * @param clazz     class this acessors belong to
     * @throws MappingException if collection is null or empty
     */
    private static void assertAccesorsNonEmpty(Collection<? extends DbAccessor> accessors, Class<?> clazz) throws MappingException {
        if (accessors == null || accessors.isEmpty()) {
            throw new MappingException("Entity of class " + clazz.getName() + " has no valid columns");
        }
    }
}
