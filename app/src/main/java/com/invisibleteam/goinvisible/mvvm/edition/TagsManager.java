package com.invisibleteam.goinvisible.mvvm.edition;

import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import com.invisibleteam.goinvisible.model.ObjectType;
import com.invisibleteam.goinvisible.model.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.media.ExifInterface.TAG_ARTIST;
import static android.media.ExifInterface.TAG_BITS_PER_SAMPLE;
import static android.media.ExifInterface.TAG_CFA_PATTERN;
import static android.media.ExifInterface.TAG_COLOR_SPACE;
import static android.media.ExifInterface.TAG_COMPONENTS_CONFIGURATION;
import static android.media.ExifInterface.TAG_COMPRESSION;
import static android.media.ExifInterface.TAG_CONTRAST;
import static android.media.ExifInterface.TAG_COPYRIGHT;
import static android.media.ExifInterface.TAG_CUSTOM_RENDERED;
import static android.media.ExifInterface.TAG_DATETIME;
import static android.media.ExifInterface.TAG_DATETIME_DIGITIZED;
import static android.media.ExifInterface.TAG_DATETIME_ORIGINAL;
import static android.media.ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION;
import static android.media.ExifInterface.TAG_DIGITAL_ZOOM_RATIO;
import static android.media.ExifInterface.TAG_EXIF_VERSION;
import static android.media.ExifInterface.TAG_EXPOSURE_BIAS_VALUE;
import static android.media.ExifInterface.TAG_EXPOSURE_MODE;
import static android.media.ExifInterface.TAG_EXPOSURE_PROGRAM;
import static android.media.ExifInterface.TAG_EXPOSURE_TIME;
import static android.media.ExifInterface.TAG_FILE_SOURCE;
import static android.media.ExifInterface.TAG_FLASH;
import static android.media.ExifInterface.TAG_FLASHPIX_VERSION;
import static android.media.ExifInterface.TAG_FOCAL_LENGTH_IN_35MM_FILM;
import static android.media.ExifInterface.TAG_FOCAL_PLANE_RESOLUTION_UNIT;
import static android.media.ExifInterface.TAG_F_NUMBER;
import static android.media.ExifInterface.TAG_GAIN_CONTROL;
import static android.media.ExifInterface.TAG_GPS_ALTITUDE_REF;
import static android.media.ExifInterface.TAG_GPS_AREA_INFORMATION;
import static android.media.ExifInterface.TAG_GPS_DATESTAMP;
import static android.media.ExifInterface.TAG_GPS_DEST_DISTANCE_REF;
import static android.media.ExifInterface.TAG_GPS_DEST_LATITUDE_REF;
import static android.media.ExifInterface.TAG_GPS_DEST_LONGITUDE_REF;
import static android.media.ExifInterface.TAG_GPS_DIFFERENTIAL;
import static android.media.ExifInterface.TAG_GPS_LATITUDE_REF;
import static android.media.ExifInterface.TAG_GPS_LONGITUDE_REF;
import static android.media.ExifInterface.TAG_GPS_MAP_DATUM;
import static android.media.ExifInterface.TAG_GPS_MEASURE_MODE;
import static android.media.ExifInterface.TAG_GPS_PROCESSING_METHOD;
import static android.media.ExifInterface.TAG_GPS_SATELLITES;
import static android.media.ExifInterface.TAG_GPS_SPEED_REF;
import static android.media.ExifInterface.TAG_GPS_STATUS;
import static android.media.ExifInterface.TAG_GPS_TIMESTAMP;
import static android.media.ExifInterface.TAG_GPS_TRACK_REF;
import static android.media.ExifInterface.TAG_GPS_VERSION_ID;
import static android.media.ExifInterface.TAG_IMAGE_DESCRIPTION;
import static android.media.ExifInterface.TAG_IMAGE_LENGTH;
import static android.media.ExifInterface.TAG_IMAGE_UNIQUE_ID;
import static android.media.ExifInterface.TAG_IMAGE_WIDTH;
import static android.media.ExifInterface.TAG_INTEROPERABILITY_INDEX;
import static android.media.ExifInterface.TAG_ISO_SPEED_RATINGS;
import static android.media.ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT;
import static android.media.ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH;
import static android.media.ExifInterface.TAG_LIGHT_SOURCE;
import static android.media.ExifInterface.TAG_MAKE;
import static android.media.ExifInterface.TAG_MAKER_NOTE;
import static android.media.ExifInterface.TAG_METERING_MODE;
import static android.media.ExifInterface.TAG_MODEL;
import static android.media.ExifInterface.TAG_OECF;
import static android.media.ExifInterface.TAG_ORIENTATION;
import static android.media.ExifInterface.TAG_PHOTOMETRIC_INTERPRETATION;
import static android.media.ExifInterface.TAG_PIXEL_X_DIMENSION;
import static android.media.ExifInterface.TAG_PIXEL_Y_DIMENSION;
import static android.media.ExifInterface.TAG_PLANAR_CONFIGURATION;
import static android.media.ExifInterface.TAG_RELATED_SOUND_FILE;
import static android.media.ExifInterface.TAG_RESOLUTION_UNIT;
import static android.media.ExifInterface.TAG_ROWS_PER_STRIP;
import static android.media.ExifInterface.TAG_SAMPLES_PER_PIXEL;
import static android.media.ExifInterface.TAG_SATURATION;
import static android.media.ExifInterface.TAG_SCENE_CAPTURE_TYPE;
import static android.media.ExifInterface.TAG_SCENE_TYPE;
import static android.media.ExifInterface.TAG_SENSING_METHOD;
import static android.media.ExifInterface.TAG_SHARPNESS;
import static android.media.ExifInterface.TAG_SOFTWARE;
import static android.media.ExifInterface.TAG_SPATIAL_FREQUENCY_RESPONSE;
import static android.media.ExifInterface.TAG_SPECTRAL_SENSITIVITY;
import static android.media.ExifInterface.TAG_STRIP_BYTE_COUNTS;
import static android.media.ExifInterface.TAG_STRIP_OFFSETS;
import static android.media.ExifInterface.TAG_SUBJECT_AREA;
import static android.media.ExifInterface.TAG_SUBJECT_DISTANCE;
import static android.media.ExifInterface.TAG_SUBJECT_DISTANCE_RANGE;
import static android.media.ExifInterface.TAG_SUBJECT_LOCATION;
import static android.media.ExifInterface.TAG_SUBSEC_TIME;
import static android.media.ExifInterface.TAG_SUBSEC_TIME_DIGITIZED;
import static android.media.ExifInterface.TAG_SUBSEC_TIME_ORIGINAL;
import static android.media.ExifInterface.TAG_THUMBNAIL_IMAGE_LENGTH;
import static android.media.ExifInterface.TAG_THUMBNAIL_IMAGE_WIDTH;
import static android.media.ExifInterface.TAG_TRANSFER_FUNCTION;
import static android.media.ExifInterface.TAG_USER_COMMENT;
import static android.media.ExifInterface.TAG_WHITE_BALANCE;
import static android.media.ExifInterface.TAG_Y_CB_CR_POSITIONING;
import static android.media.ExifInterface.TAG_Y_CB_CR_SUB_SAMPLING;
import static com.invisibleteam.goinvisible.model.ObjectType.DOUBLE;
import static com.invisibleteam.goinvisible.model.ObjectType.INTEGER;
import static com.invisibleteam.goinvisible.model.ObjectType.STRING;

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
        if (tag.getKey().equals(TAG_GPS_TIMESTAMP)) {
            tag.setValue("00:00:00");
        } else {
            switch (tag.getObjectType()) {
                case STRING:
                    tag.setValue("");
                    break;
                case INTEGER:
                case DOUBLE:
                    tag.setValue("0");
            }
        }
        return editTag(tag);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Tag> getAllTags() {
        List<Tag> tagsList = new ArrayList<>();
        tagsList.addAll(getAllTags(exifInterface, STRING_TAGS_LIST, STRING));
        tagsList.addAll(getAllTags(exifInterface, INTEGER_TAGS_LIST, INTEGER));
        tagsList.addAll(getAllTags(exifInterface, DOUBLE_TAGS_LIST, DOUBLE));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tagsList.addAll(getAllTags(exifInterface, STRING_TAGS_LIST_API_24, STRING));
            tagsList.addAll(getAllTags(exifInterface, INTEGER_TAGS_LIST_API_24, INTEGER));
            tagsList.addAll(getAllTags(exifInterface, DOUBLE_TAGS_LIST_API_24, DOUBLE));
        }
        return tagsList;
    }

    private List<Tag> getAllTags(ExifInterface exif, List<String> keysList, ObjectType objectType) {
        List<Tag> tagsList = new ArrayList<>();
        Object tagValue;
        String tagKey;
        for (int index = 0; index < keysList.size(); index++) {
            tagKey = keysList.get(index);

            switch (objectType) {
                case STRING:
                    tagValue = exif.getAttribute(tagKey);
                    if (TextUtils.isEmpty((String) tagValue)) {
                        continue;
                    }
                    break;
                case INTEGER:
                    tagValue = exif.getAttributeInt(tagKey, 0);
                    break;
                case DOUBLE:
                    tagValue = exif.getAttributeDouble(tagKey, 0);
                    break;
                default:
                    continue;
            }

            tagsList.add(new Tag(tagKey, String.valueOf(tagValue), objectType));
        }
        return tagsList;
    }

    private static final List<String> STRING_TAGS_LIST = Arrays.asList(
            TAG_GPS_DATESTAMP,
            TAG_GPS_LATITUDE_REF,
            TAG_GPS_TIMESTAMP,
            TAG_GPS_LONGITUDE_REF,
            TAG_GPS_PROCESSING_METHOD,
            TAG_DATETIME,
            TAG_MAKE,
            TAG_MODEL);

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


    private static final List<String> INTEGER_TAGS_LIST = Arrays.asList(
            TAG_IMAGE_LENGTH,
            TAG_WHITE_BALANCE,
            TAG_GPS_ALTITUDE_REF,
            TAG_FLASH,
            TAG_IMAGE_WIDTH,
            TAG_ORIENTATION);

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

    private static final List<String> DOUBLE_TAGS_LIST = Arrays.asList(
            TAG_EXPOSURE_TIME);

    private static final List<String> DOUBLE_TAGS_LIST_API_24 = Arrays.asList(
            TAG_DIGITAL_ZOOM_RATIO,
            TAG_EXPOSURE_BIAS_VALUE,
            TAG_F_NUMBER,
            TAG_SUBJECT_DISTANCE);
}