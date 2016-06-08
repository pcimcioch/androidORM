package org.androidorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mark the field or property to be mapped to table column.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Column {

    /**
     * Default column length.
     */
    int DEFAULT_LENGTH = 255;

    /**
     * The name of the column. Defaults to the property or field name.
     */
    String name() default "";

    /**
     * Whether the property is a unique key.
     */
    boolean unique() default false;

    /**
     * Whether the database column is nullable.
     */
    boolean nullable() default true;

    /**
     * The column length. (Applies only if a string-valued column is used.)
     */
    int length() default DEFAULT_LENGTH;
}
