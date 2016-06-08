package org.androidorm.annotations;

/**
 * Type indicating how enum property should be presented in database.
 */
public enum EnumType {
    /** Persist enumerated type property or field as an integer. */
    ORDINAL,

    /** Persist enumerated type property or field as a string. */
    STRING
}
