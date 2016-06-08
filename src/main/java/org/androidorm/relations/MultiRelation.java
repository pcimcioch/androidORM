package org.androidorm.relations;

import org.androidorm.accessors.DbForeignEntityAcessor;
import org.androidorm.exceptions.ReflectionException;
import org.androidorm.mutator.IMutator;
import org.androidorm.types.CollectionType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Class representing foreign relation seen from "many" side.
 */
public class MultiRelation {

    private DbForeignEntityAcessor inverseAccessor;

    protected IMutator mutator;

    /**
     * Constructor.
     *
     * @param inverseAccessor accessor that belongs to foreign class of this relation, pointing to this multi relations. owner
     * @param mutator         mutator to access collection of foreign objects
     */
    public MultiRelation(DbForeignEntityAcessor inverseAccessor, IMutator mutator) {
        this.inverseAccessor = inverseAccessor;
        this.mutator = mutator;
    }

    /**
     * Get accessor that belongs to foreign class of this relation, pointing to this multi relations owner.
     *
     * @return accessor
     */
    public DbForeignEntityAcessor getInverseAccessor() {
        return inverseAccessor;
    }

    /**
     * Get mutator to modify collection of foreign objects.
     *
     * @return mutator
     */
    public IMutator getMutator() {
        return mutator;
    }

    /**
     * Get class of foreign object.
     *
     * @return class of foreign object
     */
    public Class<?> getType() {
        return mutator.getCollectionParameterizedType();
    }

    /**
     * Set given collection of objects as collection of foreign objects.
     *
     * @param entity         entity to which collection should be inserted
     * @param foreignObjects collection of foreign objects to set
     * @throws ReflectionException if collection can't be set
     */
    public void setRelationObjects(Object entity, List<?> foreignObjects) throws ReflectionException {
        try {
            CollectionType collectionType = mutator.getCollectionType();
            Object collection = collectionType.createCollection(foreignObjects);
            mutator.set(entity, collection);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Can't set collection in %s %s", e, entity.getClass(), e.getMessage());
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Can't set collection in %s %s", e, entity.getClass(), e.getMessage());
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Can't set collection in %s %s", e, entity.getClass(), e.getMessage());
        }
    }
}
