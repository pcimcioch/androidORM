package org.androidorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicating that marked property is transient, and shouldn't be mapped.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Transient {

}
