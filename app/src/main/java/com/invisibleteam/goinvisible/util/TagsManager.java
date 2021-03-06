package com.invisibleteam.goinvisible.util;

import android.support.media.ExifInterface;
import android.util.Log;

import com.invisibleteam.goinvisible.model.GeolocationTag;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroupType;
import com.invisibleteam.goinvisible.model.TagType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.media.ExifInterface.*;
import static com.invisibleteam.goinvisible.model.InputType.DATETIME_STRING;
import static com.invisibleteam.goinvisible.model.InputType.DATE_STRING;
import static com.invisibleteam.goinvisible.model.InputType.POSITION_DOUBLE;
import static com.invisibleteam.goinvisible.model.InputType.RANGED_INTEGER;
import static com.invisibleteam.goinvisible.model.InputType.RATIONAL;
import static com.invisibleteam.goinvisible.model.InputType.TEXT_STRING;
import static com.invisibleteam.goinvisible.model.InputType.TIMESTAMP_STRING;
import static com.invisibleteam.goinvisible.model.InputType.UNMODIFIABLE;
import static com.invisibleteam.goinvisible.model.InputType.VALUE_DOUBLE;
import static com.invisibleteam.goinvisible.model.InputType.VALUE_INTEGER;
import static com.invisibleteam.goinvisible.model.TagGroupType.ADVANCED;
import static com.invisibleteam.goinvisible.model.TagGroupType.DEVICE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.IMAGE_INFO;
import static com.invisibleteam.goinvisible.model.TagGroupType.LOCATION_INFO;

public class TagsManager {

    private static final String TAG = TagsManager.class.getSimpleName();
    private final ExifInterface exifInterface;

    public TagsManager(ExifInterface exifInterface) {
        this.exifInterface = exifInterface;
    }

    private void setExifAttributes(Tag tag) {
        if (tag.getValue() == null) {
            Log.w(TAG, "Tag value is null");
            return;
        }

        if (RATIONAL.equals(tag.getTagType().getInputType())) {
            double tagValue = Double.parseDouble(tag.getValue());
            String rationalTagValue = TagUtil.parseDoubleValueToRational(tagValue);
            exifInterface.setAttribute(tag.getKey(), rationalTagValue);
        } else if (isPositionTag(tag)) {
            GeolocationTag geolocationTag = (GeolocationTag) tag;
            exifInterface.setAttribute(geolocationTag.getKey(), geolocationTag.getValue());
            exifInterface.setAttribute(geolocationTag.getSecondKey(), geolocationTag.getSecondValue());
            exifInterface.setAttribute(TAG_GPS_LATITUDE_REF, geolocationTag.getLatitudeRef());
            exifInterface.setAttribute(TAG_GPS_LONGITUDE_REF, geolocationTag.getLongitudeRef());
        } else {
            exifInterface.setAttribute(tag.getKey(), tag.getValue());
        }
    }

    public boolean saveTags(List<Tag> tags) {
        if (tags.isEmpty()) {
            return false;
        }

        for (Tag tag : tags) {
            setExifAttributes(tag);
        }

        try {
            exifInterface.saveAttributes();
            return true;
        } catch (IOException e) {
            Log.d(TAG, String.valueOf(e.getMessage()));
            return false;
        }
    }

    private boolean isPositionTag(Tag tag) {
        return tag instanceof GeolocationTag;
    }

    public void clearTags(List<Tag> tagsList) {
        for (Tag tag : tagsList) {
            clearTag(tag);
        }
    }

    public void clearTag(Tag tag) {
        switch (tag.getTagType().getInputType()) {
            case TEXT_STRING:
                tag.setValue("");
                break;
            case TIMESTAMP_STRING:
                tag.setValue("00:00:00");
                break;
            case DATETIME_STRING:
                tag.setValue("2001-01-01 00:00:00");
                break;
            case DATE_STRING:
                tag.setValue("2001-01-01");
                break;
            case RANGED_STRING:
            case RANGED_INTEGER:
            case VALUE_INTEGER:
                tag.setValue("0");
                break;
            case VALUE_DOUBLE:
                tag.setValue("0.0");
                break;
            case POSITION_DOUBLE:
                clearPositionTag((GeolocationTag) tag);
                break;
            case RATIONAL:
                tag.setValue("0.0");
                break;
            default:
                Log.w(TAG, "Incorrect tag type!");
                break;
        }
        setExifAttributes(tag);
    }

