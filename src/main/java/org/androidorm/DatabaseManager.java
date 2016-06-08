package org.androidorm;

import android.database.sqlite.SQLiteDatabase;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.schema.SqlSchemaManager;

/**
 * main database and ORM manager.
 */
public class DatabaseManager {

    private String dbName;

    private SchemaManager schemaManager;

    private SqlSchemaManager sqlSchemaManager;

    /**
     * Constructor. Creates manager from given entity classes.
     *
     * @param dbName  database name
     * @param classes entity classes that will be mapped to database
     * @throws AndroidOrmException if anything went wrong
     */
    public DatabaseManager(String dbName, Class<?>... classes) throws AndroidOrmException {
        this.schemaManager = new SchemaManager(classes);
        this.sqlSchemaManager = new SqlSchemaManager(schemaManager);
        this.dbName = dbName;
    }

    /**
     * Get manager to manage entity objects.
     *
     * @return manager
     */
    public EntityManager getEntityManager() {
        return new EntityManager(dbName, schemaManager);
    }

    /**
     * Get manager to manage database sql schema.
     *
     * @return manager
     */
    public SqlSchemaManager getSqlSchemaManager() {
        return sqlSchemaManager;
    }

    /**
     * Create database schema.
     */
    public void createSchema() {
        SQLiteDatabase sqlDb = SQLiteDatabase.openDatabase(dbName, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            for (String query : sqlSchemaManager.getDatabaseSchemaSQL().split("\\n")) {
                sqlDb.execSQL(query);
            }
        } finally {
            sqlDb.close();
        }
    }
}
