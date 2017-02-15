package com.invisibleteam.goinvisible.util;

import javax.annotation.Nullable;

import static android.support.media.ExifInterface.*;

public class DialogRangedValuesUtil {

    @Nullable
    public static String[] getValues(String key) {
        switch (key) {
            case TAG_RESOLUTION_UNIT:
            case TAG_CONTRAST:
            case TAG_EXPOSURE_MODE:
            case TAG_EXPOSURE_PROGRAM:
            case TAG_GAIN_CONTROL:
            case TAG_ISO_SPEED_RATINGS:
            case TAG_METERING_MODE:
            case TAG_SATURATION:
            case TAG_SCENE_CAPTURE_TYPE:
            case TAG_SHARPNESS:
            case TAG_WHITE_BALANCE:
            case TAG_GPS_ALTITUDE_REF:
            case TAG_FLASH:
            case TAG_ORIENTATION:
                return new String[]{"0", "1"};

            default:
                return null;
        }
    }
}
