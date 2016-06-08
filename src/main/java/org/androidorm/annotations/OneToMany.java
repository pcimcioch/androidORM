package org.androidorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mark the properties that are collection of foreign keys to other entities in one to many relation. Property must be {@link java.util.Set} or {@link java.util.List} of objects of
 * entity classes
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface OneToMany {

    /**
     * The field that owns the relationship. Required unless the relationship is unidirectional.
     */
    String mappedBy() default "";
}
