package com.invisibleteam.goinvisible.mvvm.edition;

import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.VisibleForTesting;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.media.ExifInterface.*;

class TagsProvider {

    private final ImageDetails imageDetails;

    TagsProvider(ImageDetails imageDetails) {
        this.imageDetails = imageDetails;
    }

    List<Tag> getAllTags() {
        try {
            ExifInterface exif = new ExifInterface(imageDetails.getPath());
            return getAllTags(exif);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    List<Tag> getAllTags(ExifInterface exif) {
        List<Tag> tagsList = new ArrayList<>();
        tagsList.addAll(getAllTags(exif, STRING_TAGS_LIST));
        tagsList.addAll(getAllTags(exif, INTEGER_TAGS_LIST));
        tagsList.addAll(getAllTags(exif, DOUBLE_TAGS_LIST));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tagsList.addAll(getAllTags(exif, STRING_TAGS_LIST_API_24));
            tagsList.addAll(getAllTags(exif, INTEGER_TAGS_LIST_API_24));
            tagsList.addAll(getAllTags(exif, DOUBLE_TAGS_LIST_API_24));
        }
        return tagsList;
    }

    private List<Tag> getAllTags(ExifInterface exif, List<String> keysList) {
        List<Tag> tagsList = new ArrayList<>();
        Object tagValue;
        String tagKey;
        for (int index = 0; index < keysList.size(); index++) {
            tagKey = keysList.get(index);
            tagValue = exif.getAttribute(tagKey);

            if (tagValue != null) {
                tagsList.add(new Tag(tagKey, tagValue.toString()));
            }
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
            TAG_GPS_DEST_BEARING_REF,
            TAG_GPS_DEST_DISTANCE_REF,
            TAG_GPS_DEST_LATITUDE_REF,
            TAG_GPS_DEST_LONGITUDE_REF,
            TAG_GPS_IMG_DIRECTION_REF,
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
            TAG_FOCAL_LENGTH,
            TAG_GPS_LATITUDE,
            TAG_GPS_ALTITUDE,
            TAG_GPS_LONGITUDE,
            TAG_EXPOSURE_TIME);

    private static final List<String> DOUBLE_TAGS_LIST_API_24 = Arrays.asList(
            TAG_DIGITAL_ZOOM_RATIO,
            TAG_EXPOSURE_BIAS_VALUE,
            TAG_F_NUMBER,
            TAG_SUBJECT_DISTANCE,
            TAG_PRIMARY_CHROMATICITIES,
            TAG_REFERENCE_BLACK_WHITE,
            TAG_WHITE_POINT,
            TAG_X_RESOLUTION,
            TAG_Y_CB_CR_COEFFICIENTS,
            TAG_Y_RESOLUTION,
            TAG_APERTURE_VALUE,
            TAG_BRIGHTNESS_VALUE,
            TAG_COMPRESSED_BITS_PER_PIXEL,
            TAG_EXPOSURE_INDEX,
            TAG_FLASH_ENERGY,
            TAG_FOCAL_PLANE_X_RESOLUTION,
            TAG_FOCAL_PLANE_Y_RESOLUTION,
            TAG_MAX_APERTURE_VALUE,
            TAG_GPS_IMG_DIRECTION,
            TAG_GPS_SPEED,
            TAG_GPS_TRACK,
            TAG_GPS_DOP,
            TAG_GPS_DEST_BEARING,
            TAG_GPS_DEST_DISTANCE,
            TAG_GPS_DEST_LATITUDE,
            TAG_GPS_DEST_LONGITUDE,
            TAG_SHUTTER_SPEED_VALUE);
}