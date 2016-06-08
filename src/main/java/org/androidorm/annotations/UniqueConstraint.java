package org.androidorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Unique constraint. Used with {@link Table} annotation.
 */
@Target({})
@Retention(RUNTIME)
public @interface UniqueConstraint {

    /** An array of the column names that make up the constraint. */
    String[] columnNames();
}
