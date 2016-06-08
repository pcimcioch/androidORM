package org.androidorm.db.processors;

import org.androidorm.SchemaManager;
import org.androidorm.db.DatabaseProvider;
import org.androidorm.db.DbRepresentation;
import org.androidorm.db.PersistenceUnit;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.utils.ReflectionHelper;
import org.androidorm.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor for finding collection of entities.
 */
public class AllFinder extends FinderBase {

    private List<DbRepresentation> newRepresentations = new ArrayList<DbRepresentation>();

    /**
     * Constructor.
     *
     * @param schemaManager   schema manager
     * @param persistenceUnit persistence unit
     * @param dbProvider      database provider
     */
    public AllFinder(SchemaManager schemaManager, PersistenceUnit persistenceUnit, DatabaseProvider dbProvider) {
        super(schemaManager, persistenceUnit, dbProvider);
    }

    /**
     * Find collection of entities.
     *
     * @param klass     entities class
     * @param sql       sql appendix query
     * @param arguments arguments for sql query
     * @param <T>       type of the entity
     * @return list of found entities
     * @throws AndroidOrmException if finding failed
     */
    public <T> List<T> findAll(Class<T> klass, String sql, Object[] arguments) throws AndroidOrmException {
        this.clazz = klass;

        validateAll();
        return findAll(sql, arguments);
    }

    /**
     * Validate and configure processor.
     */
    private void validateAll() {
        validateClassAndState();
    }

    /**
     * Validate class - if class is entity class.
     */
    private void validateClassAndState() {
        dbEntity = Validators.checkDbEntity(clazz, schemaManager);
    }

    /**
     * Find collection of entities.
     *
     * @param sql       sql appendix query
     * @param arguments arguments for sql query
     * @param <T>       type of the entity
     * @return list of found entities
     * @throws AndroidOrmException if finding failed
     */
    private <T> List<T> findAll(String sql, Object[] arguments) throws AndroidOrmException {
        String[] stringArgs = StringUtils.toStringArray(arguments);
        List<DbRepresentation> list = getRepresentationFromDatabase(sql, stringArgs);
        loadForeignEntities();
        return createEntityList(list);
    }

    /**
     * Get representations of searched objects. Some of them may be already in persistence unit - some may be obtained from database itself.
     *
     * @param sql  sql appendix query
     * @param args arguments for sql query
     * @return list of representations of searched entities
     * @throws AndroidOrmException if finding failed
     */
    private List<DbRepresentation> getRepresentationFromDatabase(String sql, String[] args) throws AndroidOrmException {
        String tableName = dbEntity.getTableName();

        List<Object[]> valuesList = dbProvider.doFindAll(sql, args, tableName, dbEntity.getSimpleAccessors());
        return createRepresentationsFromValuesList(valuesList);
    }

    /**
     * Create or get (if in persistence unit) representations from raw records values obtained from database.
     *
     * @param valuesList records values
     * @return list of representations
     * @throws AndroidOrmException if creating failed
     */
    private List<DbRepresentation> createRepresentationsFromValuesList(List<Object[]> valuesList) throws AndroidOrmException {
        List<DbRepresentation> representations = new ArrayList<DbRepresentation>(valuesList.size());
        newRepresentations.clear();

        for (Object[] values : valuesList) {
            DbRepresentation representation = findInPersistenceUnit(values);
            if (representation == null) {
                representation = createRepresentationFromValues(values, ReflectionHelper.getInstance(clazz));
                newRepresentations.add(representation);
            }
            representations.add(representation);
        }

        return representations;
    }

    /**
     * Find representation in persistence unit.
     *
     * @param values values that represents representation. Must be in the same order as simple accessors in representation dbEntity
     * @return representation or null if entity is not in persistence unit
     */
    private DbRepresentation findInPersistenceUnit(Object[] values) {
        int idPos = dbEntity.getIdPosition();
        Object idValue = values[idPos];
        return persistenceUnit.get(clazz, idValue);
    }

    /**
     * Load foreign entities for newly obtained representations (the one obtained from database, not persistence unit).
     *
     * @throws AndroidOrmException if loading failed
     */
    private void loadForeignEntities() throws AndroidOrmException {
        for (DbRepresentation elem : newRepresentations) {
            loadForeignEntities(elem);
        }
    }

    /**
     * Create list of entities from list of entity representations.
     *
     * @param representations representations list
     * @param <T>             type of the entity
     * @return list of entities
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> createEntityList(List<DbRepresentation> representations) {
        List<T> result = new ArrayList<T>(representations.size());
        for (DbRepresentation rep : representations) {
            result.add((T) rep.getEntity());
        }
        return result;
    }
}
