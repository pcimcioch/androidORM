package org.androidorm.annotations.column;

import org.androidorm.accessors.DbAccessor;
import org.androidorm.accessors.DbEnumFieldAccessor;
import org.androidorm.annotations.Applier;
import org.androidorm.annotations.Enumerated;

/**
 * Apply Enumerated annotation to accessor.
 */
public class EnumeratedApplier extends Applier<DbAccessor, Enumerated> {

    @Override
    public void apply(DbAccessor accessor, Enumerated annotation) {
        if (!(accessor instanceof DbEnumFieldAccessor)) {
            return;
        }

        ((DbEnumFieldAccessor) accessor).setEnumRepresentationType(annotation.value());
    }

    @Override
    public Class<Enumerated> getAnnotationType() {
        return Enumerated.class;
    }
}
