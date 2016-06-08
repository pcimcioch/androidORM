package org.androidorm.db;

import org.androidorm.accessors.DbForeignEntityAcessor;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.EntityStateException;
import org.androidorm.exceptions.ReflectionException;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Container to keep attached entities and their state.
 */
public class PersistenceUnit {

    private Map<Object, DbRepresentation> attachedEntities = new IdentityHashMap<Object, DbRepresentation>();

    /**
     * Constructor. Create copy of given persistence unit.
     *
     * @param origin original object to copy
     */
    public PersistenceUnit(PersistenceUnit origin) {
        setState(origin);
    }

    public PersistenceUnit() {

    }

    /**
     * Set this persistence unit state to given one.
     *
     * @param origin original persistence unit to copy form
     */
    public void setState(PersistenceUnit origin) {
        clear();
        for (Entry<Object, DbRepresentation> entry : origin.attachedEntities.entrySet()) {
            this.attachedEntities.put(entry.getKey(), new DbRepresentation(entry.getValue()));
        }
    }

    /**
     * Checks if unit contains entity.
     *
     * @param entity entity to check
     * @return if unit contains given entity
     */
    public boolean containsEntity(Object entity) {
        return attachedEntities.containsKey(entity);
    }

    /**
     * Adds entity and its representation to unit. Throws exception if entity is already in unit.
     *
     * @param entity         entity to add
     * @param representation entity representation
     * @throws EntityStateException if entity is already in persistence unit
     */
    public void put(Object entity, DbRepresentation representation) throws EntityStateException {
        if (attachedEntities.containsKey(entity)) {
            throw new EntityStateException("Persistence Unit already contains this entity");
        }
        attachedEntities.put(entity, representation);
    }

    /**
     * Updates entity representation / adds new entity representation.
     *
     * @param entity         entity
     * @param representation entity representation
     */
    public void update(Object entity, DbRepresentation representation) {
        attachedEntities.put(entity, representation);
    }

    /**
     * Removes entity from unit.
     *
     * @param entity entity to remove
     * @return removed entity representation or null if entity was not in this unit
     */
    public Object remove(Object entity) {
        return attachedEntities.remove(entity);
    }

    /**
     * Return representation of given entity.
     *
     * @param entity entity
     * @return representation
     */
    public DbRepresentation get(Object entity) {
        return attachedEntities.get(entity);
    }

    /**
     * Return representation for entity of given class and id.
     *
     * @param clazz class
     * @param id    the id
     * @return representation or null if representation was not found
     */
    public DbRepresentation get(Class<?> clazz, Object id) {
        if (id == null) {
            return null;
        }

        for (DbRepresentation representation : attachedEntities.values()) {
            Object entity = representation.getEntity();
            if (clazz.isInstance(entity) && id.equals(representation.getDbIdValue())) {
                return representation;
            }
        }

        return null;
    }

    /**
     * Checks if all entity's foreign entities are attached.
     *
     * @param entity   entity to check
     * @param dbEntity enity's dbEntity
     * @throws EntityStateException if entity is detached
     * @throws ReflectionException  if reflective operation failed
     */
    public void checkRelations(Object entity, DbEntity dbEntity) throws EntityStateException, ReflectionException {
        for (DbForeignEntityAcessor foreignEntityAccessor : dbEntity.getToOneForeignAccessors()) {
            Object foreignObject = foreignEntityAccessor.extractForeignEntity(entity);
            if (foreignObject == null) {
                continue;
            }

            if (!containsEntity(foreignObject)) {
                throw new EntityStateException("Foreign object %s of class %s is detached. Persist it first", foreignEntityAccessor.getName(), entity.getClass());
            }
        }
    }

    /**
     * Clear whole persistence unit.
     */
    public void clear() {
        this.attachedEntities.clear();
    }
}
