package com.invisibleteam.goinvisible.model;

public class GeolocationTag extends Tag {

    public GeolocationTag(
            String key,
            String secondKey,
            String value,
            String secondValue,
            TagType tagType) {

        super(key, secondKey, value, secondValue, tagType);
    }

    @Override
    protected String generateFormattedValue() {
        return getValue() + " " + getSecondValue();
    }
}
