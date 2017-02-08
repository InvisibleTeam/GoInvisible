package com.invisibleteam.goinvisible.model;


import javax.annotation.Nullable;

public class GeolocationTag extends Tag {

    public GeolocationTag(
            String key,
            String secondKey,
            @Nullable String value,
            @Nullable String secondValue,
            TagType tagType) {

        super(key, secondKey, value, secondValue, tagType);
    }

    @Override
    protected String generateFormattedValue() {
        return getValue() + " " + getSecondValue();
    }
}
