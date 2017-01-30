package com.invisibleteam.goinvisible.model;

public enum InputType {
    /**
     * Text with no restrictions.
     */
    TextString,
    /**
     * Text containing date.
     */
    DateString,
    /**
     * 0/1 number.
     */
    BooleanInteger,
    /**
     * Integer with no restrictions.
     */
    ValueInteger,
    /**
     * Double with no restrictions.
     */
    ValueDouble,
    /**
     * Latitude, longitude double.
     */
    PositionDouble
}
