package org.androidorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Mark the field or property that is enum, to be mapped as database column.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Enumerated {

    /**
     * The type used in mapping an enum type.
     */
    EnumType value() default EnumType.ORDINAL;
}
