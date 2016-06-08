package org.androidorm.annotations.entity;

import org.androidorm.annotations.Applier;
import org.androidorm.annotations.Table;
import org.androidorm.annotations.UniqueConstraint;
import org.androidorm.entities.DbEntity;
import org.androidorm.exceptions.MappingException;

/**
 * Apply Table annotation to dbEntity.
 */
public class TableApplier extends Applier<DbEntity, Table> {

    @Override
    public void apply(DbEntity dbEntity, Table annotation) throws MappingException {
        dbEntity.setTableName(annotation.name());

        UniqueConstraint[] uniqueConstraints = annotation.uniqueConstraints();
        if (uniqueConstraints.length > 0) {
            String[][] uniqueConstraintsStrings = new String[uniqueConstraints.length][];
            for (int i = 0; i < uniqueConstraints.length; ++i) {
                uniqueConstraintsStrings[i] = uniqueConstraints[i].columnNames();
            }
            dbEntity.setUniqueConstraints(uniqueConstraintsStrings);
        }
    }

    @Override
    public Class<Table> getAnnotationType() {
        return Table.class;
    }
}