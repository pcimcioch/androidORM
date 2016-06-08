package org.androidorm;

import org.androidorm.entities.DbEntitiesFactory;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.AndroidOrmException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class allowing managing whole schea for db - java class mapping.
 */
public class SchemaManager {

    private Map<Class<?>, DbEntity> entities = new HashMap<Class<?>, DbEntity>();

    /**
     * Constructor.
     *
     * @param classes entity classes to create schema from
     * @throws AndroidOrmException if entitie classes could not be correctly mapped
     */
    SchemaManager(Class<?>... classes) throws AndroidOrmException {
        init(classes);
    }

    /**
     * Creates schema from given classes.
     *
     * @param classes classes
     * @throws AndroidOrmException if entitie classes could not be correctly mapped
     */
    private void init(Class<?>... classes) throws AndroidOrmException {
        entities.putAll(DbEntitiesFactory.buildEntities(classes));
    }

    /**
     * Return dnEntity for given class.
     *
     * @param clazz class type
     * @return dbEntity for given class or null if given class is not entity class
     */
    public DbEntity getDbEntity(Class<?> clazz) {
        return entities.get(clazz);
    }

    /**
     * Get all dbEntities.
     *
     * @return collection of all dbEntities
     */
    public Collection<DbEntity> getDbEntities() {
        return entities.values();
    }

    /**
     * Checks if given class is entity class.
     *
     * @param clazz class type to check
     * @return if given class is entity class
     */
    public boolean isEntityClass(Class<?> clazz) {
        return entities.containsKey(clazz);
    }
}