    private void clearPositionTag(GeolocationTag tag) {
        final String defaultGPSValue = TagUtil.parseDoubleGPSToRationalGPS(0.000000);
        tag.setValue(defaultGPSValue);
        tag.setSecondValue(defaultGPSValue);
        final Tag latitudeRef = new Tag(TAG_GPS_LATITUDE_REF, "", TagType.build(InputType.POSITION_DOUBLE), LOCATION_INFO);
        setExifAttributes(latitudeRef);
        final Tag longitudeRef = new Tag(TAG_GPS_LONGITUDE_REF, "", TagType.build(InputType.POSITION_DOUBLE), LOCATION_INFO);
        setExifAttributes(longitudeRef);
    }

    private GeolocationTag getGeolocationTag() {
        TagType tagType = TagType.build(POSITION_DOUBLE);
        String rationalLat = exifInterface.getAttribute(TAG_GPS_LATITUDE);
        String rationalLng = exifInterface.getAttribute(TAG_GPS_LONGITUDE);
        String latRef = exifInterface.getAttribute(TAG_GPS_LATITUDE_REF);
        String lngRef = exifInterface.getAttribute(TAG_GPS_LONGITUDE_REF);

        return new GeolocationTag(
                TAG_GPS_LATITUDE,
                TAG_GPS_LONGITUDE,
                rationalLat,
                rationalLng,
                latRef,
                lngRef,
                tagType);
    }

    public List<Tag> getAllTags() {
        List<Tag> tagsList = new ArrayList<>();
        tagsList.add(getGeolocationTag());
        tagsList.addAll(getAllTags(TAG_LIST, TAG_GROUP_LIST));

        Collections.sort(tagsList, (tag, t1) -> tag.getTagGroupType().ordinal() - t1.getTagGroupType().ordinal());

        return tagsList;
    }

    private List<Tag> getAllTags(Map<String, InputType> keysList, Map<String, TagGroupType> keysGroupList) {
        List<Tag> tagsList = new ArrayList<>();
        for (Map.Entry<String, InputType> entry : keysList.entrySet()) {
            String key = entry.getKey();
            InputType inputType = entry.getValue();
            Object tagValue;

            switch (inputType) {
                case TEXT_STRING:
                case DATE_STRING:
                case DATETIME_STRING:
                case TIMESTAMP_STRING:
                    tagValue = exifInterface.getAttribute(key);
                    break;
                case RANGED_INTEGER:
                case RANGED_STRING:
                case UNMODIFIABLE:
                case VALUE_INTEGER:
                    tagValue = exifInterface.getAttributeInt(key, 0);
                    break;
                case VALUE_DOUBLE:
                case POSITION_DOUBLE:
                    tagValue = exifInterface.getAttributeDouble(key, 0);
                    break;
                case RATIONAL:
                    tagValue = TagUtil.parseRationalValueToDouble(exifInterface.getAttribute(key));
                    break;
                default:
                    continue;
            }

            String stringValue = String.valueOf(tagValue);
            tagsList.add(new Tag(key, stringValue, TagType.build(inputType), keysGroupList.get(key)));

        }

        return tagsList;
    }

    private static final Map<String, InputType> TAG_LIST;
    private static final Map<String, TagGroupType> TAG_GROUP_LIST;

    static {
        TAG_LIST = getTags();
        TAG_GROUP_LIST = getTagsGroups();
    }

