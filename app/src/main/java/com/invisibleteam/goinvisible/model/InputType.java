package com.invisibleteam.goinvisible.model;

public enum InputType {
    /**
     * Text with no restrictions.
     */
    TEXT_STRING,
    /**
     * Text containing timestamp.
     */
    TIMESTAMP_STRING,
    /**
     * Text containing datetime.
     */
    DATETIME_STRING,
    /**
     * Text containing date.
     */
    DATE_STRING,
    /**
     * Ranged text.
     */
    RANGED_STRING,
    /**
     * 0/1 number.
     */
    RANGED_INTEGER,
    /**
     * Integer with no restrictions.
     */
    VALUE_INTEGER,
    /**
     * Double with no restrictions.
     */
    VALUE_DOUBLE,
    /**
     * Simple rational type.
     */
    RATIONAL,
    /**
     * Latitude, longitude double.
     */
    POSITION_DOUBLE,
    /**
     * Unmodifiable type.
     */
    UNMODIFIABLE,
    /**
     * For unknown type.
     */
    INDEFINITE
}
