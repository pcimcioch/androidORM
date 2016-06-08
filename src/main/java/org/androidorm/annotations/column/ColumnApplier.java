package org.androidorm.annotations.column;

import org.androidorm.accessors.DbAccessor;
import org.androidorm.annotations.Applier;
import org.androidorm.annotations.Column;
import org.androidorm.exceptions.MappingException;
import org.androidorm.utils.StringUtils;

/**
 * Apply Column annotation to accessor.
 */
public class ColumnApplier extends Applier<DbAccessor, Column> {

    public static final int DEFAULT_LENGTH = 255;

    public static final int MAX_LENGTH = 65535;

    @Override
    public void apply(DbAccessor accessor, Column annotation) throws MappingException {
        if (annotation.length() < 1 || annotation.length() > MAX_LENGTH) {
            throw new MappingException("Column length : %d must be [1, %d]", annotation.length(), MAX_LENGTH);
        }

        String name = annotation.name();
        if (StringUtils.isNotEmpty(name)) {
            accessor.setName(name);
        }

        if (!annotation.nullable()) {
            accessor.setNullable(false);
        }
        if (annotation.unique()) {
            accessor.setUnique(annotation.unique());
        }
        if (annotation.length() != DEFAULT_LENGTH) {
            accessor.setLength(annotation.length());
        }
    }

    @Override
    public Class<Column> getAnnotationType() {
        return Column.class;
    }

}