    static private Map<String, InputType> getTags() {
        Map<String, InputType> map = new HashMap<>();
        //location
        map.put(TAG_GPS_DATESTAMP, DATE_STRING);
        map.put(TAG_GPS_TIMESTAMP, TIMESTAMP_STRING);
        map.put(TAG_GPS_PROCESSING_METHOD, TEXT_STRING);
        map.put(TAG_GPS_ALTITUDE_REF, RANGED_INTEGER);
        map.put(TAG_GPS_AREA_INFORMATION, TEXT_STRING); // null
        map.put(TAG_GPS_DEST_DISTANCE_REF, TEXT_STRING);
        map.put(TAG_GPS_DEST_BEARING, RATIONAL);
        map.put(TAG_GPS_DEST_DISTANCE, RATIONAL);
        map.put(TAG_GPS_DEST_LATITUDE_REF, TEXT_STRING);
        map.put(TAG_GPS_DEST_LATITUDE, RATIONAL);
        map.put(TAG_GPS_DEST_LONGITUDE_REF, TEXT_STRING);
        map.put(TAG_GPS_DEST_LONGITUDE, RATIONAL);
        map.put(TAG_GPS_MAP_DATUM, TEXT_STRING);
        map.put(TAG_GPS_MEASURE_MODE, TEXT_STRING);
        map.put(TAG_GPS_SATELLITES, TEXT_STRING);
        map.put(TAG_GPS_SPEED_REF, TEXT_STRING);
        map.put(TAG_GPS_SPEED, RATIONAL);
        map.put(TAG_GPS_STATUS, TEXT_STRING);
        map.put(TAG_GPS_TRACK_REF, TEXT_STRING);
        map.put(TAG_GPS_TRACK, RATIONAL);
        map.put(TAG_GPS_VERSION_ID, TEXT_STRING);
        map.put(TAG_GPS_DIFFERENTIAL, VALUE_INTEGER);
        map.put(TAG_GPS_DOP, RATIONAL);
        map.put(TAG_GPS_IMG_DIRECTION, RATIONAL);

        //image
        map.put(TAG_USER_COMMENT, TEXT_STRING);
        map.put(TAG_ARTIST, TEXT_STRING);
        map.put(TAG_IMAGE_DESCRIPTION, TEXT_STRING);
        map.put(TAG_COPYRIGHT, TEXT_STRING);
        map.put(TAG_BRIGHTNESS_VALUE, RATIONAL);
        map.put(TAG_RESOLUTION_UNIT, RANGED_INTEGER);
        map.put(TAG_CONTRAST, RANGED_INTEGER);
        map.put(TAG_SATURATION, RANGED_INTEGER);
        map.put(TAG_FLASH_ENERGY, RATIONAL);
        map.put(TAG_EXIF_VERSION, TEXT_STRING);
        map.put(TAG_ISO_SPEED_RATINGS, VALUE_INTEGER);
        map.put(TAG_SOFTWARE, TEXT_STRING);
        map.put(TAG_EXPOSURE_MODE, RANGED_INTEGER);
        map.put(TAG_EXPOSURE_INDEX, RATIONAL);
        map.put(TAG_EXPOSURE_PROGRAM, RANGED_INTEGER);
        map.put(TAG_DATETIME, DATETIME_STRING);
        map.put(TAG_IMAGE_LENGTH, UNMODIFIABLE);
        map.put(TAG_IMAGE_WIDTH, UNMODIFIABLE);
        map.put(TAG_ORIENTATION, RANGED_INTEGER);
        map.put(TAG_EXPOSURE_TIME, VALUE_DOUBLE);

        //device
        map.put(TAG_MODEL, TEXT_STRING);
        map.put(TAG_WHITE_BALANCE, RANGED_INTEGER);
        map.put(TAG_FLASH, RANGED_INTEGER);
        map.put(TAG_FOCAL_LENGTH_IN_35MM_FILM, VALUE_INTEGER);
        map.put(TAG_FOCAL_LENGTH, RATIONAL);
        map.put(TAG_FOCAL_PLANE_X_RESOLUTION, RATIONAL);
        map.put(TAG_FOCAL_PLANE_Y_RESOLUTION, RATIONAL);
        map.put(TAG_FOCAL_PLANE_RESOLUTION_UNIT, VALUE_INTEGER); // 0
        map.put(TAG_F_NUMBER, VALUE_DOUBLE); // Aperture
        map.put(TAG_DIGITAL_ZOOM_RATIO, VALUE_DOUBLE);
        map.put(TAG_MAX_APERTURE_VALUE, RATIONAL);
        map.put(TAG_APERTURE_VALUE, RATIONAL);

        //advanced
        map.put(TAG_MAKE, TEXT_STRING);
        map.put(TAG_DATETIME_DIGITIZED, DATETIME_STRING);
        map.put(TAG_DATETIME_ORIGINAL, DATETIME_STRING);
        map.put(TAG_GAIN_CONTROL, RANGED_INTEGER);
        map.put(TAG_METERING_MODE, RANGED_INTEGER);
        map.put(TAG_SCENE_CAPTURE_TYPE, RANGED_INTEGER);
        map.put(TAG_SHARPNESS, RANGED_INTEGER);
        map.put(TAG_CFA_PATTERN, TEXT_STRING);
        map.put(TAG_COMPONENTS_CONFIGURATION, TEXT_STRING); // null
        map.put(TAG_DEVICE_SETTING_DESCRIPTION, TEXT_STRING);
        map.put(TAG_FILE_SOURCE, TEXT_STRING);
        map.put(TAG_FLASHPIX_VERSION, TEXT_STRING);
        map.put(TAG_SPATIAL_FREQUENCY_RESPONSE, TEXT_STRING);
        map.put(TAG_SPECTRAL_SENSITIVITY, TEXT_STRING);
        map.put(TAG_SUBSEC_TIME, TEXT_STRING); // null
        map.put(TAG_SCENE_TYPE, TEXT_STRING);
        map.put(TAG_RELATED_SOUND_FILE, TEXT_STRING);
        map.put(TAG_IMAGE_UNIQUE_ID, TEXT_STRING);
        map.put(TAG_MAKER_NOTE, TEXT_STRING);
        map.put(TAG_OECF, TEXT_STRING);
        map.put(TAG_SUBSEC_TIME_DIGITIZED, TEXT_STRING);
        map.put(TAG_SUBSEC_TIME_ORIGINAL, TEXT_STRING);
        map.put(TAG_INTEROPERABILITY_INDEX, TEXT_STRING);
        map.put(TAG_PHOTOMETRIC_INTERPRETATION, VALUE_INTEGER);
        map.put(TAG_BITS_PER_SAMPLE, VALUE_INTEGER);
        map.put(TAG_COMPRESSED_BITS_PER_PIXEL, RATIONAL);
        map.put(TAG_COMPRESSION, VALUE_INTEGER);
        map.put(TAG_JPEG_INTERCHANGE_FORMAT, UNMODIFIABLE);
        map.put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, UNMODIFIABLE);
        map.put(TAG_PLANAR_CONFIGURATION, VALUE_INTEGER);
        map.put(TAG_ROWS_PER_STRIP, VALUE_INTEGER);
        map.put(TAG_SAMPLES_PER_PIXEL, VALUE_INTEGER);
        map.put(TAG_STRIP_BYTE_COUNTS, VALUE_INTEGER);
        map.put(TAG_STRIP_OFFSETS, VALUE_INTEGER);
        map.put(TAG_TRANSFER_FUNCTION, VALUE_INTEGER);
        map.put(TAG_Y_CB_CR_POSITIONING, VALUE_INTEGER);
        map.put(TAG_Y_CB_CR_SUB_SAMPLING, VALUE_INTEGER);
        map.put(TAG_COLOR_SPACE, VALUE_INTEGER);
        map.put(TAG_CUSTOM_RENDERED, VALUE_INTEGER);
        map.put(TAG_LIGHT_SOURCE, VALUE_INTEGER);
        map.put(TAG_PIXEL_X_DIMENSION, VALUE_INTEGER); // 0
        map.put(TAG_PIXEL_Y_DIMENSION, VALUE_INTEGER);
        map.put(TAG_SENSING_METHOD, VALUE_INTEGER);
        map.put(TAG_SUBJECT_AREA, VALUE_INTEGER);
        map.put(TAG_SUBJECT_DISTANCE_RANGE, VALUE_INTEGER);
        map.put(TAG_SUBJECT_LOCATION, VALUE_INTEGER);
        map.put(TAG_THUMBNAIL_IMAGE_LENGTH, VALUE_INTEGER);
        map.put(TAG_THUMBNAIL_IMAGE_WIDTH, VALUE_INTEGER);
        map.put(TAG_DIGITAL_ZOOM_RATIO, VALUE_DOUBLE);
        map.put(TAG_EXPOSURE_BIAS_VALUE, UNMODIFIABLE);
        map.put(TAG_PRIMARY_CHROMATICITIES, RATIONAL);
        map.put(TAG_REFERENCE_BLACK_WHITE, RATIONAL);
        map.put(TAG_SHUTTER_SPEED_VALUE, RATIONAL);
        map.put(TAG_WHITE_POINT, RATIONAL);
        map.put(TAG_X_RESOLUTION, RATIONAL);
        map.put(TAG_Y_RESOLUTION, RATIONAL);


