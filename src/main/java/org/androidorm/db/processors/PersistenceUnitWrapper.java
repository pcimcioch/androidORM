package org.androidorm.db.processors;

import org.androidorm.SchemaManager;
import org.androidorm.db.PersistenceUnit;

/**
 * Simple wrapper for {@link PersistenceUnit} methods that validates arguments.
 */
public class PersistenceUnitWrapper {

    private final SchemaManager schemaManager;

    private final PersistenceUnit persistenceUnit;

    /**
     * Constructor.
     *
     * @param schemaManager   schema manager
     * @param persistenceUnit persistence unit
     */
    public PersistenceUnitWrapper(SchemaManager schemaManager, PersistenceUnit persistenceUnit) {
        this.schemaManager = schemaManager;
        this.persistenceUnit = persistenceUnit;
    }

    /**
     * Detach entity.
     *
     * @param entity entity to detach
     */
    public void detach(Object entity) {
        Validators.checkDbEntity(entity.getClass(), schemaManager);
        persistenceUnit.remove(entity);
    }

    /**
     * Checks if persistence unit contains entity (if entity is attached).
     *
     * @param entity entity
     * @return if entity is attached
     */
    public boolean contains(Object entity) {
        Validators.checkDbEntity(entity.getClass(), schemaManager);
        return persistenceUnit.containsEntity(entity);
    }

}
