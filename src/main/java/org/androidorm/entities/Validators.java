package org.androidorm.entities;

import org.androidorm.accessors.DbAccessor;
import org.androidorm.exceptions.MappingException;
import org.androidorm.utils.ReflectionHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * Util function for validating {@link DbEntity}.
 */
final class Validators {

    private Validators() {

    }

    /**
     * Verify dbEntity correctness.
     *
     * @param dbEntity dbEntity to check
     * @throws MappingException if dbEntity is not correct
     */
    public static void verifyDbEntity(DbEntity dbEntity) throws MappingException {
        assertDbEntityConstructors(dbEntity);
        assertDbEntityNames(dbEntity);
    }

    /**
     * Checks if dbEntity doesn't have column names duplicates.
     *
     * @param dbEntity dbEntity to check
     * @throws MappingException if any name is duplicated
     */
    private static void assertDbEntityNames(DbEntity dbEntity) throws MappingException {
        Class<?> clazz = dbEntity.getEntityClass();
        Set<String> fieldNames = new HashSet<String>();

        for (DbAccessor dbAccessor : dbEntity.getSimpleAccessors()) {
            if (!fieldNames.add(dbAccessor.getName())) {
                throw new MappingException("Class %s has column duplicate: %s", clazz.getName(), dbAccessor.getName());
            }
        }
    }

    /**
     * Checks if dbEntity has correct constructors.
     *
     * @param dbEntity dbEntity to check
     * @throws MappingException if entity has no default constructor
     */
    private static void assertDbEntityConstructors(DbEntity dbEntity) throws MappingException {
        Class<?> clazz = dbEntity.getEntityClass();
        if (!ReflectionHelper.hasDefaultConstructor(clazz)) {
            throw new MappingException("Class %s has no default constructor", clazz.getName());
        }
    }
}