        return Collections.unmodifiableMap(map);
    }

    static private Map<String, TagGroupType> getTagsGroups() {
        Map<String, TagGroupType> map = new HashMap<>();
        //location - 25 items
        map.put(TAG_GPS_DATESTAMP, LOCATION_INFO);
        map.put(TAG_GPS_TIMESTAMP, LOCATION_INFO);
        map.put(TAG_GPS_PROCESSING_METHOD, LOCATION_INFO);
        map.put(TAG_GPS_ALTITUDE_REF, LOCATION_INFO);
        map.put(TAG_GPS_AREA_INFORMATION, LOCATION_INFO); // null
        map.put(TAG_GPS_DEST_DISTANCE_REF, LOCATION_INFO);
        map.put(TAG_GPS_DEST_BEARING, LOCATION_INFO);
        map.put(TAG_GPS_DEST_DISTANCE, LOCATION_INFO);
        map.put(TAG_GPS_DEST_LATITUDE_REF, LOCATION_INFO);
        map.put(TAG_GPS_DEST_LATITUDE, LOCATION_INFO);
        map.put(TAG_GPS_DEST_LONGITUDE_REF, LOCATION_INFO);
        map.put(TAG_GPS_DEST_LONGITUDE, LOCATION_INFO);
        map.put(TAG_GPS_MAP_DATUM, LOCATION_INFO);
        map.put(TAG_GPS_MEASURE_MODE, LOCATION_INFO);
        map.put(TAG_GPS_SATELLITES, LOCATION_INFO);
        map.put(TAG_GPS_SPEED_REF, LOCATION_INFO);
        map.put(TAG_GPS_SPEED, LOCATION_INFO);
        map.put(TAG_GPS_STATUS, LOCATION_INFO);
        map.put(TAG_GPS_TRACK_REF, LOCATION_INFO);
        map.put(TAG_GPS_TRACK, LOCATION_INFO);
        map.put(TAG_GPS_VERSION_ID, LOCATION_INFO);
        map.put(TAG_GPS_DIFFERENTIAL, LOCATION_INFO);
        map.put(TAG_GPS_DOP, LOCATION_INFO);
        map.put(TAG_GPS_IMG_DIRECTION, LOCATION_INFO);

        //image - 20 items
        map.put(TAG_USER_COMMENT, IMAGE_INFO);
        map.put(TAG_ARTIST, IMAGE_INFO);
        map.put(TAG_IMAGE_DESCRIPTION, IMAGE_INFO);
        map.put(TAG_COPYRIGHT, IMAGE_INFO);
        map.put(TAG_BRIGHTNESS_VALUE, IMAGE_INFO);
        map.put(TAG_RESOLUTION_UNIT, IMAGE_INFO);
        map.put(TAG_CONTRAST, IMAGE_INFO);
        map.put(TAG_SATURATION, IMAGE_INFO);
        map.put(TAG_FLASH_ENERGY, IMAGE_INFO);
        map.put(TAG_EXIF_VERSION, IMAGE_INFO);
        map.put(TAG_ISO_SPEED_RATINGS, IMAGE_INFO);
        map.put(TAG_SOFTWARE, IMAGE_INFO);
        map.put(TAG_EXPOSURE_MODE, IMAGE_INFO);
        map.put(TAG_EXPOSURE_INDEX, IMAGE_INFO);
        map.put(TAG_EXPOSURE_PROGRAM, IMAGE_INFO);
        map.put(TAG_DATETIME, IMAGE_INFO);
        map.put(TAG_IMAGE_LENGTH, IMAGE_INFO);
        map.put(TAG_IMAGE_WIDTH, IMAGE_INFO);
        map.put(TAG_ORIENTATION, IMAGE_INFO);
        map.put(TAG_EXPOSURE_TIME, IMAGE_INFO);

        //device - 12 items
        map.put(TAG_MODEL, DEVICE_INFO);
        map.put(TAG_WHITE_BALANCE, DEVICE_INFO);
        map.put(TAG_FLASH, DEVICE_INFO);
        map.put(TAG_FOCAL_LENGTH_IN_35MM_FILM, DEVICE_INFO);
        map.put(TAG_FOCAL_LENGTH, DEVICE_INFO);
        map.put(TAG_FOCAL_PLANE_X_RESOLUTION, DEVICE_INFO);
        map.put(TAG_FOCAL_PLANE_Y_RESOLUTION, DEVICE_INFO);
        map.put(TAG_FOCAL_PLANE_RESOLUTION_UNIT, DEVICE_INFO); // 0
        map.put(TAG_F_NUMBER, DEVICE_INFO); // Aperture
        map.put(TAG_DIGITAL_ZOOM_RATIO, DEVICE_INFO);
        map.put(TAG_MAX_APERTURE_VALUE, DEVICE_INFO);
        map.put(TAG_APERTURE_VALUE, DEVICE_INFO);

        //advanced - 55 items
        map.put(TAG_MAKE, ADVANCED);
        map.put(TAG_DATETIME_DIGITIZED, ADVANCED);
        map.put(TAG_DATETIME_ORIGINAL, ADVANCED);
        map.put(TAG_GAIN_CONTROL, ADVANCED);
        map.put(TAG_METERING_MODE, ADVANCED);
        map.put(TAG_SCENE_CAPTURE_TYPE, ADVANCED);
        map.put(TAG_SHARPNESS, ADVANCED);
        map.put(TAG_CFA_PATTERN, ADVANCED);
        map.put(TAG_COMPONENTS_CONFIGURATION, ADVANCED); // null
        map.put(TAG_DEVICE_SETTING_DESCRIPTION, ADVANCED);
        map.put(TAG_FILE_SOURCE, ADVANCED);
        map.put(TAG_FLASHPIX_VERSION, ADVANCED);
        map.put(TAG_SPATIAL_FREQUENCY_RESPONSE, ADVANCED);
        map.put(TAG_SPECTRAL_SENSITIVITY, ADVANCED);
        map.put(TAG_SUBSEC_TIME, ADVANCED); // null
        map.put(TAG_SCENE_TYPE, ADVANCED);
        map.put(TAG_RELATED_SOUND_FILE, ADVANCED);
        map.put(TAG_IMAGE_UNIQUE_ID, ADVANCED);
        map.put(TAG_MAKER_NOTE, ADVANCED);
        map.put(TAG_OECF, ADVANCED);
        map.put(TAG_SUBSEC_TIME_DIGITIZED, ADVANCED);
        map.put(TAG_SUBSEC_TIME_ORIGINAL, ADVANCED);
        map.put(TAG_INTEROPERABILITY_INDEX, ADVANCED);
        map.put(TAG_PHOTOMETRIC_INTERPRETATION, ADVANCED);
        map.put(TAG_BITS_PER_SAMPLE, ADVANCED);
        map.put(TAG_COMPRESSED_BITS_PER_PIXEL, ADVANCED);
        map.put(TAG_COMPRESSION, ADVANCED);
        map.put(TAG_JPEG_INTERCHANGE_FORMAT, ADVANCED);
        map.put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, ADVANCED);
        map.put(TAG_PLANAR_CONFIGURATION, ADVANCED);
        map.put(TAG_ROWS_PER_STRIP, ADVANCED);
        map.put(TAG_SAMPLES_PER_PIXEL, ADVANCED);
        map.put(TAG_STRIP_BYTE_COUNTS, ADVANCED);
        map.put(TAG_STRIP_OFFSETS, ADVANCED);
        map.put(TAG_TRANSFER_FUNCTION, ADVANCED);
        map.put(TAG_Y_CB_CR_POSITIONING, ADVANCED);
        map.put(TAG_Y_CB_CR_SUB_SAMPLING, ADVANCED);
        map.put(TAG_COLOR_SPACE, ADVANCED);
        map.put(TAG_CUSTOM_RENDERED, ADVANCED);
        map.put(TAG_LIGHT_SOURCE, ADVANCED);
        map.put(TAG_PIXEL_X_DIMENSION, ADVANCED); // 0
        map.put(TAG_PIXEL_Y_DIMENSION, ADVANCED);
        map.put(TAG_SENSING_METHOD, ADVANCED);
        map.put(TAG_SUBJECT_AREA, ADVANCED);
        map.put(TAG_SUBJECT_DISTANCE_RANGE, ADVANCED);
        map.put(TAG_SUBJECT_LOCATION, ADVANCED);
        map.put(TAG_THUMBNAIL_IMAGE_LENGTH, ADVANCED);
        map.put(TAG_THUMBNAIL_IMAGE_WIDTH, ADVANCED);
        map.put(TAG_DIGITAL_ZOOM_RATIO, ADVANCED);
        map.put(TAG_EXPOSURE_BIAS_VALUE, ADVANCED);
        map.put(TAG_PRIMARY_CHROMATICITIES, ADVANCED);
        map.put(TAG_REFERENCE_BLACK_WHITE, ADVANCED);
        map.put(TAG_SHUTTER_SPEED_VALUE, ADVANCED);
        map.put(TAG_WHITE_POINT, ADVANCED);
        map.put(TAG_X_RESOLUTION, ADVANCED);
        map.put(TAG_Y_RESOLUTION, ADVANCED);

        return Collections.unmodifiableMap(map);
    }
}