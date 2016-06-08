package org.androidorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mark class as entity, that should be mapped to database table.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {

    /**
     * The name of the table. Defaults to the class name.
     */
    String name() default "";

    /**
     * Unique constraints that are to be placed on the table. These are only used if table generation is in effect.
     */
    UniqueConstraint[] uniqueConstraints() default {};
}