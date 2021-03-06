package com.invisibleteam.goinvisible.model;


import android.support.annotation.VisibleForTesting;

import com.invisibleteam.goinvisible.util.TagUtil;

public class GeolocationTag extends Tag {

    private String secondKey;
    private String secondValue;
    private String latitudeRef;
    private String longitudeRef;

    public GeolocationTag(
            String key,
            String secondKey,
            String value,
            String secondValue,
            String latitudeRef,
            String longitudeRef,
            TagType tagType) {

        super(key, value, tagType, TagGroupType.LOCATION_INFO);

        this.secondKey = secondKey;
        this.secondValue = secondValue;
        this.latitudeRef = latitudeRef;
        this.longitudeRef = longitudeRef;

        formatPositionValue();
    }

    @Override
    public Tag copy() {
        return new GeolocationTag(
                this.getKey(),
                this.getSecondKey(),
                this.getValue(),
                this.getSecondValue(),
                this.getLatitudeRef(),
                this.getLongitudeRef(),
                this.getTagType());
    }

    private void formatPositionValue() {
        String geoLat = TagUtil.parseRationalGPSToGeoGPS(getValue(), latitudeRef);
        String geoLng = TagUtil.parseRationalGPSToGeoGPS(secondValue, longitudeRef);

        formattedValue = geoLat + "\n" + geoLng;
    }

    @VisibleForTesting
    public void setSecondKey(String secondKey) {
        this.secondKey = secondKey;
    }

    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
        formatPositionValue();
    }

    public void setLatitudeRef(String latitudeRef) {
        this.latitudeRef = latitudeRef;
        formatPositionValue();
    }

    public void setLongitudeRef(String longitudeRef) {
        this.longitudeRef = longitudeRef;
        formatPositionValue();
    }

    public String getSecondKey() {
        return secondKey;
    }

    public String getSecondValue() {
        return secondValue;
    }

    public String getLatitudeRef() {
        return latitudeRef;
    }

    public String getLongitudeRef() {
        return longitudeRef;
    }
}
