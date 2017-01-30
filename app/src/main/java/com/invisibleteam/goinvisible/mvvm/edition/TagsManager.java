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
import static com.invisibleteam.goinvisible.model.InputType.TextString;

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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tagsList.addAll(getAllTags(TAG_LIST_N));
        }
        return tagsList;
    }

    private List<Tag> getAllTags(Map<String, InputType> keysList) {
        List<Tag> tagsList = new ArrayList<>();
        for (Map.Entry<String, InputType> entry : keysList.entrySet()) {
            String key = entry.getKey();
            InputType inputType = entry.getValue();
            Object tagValue;

            switch (inputType) {
                case TextString:
                case DateString:
                    tagValue = exifInterface.getAttribute(key);
                    break;
                case BooleanInteger:
                case ValueInteger:
                    tagValue = exifInterface.getAttributeInt(key, 0);
                    break;
                case ValueDouble:
                case PositionDouble:
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

    private static final Map<String, InputType> TAG_LIST = new HashMap<String, InputType>() {
        {
            put(TAG_GPS_DATESTAMP, TextString);
            put(TAG_GPS_LATITUDE_REF, TextString);
            put(TAG_GPS_TIMESTAMP, TextString);
            put(TAG_GPS_LONGITUDE_REF, TextString);
            put(TAG_GPS_PROCESSING_METHOD, TextString);
            put(TAG_DATETIME, TextString);
            put(TAG_MAKE, TextString);
            put(TAG_MODEL, TextString);
            put(TAG_IMAGE_LENGTH, InputType.ValueInteger);
            put(TAG_WHITE_BALANCE, InputType.ValueInteger);
            put(TAG_GPS_ALTITUDE_REF, InputType.ValueInteger);
            put(TAG_FLASH, InputType.ValueInteger);
            put(TAG_IMAGE_WIDTH, InputType.ValueInteger);
            put(TAG_ORIENTATION, InputType.ValueInteger);
            put(TAG_EXPOSURE_TIME, InputType.ValueDouble);

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static final Map<String, InputType> TAG_LIST_N = new HashMap<String, InputType>() {
        {
            put(TAG_COPYRIGHT, TextString);
            put(TAG_IMAGE_DESCRIPTION, TextString);
            put(TAG_ARTIST, TextString);
            put(TAG_SOFTWARE, TextString);
            put(TAG_CFA_PATTERN, TextString);
            put(TAG_COMPONENTS_CONFIGURATION, TextString);
            put(TAG_DATETIME_DIGITIZED, TextString);
            put(TAG_DATETIME_ORIGINAL, TextString);
            put(TAG_DEVICE_SETTING_DESCRIPTION, TextString);
            put(TAG_EXIF_VERSION, TextString);
            put(TAG_FILE_SOURCE, TextString);
            put(TAG_FLASHPIX_VERSION, TextString);
            put(TAG_SPATIAL_FREQUENCY_RESPONSE, TextString);
            put(TAG_SPECTRAL_SENSITIVITY, TextString);
            put(TAG_SUBSEC_TIME, TextString);
            put(TAG_SCENE_TYPE, TextString);
            put(TAG_RELATED_SOUND_FILE, TextString);
            put(TAG_IMAGE_UNIQUE_ID, TextString);
            put(TAG_MAKER_NOTE, TextString);
            put(TAG_OECF, TextString);
            put(TAG_SUBSEC_TIME_DIGITIZED, TextString);
            put(TAG_SUBSEC_TIME_ORIGINAL, TextString);
            put(TAG_GPS_AREA_INFORMATION, TextString);
            put(TAG_GPS_DEST_DISTANCE_REF, TextString);
            put(TAG_GPS_DEST_LATITUDE_REF, TextString);
            put(TAG_GPS_DEST_LONGITUDE_REF, TextString);
            put(TAG_GPS_MAP_DATUM, TextString);
            put(TAG_GPS_MEASURE_MODE, TextString);
            put(TAG_GPS_SATELLITES, TextString);
            put(TAG_GPS_SPEED_REF, TextString);
            put(TAG_GPS_STATUS, TextString);
            put(TAG_USER_COMMENT, TextString);
            put(TAG_GPS_TRACK_REF, TextString);
            put(TAG_GPS_VERSION_ID, TextString);
            put(TAG_INTEROPERABILITY_INDEX, TextString);
            put(TAG_PHOTOMETRIC_INTERPRETATION, InputType.ValueInteger);
            put(TAG_BITS_PER_SAMPLE, InputType.ValueInteger);
            put(TAG_COMPRESSION, InputType.ValueInteger);
            put(TAG_JPEG_INTERCHANGE_FORMAT, InputType.ValueInteger);
            put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, InputType.ValueInteger);
            put(TAG_PLANAR_CONFIGURATION, InputType.ValueInteger);
            put(TAG_RESOLUTION_UNIT, InputType.ValueInteger);
            put(TAG_ROWS_PER_STRIP, InputType.ValueInteger);
            put(TAG_SAMPLES_PER_PIXEL, InputType.ValueInteger);
            put(TAG_STRIP_BYTE_COUNTS, InputType.ValueInteger);
            put(TAG_STRIP_OFFSETS, InputType.ValueInteger);
            put(TAG_TRANSFER_FUNCTION, InputType.ValueInteger);
            put(TAG_Y_CB_CR_POSITIONING, InputType.ValueInteger);
            put(TAG_Y_CB_CR_SUB_SAMPLING, InputType.ValueInteger);
            put(TAG_COLOR_SPACE, InputType.ValueInteger);
            put(TAG_CONTRAST, InputType.ValueInteger);
            put(TAG_CUSTOM_RENDERED, InputType.ValueInteger);
            put(TAG_EXPOSURE_MODE, InputType.ValueInteger);
            put(TAG_EXPOSURE_PROGRAM, InputType.ValueInteger);
            put(TAG_FOCAL_LENGTH_IN_35MM_FILM, InputType.ValueInteger);
            put(TAG_FOCAL_PLANE_RESOLUTION_UNIT, InputType.ValueInteger);
            put(TAG_GAIN_CONTROL, InputType.ValueInteger);
            put(TAG_ISO_SPEED_RATINGS, InputType.ValueInteger);
            put(TAG_LIGHT_SOURCE, InputType.ValueInteger);
            put(TAG_METERING_MODE, InputType.ValueInteger);
            put(TAG_PIXEL_X_DIMENSION, InputType.ValueInteger);
            put(TAG_PIXEL_Y_DIMENSION, InputType.ValueInteger);
            put(TAG_SATURATION, InputType.ValueInteger);
            put(TAG_SCENE_CAPTURE_TYPE, InputType.ValueInteger);
            put(TAG_SENSING_METHOD, InputType.ValueInteger);
            put(TAG_SHARPNESS, InputType.ValueInteger);
            put(TAG_SUBJECT_AREA, InputType.ValueInteger);
            put(TAG_SUBJECT_DISTANCE_RANGE, InputType.ValueInteger);
            put(TAG_SUBJECT_LOCATION, InputType.ValueInteger);
            put(TAG_GPS_DIFFERENTIAL, InputType.ValueInteger);
            put(TAG_THUMBNAIL_IMAGE_LENGTH, InputType.ValueInteger);
            put(TAG_THUMBNAIL_IMAGE_WIDTH, InputType.ValueInteger);
            put(TAG_DIGITAL_ZOOM_RATIO, InputType.ValueDouble);
            put(TAG_EXPOSURE_BIAS_VALUE, InputType.ValueDouble);
            put(TAG_F_NUMBER, InputType.ValueDouble);
            put(TAG_SUBJECT_DISTANCE, InputType.ValueDouble);
        }
    };
}