package org.androidorm.db.processors;

import org.androidorm.SchemaManager;
import org.androidorm.db.DbRepresentation;
import org.androidorm.db.PersistenceUnit;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.EntityStateException;

/**
 * Util function for validating various parameters.
 */
final class Validators {

    private Validators() {
    }

    /**
     * Looks for dbEntity for given class in schema manager or throws exception if class represents no dbEntity.
     *
     * @param clazz         class representing entity
     * @param schemaManager schema manager to look in
     * @return dbEntity
     */
    public static DbEntity checkDbEntity(Class<?> clazz, SchemaManager schemaManager) {
        DbEntity dbEntity = schemaManager.getDbEntity(clazz);
        if (dbEntity == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not registered entity class");
        }

        return dbEntity;
    }

    /**
     * Looks for dbRepresentation of entity in persistence unit or throws exception if entity is not attached.
     *
     * @param entity          entity
     * @param persistenceUnit persistence unit
     * @return dbRepresentation of entity in persistenceUnit
     * @throws EntityStateException if entity is detached
     */
    public static DbRepresentation checkAttached(Object entity, PersistenceUnit persistenceUnit) throws EntityStateException {
        DbRepresentation representation = persistenceUnit.get(entity);
        if (representation == null) {
            throw new EntityStateException("This entity is detached");
        }

        return representation;
    }

    /**
     * Checks if object is not null.
     *
     * @param arg object to check
     */
    public static void assertNotNull(Object arg) {
        if (arg == null) {
            throw new NullPointerException();
        }
    }

    /**
     * Checks if entity is detached from persistenceUnit. Throws exception if so, or does nothing if entity is attached.
     *
     * @param entity          entity to check
     * @param persistenceUnit persistence unit
     * @throws EntityStateException if entity is already attached
     */
    public static void assertDetached(Object entity, PersistenceUnit persistenceUnit) throws EntityStateException {
        if (persistenceUnit.containsEntity(entity)) {
            throw new EntityStateException("This entity is already attached. Use update instead");
        }
    }
}
