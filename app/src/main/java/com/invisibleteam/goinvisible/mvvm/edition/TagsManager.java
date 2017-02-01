package com.invisibleteam.goinvisible.mvvm.edition;

import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.media.ExifInterface.*;
import static com.invisibleteam.goinvisible.model.InputType.DATETIME_STRING;
import static com.invisibleteam.goinvisible.model.InputType.DATE_STRING;
import static com.invisibleteam.goinvisible.model.InputType.RANGED_INTEGER;
import static com.invisibleteam.goinvisible.model.InputType.RANGED_STRING;
import static com.invisibleteam.goinvisible.model.InputType.TEXT_STRING;
import static com.invisibleteam.goinvisible.model.InputType.TIMESTAMP_STRING;
import static com.invisibleteam.goinvisible.model.InputType.VALUE_DOUBLE;
import static com.invisibleteam.goinvisible.model.InputType.VALUE_INTEGER;

class TagsManager {

    private static final String TAG = TagsManager.class.getSimpleName();
    private final ExifInterface exifInterface;

    TagsManager(ExifInterface exifInterface) {
        this.exifInterface = exifInterface;
    }

    boolean editTag(Tag tag) {
        exifInterface.setAttribute(tag.getKey(), tag.getValue());
        try {
            exifInterface.saveAttributes();
            return true;
        } catch (IOException e) {
            Log.d(TAG, String.valueOf(e.getMessage()));
            return false;
        }
    }

