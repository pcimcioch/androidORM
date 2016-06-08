package org.androidorm.annotations;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicating that property is identifier. Each entity should have one, and exactly one property marked with this annotation. Must apply to numeric type
 */
@Retention(RUNTIME)
public @interface Id {

}
