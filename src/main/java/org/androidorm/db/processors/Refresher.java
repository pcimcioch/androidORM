package org.androidorm.db.processors;

import org.androidorm.SchemaManager;
import org.androidorm.db.DatabaseProvider;
import org.androidorm.db.DbRepresentation;
import org.androidorm.db.PersistenceUnit;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.DatabaseStateException;
import org.androidorm.exceptions.EntityStateException;

/**
 * Processor for refreshing entity.
 */
public class Refresher extends FinderBase {

    private Object entity;

    private DbRepresentation currentRepresentation;

    /**
     * Constructor.
     *
     * @param schemaManager   schema manager
     * @param persistenceUnit persistence unit
     * @param dbProvider      database provider
     */
    public Refresher(SchemaManager schemaManager, PersistenceUnit persistenceUnit, DatabaseProvider dbProvider) {
        super(schemaManager, persistenceUnit, dbProvider);
    }

    /**
     * Refresh entity. Set its state to database
     *
     * @param entityArg entity to refresh
     * @throws AndroidOrmException if refresh failed
     */
    public void refresh(Object entityArg) throws AndroidOrmException {
        this.entity = entityArg;
        validateAll();
        refresh();
    }

    /**
     * Validate and configure processor.
     *
     * @throws AndroidOrmException if validation or configuration failed
     */
    private void validateAll() throws AndroidOrmException {
        validateClassAndState();
    }

    /**
     * Validate entity class - if entity is entity class; and state - if entity is attached.
     *
     * @throws EntityStateException if validation failed
     */
    private void validateClassAndState() throws EntityStateException {
        Validators.assertNotNull(entity);
        currentRepresentation = Validators.checkAttached(entity, persistenceUnit);
        dbEntity = currentRepresentation.getDbEntity();
    }

    /**
     * Refresh state.
     *
     * @throws AndroidOrmException if refresh failed
     */
    private void refresh() throws AndroidOrmException {
        DbRepresentation representation = getRepresentationFromDatabase();
        if (representation == null) {
            throw new DatabaseStateException("Entity %s [id=%s] not found", dbEntity.getEntityClass(), currentRepresentation.getDbIdValue());
        }

        loadForeignEntities(representation);
    }

    /**
     * Get representation of entity from database.
     *
     * @return representation or null if entity was not found in database
     * @throws AndroidOrmException if finding failed
     */
    private DbRepresentation getRepresentationFromDatabase() throws AndroidOrmException {
        String tableName = dbEntity.getTableName();
        String idField = dbEntity.getIdAccessor().getName();

        Object[] values = dbProvider.findInDatabase(tableName, idField, currentRepresentation.getDbIdValue(), dbEntity.getSimpleAccessors());
        if (values == null) {
            return null;
        }

        persistenceUnit.remove(entity);
        return createRepresentationFromValues(values, entity);
    }
}
