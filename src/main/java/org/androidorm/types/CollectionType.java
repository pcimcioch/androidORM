package org.androidorm.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Type of collection.
 */
public enum CollectionType {

    /** {@link java.util.Set}. */
    SET(Set.class) {
        @Override
        public <T> Collection<T> createCollection(Collection<T> values) {
            return new HashSet<T>(values);
        }
    },

    /** {@link java.util.List}. */
    LIST(List.class) {
        @Override
        public <T> Collection<T> createCollection(Collection<T> values) {
            return new ArrayList<T>(values);
        }
    };

    private final Class<?> type;

    /**
     * Constructor. Actual java collection type
     *
     * @param type java collection type
     */
    CollectionType(Class<?> type) {
        this.type = type;
    }

    /**
     * Returns {@link CollectionType} based on class type.
     *
     * @param type class type
     * @return java collection type
     */
    public static CollectionType getCollectionType(Class<?> type) {
        for (CollectionType collectionType : CollectionType.values()) {
            if (type.equals(collectionType.getType())) {
                return collectionType;
            }
        }

        return null;
    }

    /**
     * Return actual java collection type. For example: {@link java.util.Set}, {@link java.util.List}.
     *
     * @return java collection type
     */
    private Class<?> getType() {
        return type;
    }

    /**
     * Create instance of this collection type and populates it with values.
     *
     * @param values values to insert into newly created collection
     * @param <T>    type of the collection elements
     * @return collection
     */
    public abstract <T> Collection<T> createCollection(Collection<T> values);
}
