package org.androidorm.schema;

import org.androidorm.SchemaManager;
import org.androidorm.entities.DbEntity;

/**
 * Manager for generating SQLite script creating database.
 */
public class SqlSchemaManager {

    private final SchemaManager schemaManager;

    /**
     * Constructs from schema manager.
     *
     * @param schemaManager schema manager
     */
    public SqlSchemaManager(SchemaManager schemaManager) {
        this.schemaManager = schemaManager;
    }

    /**
     * Get table script for given entity class.
     *
     * @param clazz class of the entity
     * @return table sql script
     */
    public String getTableSchemaSQL(Class<?> clazz) {
        DbEntity dbEntity = schemaManager.getDbEntity(clazz);
        if (dbEntity == null) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not registered entity class");
        }

        return TableGenerator.getTableDescriptor(dbEntity);
    }

    /**
     * Get script for all entities.
     *
     * @return script for creating all database
     */
    public String getDatabaseSchemaSQL() {
        return TableGenerator.getTableDescriptors(schemaManager.getDbEntities());
    }
}
