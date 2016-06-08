package org.androidorm.db.processors;

import org.androidorm.SchemaManager;
import org.androidorm.db.DatabaseProvider;
import org.androidorm.db.DbRepresentation;
import org.androidorm.db.PersistenceUnit;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.utils.ReflectionHelper;

/**
 * Processor for finding single entity in database.
 */
public class Finder extends FinderBase {

    private Object id;

    /**
     * Constructor.
     *
     * @param schemaManager   schema manager
     * @param persistenceUnit persistence unit
     * @param dbProvider      database provider
     */
    public Finder(SchemaManager schemaManager, PersistenceUnit persistenceUnit, DatabaseProvider dbProvider) {
        super(schemaManager, persistenceUnit, dbProvider);
    }

    /**
     * Find entity in either persistence unit or database itself.
     *
     * @param klass class of entity
     * @param idArg id of entity
     * @param <T>   type of the entity
     * @return entity or null if entity was not found
     * @throws AndroidOrmException if finding failed
     */
    public <T> T find(Class<T> klass, Object idArg) throws AndroidOrmException {
        this.id = idArg;
        this.clazz = klass;

        validateAll();
        return find();
    }

    /**
     * Validate and configure processor.
     */
    private void validateAll() {
        validateClass();
    }

    /**
     * Validate class - if class is entity class.
     */
    private void validateClass() {
        Validators.assertNotNull(id);
        dbEntity = Validators.checkDbEntity(clazz, schemaManager);
    }

    /**
     * Finds entity. First looks in persistence unit. If entity is not attached then gets it from database.
     *
     * @param <T> type of the entity
     * @return entity or null if entity was not found
     * @throws AndroidOrmException if finding failed
     */
    @SuppressWarnings("unchecked")
    private <T> T find() throws AndroidOrmException {
        DbRepresentation representation = persistenceUnit.get(dbEntity.getEntityClass(), id);
        if (representation == null) {
            representation = getRepresentationFromDatabase();
        }

        loadForeignEntities(representation);
        return representation == null ? null : (T) representation.getEntity();
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

        Object[] values = dbProvider.findInDatabase(tableName, idField, id, dbEntity.getSimpleAccessors());
        if (values == null) {
            return null;
        }

        return createRepresentationFromValues(values, ReflectionHelper.getInstance(clazz));
    }
}