    boolean clearTag(Tag tag) {
        /*if (tag.getKey().equals(TAG_GPS_TIMESTAMP)) {
            tag.setValue("00:00:00");
        } else {
            switch (tag.getObjectType()) {
                case STRING:
                    tag.setValue("");
                    break;
                case INTEGER:
                case DOUBLE:
                    tag.setValue("0");
                default:
            }
        }*/
        return editTag(tag);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Tag> getAllTags() {
        List<Tag> tagsList = new ArrayList<>();
        tagsList.addAll(getAllTags(TAG_LIST));
        tagsList.addAll(getAllTags(TAG_LIST_N));
        return tagsList;
    }

    private List<Tag> getAllTags(Map<String, InputType> keysList) {
        List<Tag> tagsList = new ArrayList<>();
        for (Map.Entry<String, InputType> entry : keysList.entrySet()) {
            String key = entry.getKey();
            InputType inputType = entry.getValue();
            Object tagValue;

            switch (inputType) {
                case TEXT_STRING:
                case DATE_STRING:
                case TIMESTAMP_STRING:
                    tagValue = exifInterface.getAttribute(key);
                    break;
                case RANGED_INTEGER:
                case VALUE_INTEGER:
                    tagValue = exifInterface.getAttributeInt(key, 0);
                    break;
                case VALUE_DOUBLE:
                case POSITION_DOUBLE:
                    tagValue = exifInterface.getAttributeDouble(key, 0);
                    break;
                default:
                    continue;
            }

            String stringValue = String.valueOf(tagValue);
            tagsList.add(new Tag(key, stringValue, TagType.build(inputType)));

        }
        return tagsList;
    }

    private static final Map<String, InputType> TAG_LIST = new HashMap<String, InputType>() { // V
        {
            put(TAG_GPS_DATESTAMP, DATE_STRING);
            put(TAG_GPS_TIMESTAMP, TIMESTAMP_STRING);
            put(TAG_GPS_LATITUDE_REF, RANGED_STRING);
            put(TAG_GPS_LONGITUDE_REF, RANGED_STRING);
            put(TAG_GPS_PROCESSING_METHOD, TEXT_STRING);
            put(TAG_DATETIME, DATETIME_STRING);
            put(TAG_MAKE, TEXT_STRING);
            put(TAG_MODEL, TEXT_STRING);
            put(TAG_IMAGE_LENGTH, VALUE_INTEGER);
            put(TAG_WHITE_BALANCE, RANGED_INTEGER);
            put(TAG_GPS_ALTITUDE_REF, RANGED_INTEGER);
            put(TAG_FLASH, RANGED_INTEGER);
            put(TAG_IMAGE_WIDTH, VALUE_INTEGER);
            put(TAG_ORIENTATION, RANGED_INTEGER);
            put(TAG_EXPOSURE_TIME, VALUE_DOUBLE);
        }
    };

    /**
     * We are using these tags below N android version.
     * Annotation prevents from generating lint error.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static final Map<String, InputType> TAG_LIST_N = new HashMap<String, InputType>() {
        {
            put(TAG_COPYRIGHT, TEXT_STRING);
            put(TAG_IMAGE_DESCRIPTION, TEXT_STRING);
            put(TAG_ARTIST, TEXT_STRING);
            put(TAG_DATETIME_DIGITIZED, DATETIME_STRING);
            put(TAG_DATETIME_ORIGINAL, DATETIME_STRING);
            put(TAG_USER_COMMENT, TEXT_STRING);
            put(TAG_RESOLUTION_UNIT, RANGED_INTEGER);
            put(TAG_MAX_APERTURE_VALUE, VALUE_DOUBLE);
            put(TAG_BRIGHTNESS_VALUE, VALUE_DOUBLE);
            put(TAG_DIGITAL_ZOOM_RATIO, VALUE_DOUBLE);
            put(TAG_CONTRAST, RANGED_INTEGER);
            put(TAG_EXPOSURE_MODE, RANGED_INTEGER);
            put(TAG_EXPOSURE_PROGRAM, RANGED_INTEGER);
            put(TAG_GAIN_CONTROL, VALUE_INTEGER);
            put(TAG_ISO_SPEED_RATINGS, VALUE_INTEGER);
            put(TAG_METERING_MODE, RANGED_INTEGER);
            put(TAG_SATURATION, RANGED_INTEGER);
            put(TAG_SCENE_CAPTURE_TYPE, RANGED_INTEGER);
            put(TAG_SHARPNESS, VALUE_INTEGER);
            put(TAG_F_NUMBER, VALUE_DOUBLE); // Aperture

            put(TAG_SOFTWARE, TEXT_STRING);
            put(TAG_CFA_PATTERN, TEXT_STRING);
            put(TAG_COMPONENTS_CONFIGURATION, TEXT_STRING); // null
            put(TAG_DEVICE_SETTING_DESCRIPTION, TEXT_STRING);
            put(TAG_EXIF_VERSION, TEXT_STRING);
            put(TAG_FILE_SOURCE, TEXT_STRING);
            put(TAG_FLASHPIX_VERSION, TEXT_STRING);
            put(TAG_SPATIAL_FREQUENCY_RESPONSE, TEXT_STRING);
            put(TAG_SPECTRAL_SENSITIVITY, TEXT_STRING);
            put(TAG_SUBSEC_TIME, TEXT_STRING); // null
            put(TAG_SCENE_TYPE, TEXT_STRING);
            put(TAG_RELATED_SOUND_FILE, TEXT_STRING);
            put(TAG_IMAGE_UNIQUE_ID, TEXT_STRING);
            put(TAG_MAKER_NOTE, TEXT_STRING);
            put(TAG_OECF, TEXT_STRING);
            put(TAG_SUBSEC_TIME_DIGITIZED, TEXT_STRING);
            put(TAG_SUBSEC_TIME_ORIGINAL, TEXT_STRING);
            put(TAG_GPS_AREA_INFORMATION, TEXT_STRING); // null
            put(TAG_GPS_DEST_DISTANCE_REF, TEXT_STRING);
            put(TAG_GPS_DEST_LATITUDE_REF, TEXT_STRING);
            put(TAG_GPS_DEST_LONGITUDE_REF, TEXT_STRING);
            put(TAG_GPS_MAP_DATUM, TEXT_STRING);
            put(TAG_GPS_MEASURE_MODE, TEXT_STRING);
            put(TAG_GPS_SATELLITES, TEXT_STRING);
            put(TAG_GPS_SPEED_REF, TEXT_STRING);
            put(TAG_GPS_STATUS, TEXT_STRING);
            put(TAG_GPS_TRACK_REF, TEXT_STRING);
            put(TAG_GPS_VERSION_ID, TEXT_STRING);
            put(TAG_INTEROPERABILITY_INDEX, TEXT_STRING);
            put(TAG_PHOTOMETRIC_INTERPRETATION, VALUE_INTEGER);
            put(TAG_BITS_PER_SAMPLE, VALUE_INTEGER);
            put(TAG_COMPRESSED_BITS_PER_PIXEL, VALUE_INTEGER);
            put(TAG_COMPRESSION, VALUE_INTEGER);
            put(TAG_JPEG_INTERCHANGE_FORMAT, VALUE_INTEGER);
            put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, VALUE_INTEGER);
            put(TAG_PLANAR_CONFIGURATION, VALUE_INTEGER);
            put(TAG_ROWS_PER_STRIP, VALUE_INTEGER);
            put(TAG_SAMPLES_PER_PIXEL, VALUE_INTEGER);
            put(TAG_STRIP_BYTE_COUNTS, VALUE_INTEGER);
            put(TAG_STRIP_OFFSETS, VALUE_INTEGER);
            put(TAG_TRANSFER_FUNCTION, VALUE_INTEGER);
            put(TAG_Y_CB_CR_POSITIONING, VALUE_INTEGER);
            put(TAG_Y_CB_CR_SUB_SAMPLING, VALUE_INTEGER);
            put(TAG_COLOR_SPACE, VALUE_INTEGER);
            put(TAG_CUSTOM_RENDERED, VALUE_INTEGER);
            put(TAG_FOCAL_LENGTH_IN_35MM_FILM, VALUE_INTEGER);
            put(TAG_FOCAL_PLANE_RESOLUTION_UNIT, VALUE_INTEGER); // 0
            put(TAG_LIGHT_SOURCE, VALUE_INTEGER);
            put(TAG_PIXEL_X_DIMENSION, VALUE_INTEGER); // 0
            put(TAG_PIXEL_Y_DIMENSION, VALUE_INTEGER);
            put(TAG_SENSING_METHOD, VALUE_INTEGER);
            put(TAG_SUBJECT_AREA, VALUE_INTEGER);
            put(TAG_SUBJECT_DISTANCE_RANGE, VALUE_INTEGER);
            put(TAG_SUBJECT_LOCATION, VALUE_INTEGER);
            put(TAG_GPS_DIFFERENTIAL, VALUE_INTEGER);
            put(TAG_THUMBNAIL_IMAGE_LENGTH, VALUE_INTEGER);
            put(TAG_THUMBNAIL_IMAGE_WIDTH, VALUE_INTEGER);
            put(TAG_DIGITAL_ZOOM_RATIO, VALUE_DOUBLE);
            put(TAG_EXPOSURE_BIAS_VALUE, VALUE_DOUBLE);
            put(TAG_SUBJECT_DISTANCE, VALUE_DOUBLE);
        }
    };
}