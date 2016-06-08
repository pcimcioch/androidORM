package org.androidorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mark the properties that are foreign keys to other entities in many to one relation. Property must be of entity class type.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ManyToOne {

}
