package org.androidorm;

import org.androidorm.db.DatabaseConnectionProvider;
import org.androidorm.db.DatabaseProvider;
import org.androidorm.db.PersistenceUnit;
import org.androidorm.db.processors.AllFinder;
import org.androidorm.db.processors.Finder;
import org.androidorm.db.processors.PersistenceUnitWrapper;
import org.androidorm.db.processors.Persister;
import org.androidorm.db.processors.Refresher;
import org.androidorm.db.processors.Remover;
import org.androidorm.db.processors.Updater;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.TransactionException;

import java.util.List;

/**
 * Class for managing entity objects.
 */
public class EntityManager {

    private final DatabaseProvider dbProvider;

    private final SchemaManager schemaManager;

    private final PersistenceUnit persistenceUnit = new PersistenceUnit();

    private final DatabaseConnectionProvider databaseConnectionProvider;

    private final Transaction transaction;

    /**
     * Constructor.
     *
     * @param dbName        databse name
     * @param schemaManager schema manager
     */
    protected EntityManager(String dbName, SchemaManager schemaManager) {
        this.databaseConnectionProvider = new DatabaseConnectionProvider(dbName);
        this.dbProvider = new DatabaseProvider(databaseConnectionProvider);
        this.transaction = new Transaction(persistenceUnit, databaseConnectionProvider);
        this.schemaManager = schemaManager;
    }

    /**
     * Open entity manager. Must be called in pair with {@link #close()}.
     *
     * @throws TransactionException if can't open database
     */
    public void open() throws TransactionException {
        databaseConnectionProvider.openDatabase();
        persistenceUnit.clear();
    }

    /**
     * Closes entity manager. Must be called in pair with {@link #open()}.
     *
     * @throws TransactionException if database can't be closed
     */
    public void close() throws TransactionException {
        databaseConnectionProvider.closeDatabase();
    }

    /**
     * Return if entityManager is opened.
     *
     * @return if entity manager is opened
     */
    public boolean isOpened() {
        return databaseConnectionProvider.isOpened();
    }

    /**
     * Persist object into database.
     *
     * @param entity entity to persist
     * @throws AndroidOrmException if object is already attached or couldn't be persisted
     */
    public void persist(Object entity) throws AndroidOrmException {
        new Persister(schemaManager, persistenceUnit, dbProvider).persist(entity);
    }

    /**
     * Updated object in database.
     *
     * @param entity entity to update
     * @throws AndroidOrmException if object is not attached or couldn't be updated
     */
    public void update(Object entity) throws AndroidOrmException {
        new Updater(persistenceUnit, dbProvider).update(entity);
    }

    /**
     * Finds object in database.
     *
     * @param clazz entity class
     * @param id    entity id
     * @param <T>   type of the entity
     * @return attached entity or null if object can't be find
     * @throws AndroidOrmException if entity cannot be obtained
     */
    public <T> T find(Class<T> clazz, Object id) throws AndroidOrmException {
        return new Finder(schemaManager, persistenceUnit, dbProvider).find(clazz, id);
    }

    /**
     * Finds all entities of given class.
     *
     * @param clazz entity class
     * @param <T>   type of the entity
     * @return list of all entities
     * @throws AndroidOrmException if list cannot be obtained
     */
    public <T> List<T> findAll(Class<T> clazz) throws AndroidOrmException {
        return findAll(clazz, null, null);
    }

    /**
     * Refresh entity. Set its state to database.
     *
     * @param entity wntity to refresh
     * @throws AndroidOrmException if entity cannot be refreshed
     */
    public void refresh(Object entity) throws AndroidOrmException {
        new Refresher(schemaManager, persistenceUnit, dbProvider).refresh(entity);
    }

    /**
     * Finds all entities of given class using more complicated sql query. For example:
     * <p>
     * <code>findAll(MyClass.class, "as MyTable where MyTable.field1 >=?", new Object[]{4})</code>
     *
     * @param clazz     entity class
     * @param sql       sql
     * @param arguments array of arguments to substitute "?" in sql
     * @param <T>       type of the entity
     * @return list of all entities
     * @throws AndroidOrmException if list cannot be obtained
     */
    public <T> List<T> findAll(Class<T> clazz, String sql, Object[] arguments) throws AndroidOrmException {
        return new AllFinder(schemaManager, persistenceUnit, dbProvider).findAll(clazz, sql, arguments);
    }

    /**
     * Finds all entities of given class using more complicated sql query. For example:
     * <p>
     * <br> <br> <code> findAll(MyClass.class, <br> "as T4 join MyAnotherClass as T2 on T2.field1=T4.field3 where T2.field2 in <br> (select field2 from MyYetAnotherClass where
     * field2 like \'Field%\')") <br> </code>
     *
     * @param clazz entity class
     * @param sql   sql
     * @param <T>   type of the entity
     * @return list of all entities
     * @throws AndroidOrmException if list cannot be obtained
     */
    public <T> List<T> findAll(Class<T> clazz, String sql) throws AndroidOrmException {
        return findAll(clazz, sql, null);
    }

    /**
     * Detach entity. Detached entity can't be removed or updated.
     *
     * @param entity entity to detach
     */
    public void detach(Object entity) {
        new PersistenceUnitWrapper(schemaManager, persistenceUnit).detach(entity);
    }

    /**
     * Checks if given entity is attached.
     *
     * @param entity entity to check
     * @return if entity is attached
     */
    public boolean contains(Object entity) {
        return new PersistenceUnitWrapper(schemaManager, persistenceUnit).contains(entity);
    }

    /**
     * Remove entity from database.
     *
     * @param entity entity to remove
     * @throws AndroidOrmException if entity is not attached or if it cannot be removed
     */
    public void remove(Object entity) throws AndroidOrmException {
        new Remover(schemaManager, persistenceUnit, dbProvider).remove(entity);
    }

    /**
     * Return transaction object.
     *
     * @return transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }
}
