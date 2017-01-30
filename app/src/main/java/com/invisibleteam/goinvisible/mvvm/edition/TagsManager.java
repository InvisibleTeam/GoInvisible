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
import java.util.Arrays;
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
            //TODO add N tags
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

    private static final List<String> STRING_TAGS_LIST = Arrays.asList(
            TAG_GPS_DATESTAMP,
            TAG_GPS_LATITUDE_REF,
            TAG_GPS_TIMESTAMP,
            TAG_GPS_LONGITUDE_REF,
            TAG_GPS_PROCESSING_METHOD,
            TAG_DATETIME,
            TAG_MAKE,
            TAG_MODEL);

    private static final List<String> DOUBLE_TAGS_LIST = Arrays.asList(
            TAG_EXPOSURE_TIME);

    private static final List<String> INTEGER_TAGS_LIST = Arrays.asList(
            TAG_IMAGE_LENGTH,
            TAG_WHITE_BALANCE,
            TAG_GPS_ALTITUDE_REF,
            TAG_FLASH,
            TAG_IMAGE_WIDTH,
            TAG_ORIENTATION);

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static final List<String> STRING_TAGS_LIST_API_24 = Arrays.asList(
            TAG_COPYRIGHT,
            TAG_IMAGE_DESCRIPTION,
            TAG_ARTIST,
            TAG_SOFTWARE,
            TAG_CFA_PATTERN,
            TAG_COMPONENTS_CONFIGURATION,
            TAG_DATETIME_DIGITIZED,
            TAG_DATETIME_ORIGINAL,
            TAG_DEVICE_SETTING_DESCRIPTION,
            TAG_EXIF_VERSION,
            TAG_FILE_SOURCE,
            TAG_FLASHPIX_VERSION,
            TAG_SPATIAL_FREQUENCY_RESPONSE,
            TAG_SPECTRAL_SENSITIVITY,
            TAG_SUBSEC_TIME,
            TAG_SCENE_TYPE,
            TAG_RELATED_SOUND_FILE,
            TAG_IMAGE_UNIQUE_ID,
            TAG_MAKER_NOTE,
            TAG_OECF,
            TAG_SUBSEC_TIME_DIGITIZED,
            TAG_SUBSEC_TIME_ORIGINAL,
            TAG_GPS_AREA_INFORMATION,
            TAG_GPS_DEST_DISTANCE_REF,
            TAG_GPS_DEST_LATITUDE_REF,
            TAG_GPS_DEST_LONGITUDE_REF,
            TAG_GPS_MAP_DATUM,
            TAG_GPS_MEASURE_MODE,
            TAG_GPS_SATELLITES,
            TAG_GPS_SPEED_REF,
            TAG_GPS_STATUS,
            TAG_USER_COMMENT,
            TAG_GPS_TRACK_REF,
            TAG_GPS_VERSION_ID,
            TAG_INTEROPERABILITY_INDEX);


    @RequiresApi(api = Build.VERSION_CODES.N)
    private static final List<String> INTEGER_TAGS_LIST_API_24 = Arrays.asList(
            TAG_PHOTOMETRIC_INTERPRETATION,
            TAG_BITS_PER_SAMPLE,
            TAG_COMPRESSION,
            TAG_JPEG_INTERCHANGE_FORMAT,
            TAG_JPEG_INTERCHANGE_FORMAT_LENGTH,
            TAG_PLANAR_CONFIGURATION,
            TAG_RESOLUTION_UNIT,
            TAG_ROWS_PER_STRIP,
            TAG_SAMPLES_PER_PIXEL,
            TAG_STRIP_BYTE_COUNTS,
            TAG_STRIP_OFFSETS,
            TAG_TRANSFER_FUNCTION,
            TAG_Y_CB_CR_POSITIONING,
            TAG_Y_CB_CR_SUB_SAMPLING,
            TAG_COLOR_SPACE,
            TAG_CONTRAST,
            TAG_CUSTOM_RENDERED,
            TAG_EXPOSURE_MODE,
            TAG_EXPOSURE_PROGRAM,
            TAG_FOCAL_LENGTH_IN_35MM_FILM,
            TAG_FOCAL_PLANE_RESOLUTION_UNIT,
            TAG_GAIN_CONTROL,
            TAG_ISO_SPEED_RATINGS,
            TAG_LIGHT_SOURCE,
            TAG_METERING_MODE,
            TAG_PIXEL_X_DIMENSION,
            TAG_PIXEL_Y_DIMENSION,
            TAG_SATURATION,
            TAG_SCENE_CAPTURE_TYPE,
            TAG_SENSING_METHOD,
            TAG_SHARPNESS,
            TAG_SUBJECT_AREA,
            TAG_SUBJECT_DISTANCE_RANGE,
            TAG_SUBJECT_LOCATION,
            TAG_GPS_DIFFERENTIAL,
            TAG_THUMBNAIL_IMAGE_LENGTH,
            TAG_THUMBNAIL_IMAGE_WIDTH);


    @RequiresApi(api = Build.VERSION_CODES.N)
    private static final List<String> DOUBLE_TAGS_LIST_API_24 = Arrays.asList(
            TAG_DIGITAL_ZOOM_RATIO,
            TAG_EXPOSURE_BIAS_VALUE,
            TAG_F_NUMBER,
            TAG_SUBJECT_DISTANCE);
}