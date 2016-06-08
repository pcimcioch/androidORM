package org.androidorm.db.processors;

import org.androidorm.SchemaManager;
import org.androidorm.accessors.DbAccessor;
import org.androidorm.db.DatabaseProvider;
import org.androidorm.db.PersistenceUnit;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.EntityStateException;

/**
 * Processor for removing entity from database.
 */
public class Remover {

    private SchemaManager schemaManager;

    private final PersistenceUnit persistenceUnit;

    private final DatabaseProvider dbProvider;

    private Object entity;

    private DbEntity dbEntity;

    /**
     * Constructor.
     *
     * @param schemaManager   schema manager
     * @param persistenceUnit persistence unit
     * @param dbProvider      database provider
     */
    public Remover(SchemaManager schemaManager, PersistenceUnit persistenceUnit, DatabaseProvider dbProvider) {
        this.schemaManager = schemaManager;
        this.persistenceUnit = persistenceUnit;
        this.dbProvider = dbProvider;
    }

    /**
     * Remove entity from database.
     *
     * @param entityArg entity to remove
     * @throws AndroidOrmException if removing failed
     */
    public void remove(Object entityArg) throws AndroidOrmException {
        this.entity = entityArg;
        validateAll();
        remove();
    }

    /**
     * Validate and configure entity.
     *
     * @throws EntityStateException if validation failed
     */
    private void validateAll() throws EntityStateException {
        validateClassAndState();
    }

    /**
     * Validate entity class - if entity is entity class; and state - if entity is attached.
     *
     * @throws EntityStateException if validation failed
     */
    private void validateClassAndState() throws EntityStateException {
        Validators.assertNotNull(entity);
        dbEntity = Validators.checkDbEntity(entity.getClass(), schemaManager);
        Validators.checkAttached(entity, persistenceUnit);
    }

    /**
     * Removes entity from database and persistence unit.
     *
     * @throws AndroidOrmException if removing failed
     */
    private void remove() throws AndroidOrmException {
        String tableName = dbEntity.getTableName();
        DbAccessor idAccessor = dbEntity.getIdAccessor();
        String idField = idAccessor.getName();
        Object id = idAccessor.extractValue(entity);

        dbProvider.removeFromDb(tableName, idField, id);
        persistenceUnit.remove(entity);
    }
}
