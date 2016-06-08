package org.androidorm.annotations.column;

import org.androidorm.accessors.DbAccessor;
import org.androidorm.annotations.Applier;
import org.androidorm.annotations.Id;

/**
 * Apply Id annotation to accessor.
 */
public class IdApplier extends Applier<DbAccessor, Id> {

    @Override
    protected void apply(DbAccessor accessor, Id annotation) {
        accessor.setId(true);
        accessor.setUnique(true);
        accessor.setNullable(false);
    }

    @Override
    public Class<Id> getAnnotationType() {
        return Id.class;
    }
}
