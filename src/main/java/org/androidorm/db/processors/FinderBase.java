package org.androidorm.db.processors;

import org.androidorm.SchemaManager;
import org.androidorm.accessors.DbForeignEntityAcessor;
import org.androidorm.db.DatabaseProvider;
import org.androidorm.db.DbRepresentation;
import org.androidorm.db.PersistenceUnit;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.AndroidOrmException;
import org.androidorm.exceptions.DatabaseStateException;
import org.androidorm.relations.MultiRelation;

import java.util.List;

/**
 * Base for processors for finding entities.
 */
abstract class FinderBase {

    protected final SchemaManager schemaManager;

    protected final PersistenceUnit persistenceUnit;

    protected final DatabaseProvider dbProvider;

    protected Class<?> clazz;

    protected DbEntity dbEntity;

    /**
     * Constructor.
     *
     * @param schemaManager   schema manager
     * @param persistenceUnit persistence unit
     * @param dbProvider      database provider
     */
    protected FinderBase(SchemaManager schemaManager, PersistenceUnit persistenceUnit, DatabaseProvider dbProvider) {
        this.schemaManager = schemaManager;
        this.persistenceUnit = persistenceUnit;
        this.dbProvider = dbProvider;
    }

    /**
     * Create {@link DbRepresentation} from given values. Values should be in the same order as simple acessors in representiaion's dbEntity. Add entity and representation to
     * persistence unit.
     *
     * @param values values from which representation will be created
     * @param entity entity which state should be set
     * @return created representation
     * @throws AndroidOrmException if creating failed
     */
    protected DbRepresentation createRepresentationFromValues(Object[] values, Object entity) throws AndroidOrmException {
        DbRepresentation representation = new DbRepresentation(entity, dbEntity);
        representation.createFromValues(values);

        persistenceUnit.put(entity, representation);
        return representation;
    }

    /**
     * Load foreign entities for given representation.
     *
     * @param representation representation
     * @throws AndroidOrmException if loading failed
     */
    protected void loadForeignEntities(DbRepresentation representation) throws AndroidOrmException {
        if (representation == null) {
            return;
        }

        loadToOneRelations(representation);
        loadOneToRelations(representation);
    }

    /**
     * Load foreign entities from 'ToOne' relations.
     *
     * @param representation representation
     * @throws AndroidOrmException if loading failed
     */
    private void loadToOneRelations(DbRepresentation representation) throws AndroidOrmException {
        for (DbForeignEntityAcessor foreignAccessor : dbEntity.getToOneForeignAccessors()) {
            Object foreignEntityId = representation.getExternalAccessorValue(foreignAccessor);
            Class<?> foreignEntityClass = foreignAccessor.getRealType();

            Object rel = getForeignEntity(foreignEntityId, foreignEntityClass);
            foreignAccessor.setForeignEntity(representation.getEntity(), rel);
        }
    }

    /**
     * Return foreign entity of given class and id. First searches in persistence unit, if not found, then gets from database.
     *
     * @param foreignEntityId    id value of searched entity
     * @param foreignEntityClass class of searched entity
     * @return entity
     * @throws AndroidOrmException if getting failed
     */
    private Object getForeignEntity(Object foreignEntityId, Class<?> foreignEntityClass) throws AndroidOrmException {
        if (foreignEntityId == null) {
            return null;
        }

        Object rel = new Finder(schemaManager, persistenceUnit, dbProvider).find(foreignEntityClass, foreignEntityId);
        if (rel == null) {
            throw new DatabaseStateException("Can't load related instance of %s with [id=%s]", foreignEntityClass, foreignEntityId);
        }

        return rel;
    }

    /**
     * Load foreign entities from 'OneTo' relations.
     *
     * @param representation representation
     * @throws AndroidOrmException if loading failed
     */
    private void loadOneToRelations(DbRepresentation representation) throws AndroidOrmException {
        Object foreignEntityId = representation.getDbIdValue();
        for (MultiRelation foreignAccessor : dbEntity.getMultiRelation()) {
            DbForeignEntityAcessor inverseAccessor = foreignAccessor.getInverseAccessor();
            String idFieldName = inverseAccessor.getName();
            Class<?> foreignEntityClass = foreignAccessor.getType();

            List<?> rel = getForeignEntities(foreignEntityId, idFieldName, foreignEntityClass);
            foreignAccessor.setRelationObjects(representation.getEntity(), rel);
        }
    }

    /**
     * Return all foreign entities which foreign id field equals to foreign entity id.
     *
     * @param foreignEntityId    id of foreign entity
     * @param foreignIdFieldName id field name
     * @param foreignClass       class of entities to load
     * @param <T>                type of the foreign entity
     * @return list of entities
     * @throws AndroidOrmException if getting failed
     */
    private <T> List<T> getForeignEntities(Object foreignEntityId, String foreignIdFieldName, Class<T> foreignClass) throws AndroidOrmException {
        String whereSql = "where " + foreignIdFieldName + "=?";
        Object[] arguments = new Object[]{foreignEntityId};
        return new AllFinder(schemaManager, persistenceUnit, dbProvider).findAll(foreignClass, whereSql, arguments);
    }
}
