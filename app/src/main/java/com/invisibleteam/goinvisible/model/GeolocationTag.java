package com.invisibleteam.goinvisible.model;

public class GeolocationTag extends Tag {

    private static final String tagKey = "Geolocation";

    public GeolocationTag(
            String key,
            String secondKey,
            String value,
            String secondValue,
            TagType tagType) {

        super(key, secondKey, value, secondValue, tagType);
    }

    @Override
    protected String generateFormattedKey() {
        return tagKey;
    }

    @Override
    protected String generateFormattedValue() {
        return getValue() + " " + getSecondValue();
    }
}
