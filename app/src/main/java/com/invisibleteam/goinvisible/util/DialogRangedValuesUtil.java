package com.invisibleteam.goinvisible.util;

import android.annotation.SuppressLint;

import com.invisibleteam.goinvisible.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import static android.media.ExifInterface.*;

public class DialogRangedValuesUtil {

    @SuppressWarnings("unchecked")
    @Nullable
    public static Map<Integer, Integer> getTagsMapValues(String key) {
        switch (key) {
            case TAG_RESOLUTION_UNIT:
                /*
                    1 = No absolute unit of measurement. Used for images that may have a non-square aspect ratio, but no
                    meaningful absolute dimensions.
                    2 = Inch.
                    3 = Centimeter.
                */
                return getTagExposureTimeMap();

            case TAG_CONTRAST:
                /*
                   0 = Normal
                   1 = Soft
                   2 = Hard
                 */
                return getTagContrastMap();

            case TAG_EXPOSURE_MODE:
                /*
                   0 = Auto exposure
                   1 = Manual exposure
                   2 = Auto bracket
                 */
                return getTagExposureModeMap();

            case TAG_EXPOSURE_PROGRAM:
                /*
                    0 = Not defined
                    1 = Manual
                    2 = Normal program
                    3 = Aperture priority
                    4 = Shutter priority
                    5 = Creative program (biased toward depth of field)
                    6 = Action program (biased toward fast shutter speed)
                    7 = Portrait mode (for closeup photos with the background out of focus)
                    8 = Landscape mode (for landscape photos with the background in focus)
                 */
                return getTagExposureProgramMap();

            case TAG_GAIN_CONTROL:
                /*
                    0 = None
                    1 = Low gain up
                    2 = High gain up
                    3 = Low gain down
                    4 = High gain down
                 */
                return getTagGainControlMap();

            case TAG_METERING_MODE:
                /*
                    0 = Unknown
                    1 = Average
                    2 = CenterWeightedAverage
                    3 = Spot
                    4 = MultiSpot
                    5 = Pattern
                    6 = Partial
                    255 = other
                 */
                return getTagMeteringModeMap();

            case TAG_SATURATION:
                /*
                    0 = Normal
                    1 = Low saturation
                    2 = High saturation
                 */
                return getTagSaturationMap();

            case TAG_SCENE_CAPTURE_TYPE:
                /*
                    0 = Standard
                    1 = Landscape
                    2 = Portrait
                    3 = Night scene
                 */
                return getTagSceneCaptureTypeMap();

            case TAG_SHARPNESS:
                /*
                    0 = Normal
                    1 = Soft
                    2 = Hard
                 */
                return getTagSharpnessMap();

            case TAG_WHITE_BALANCE:
                /*
                    0 = Auto white balance
                    1 = Manual white balance
                 */
                return getTagWhiteBalanceMap();

            case TAG_FLASH:
                /*
                    hex 0000 = Flash did not fire
                    hex 0001 = Flash fired
                    hex 0005 = Strobe return light not detected
                    hex 0007 = Strobe return light detected
                    hex 0009 = Flash fired, compulsory flash mode
                    hex 000D = Flash fired, compulsory flash mode, return light not detected
                    hex 000F = Flash fired, compulsory flash mode, return light detected
                    hex 0010 = Flash did not fire, compulsory flash mode
                    hex 0018 = Flash did not fire, auto mode
                    hex 0019 = Flash fired, auto mode
                    hex 001D = Flash fired, auto mode, return light not detected
                    hex 001F = Flash fired, auto mode, return light detected
                    hex 0020 = No flash function
                    hex 0041 = Flash fired, red-eye reduction mode
                    hex 0045 = Flash fired, red-eye reduction mode, return light not detected
                    hex 0047 = Flash fired, red-eye reduction mode, return light detected
                    hex 0049 = Flash fired, compulsory flash mode, red-eye reduction mode
                    hex 004D = Flash fired, compulsory flash mode, red-eye reduction mode, return light not detected
                    hex 004F = Flash fired, compulsory flash mode, red-eye reduction mode, return light detected
                    hex 0059 = Flash fired, auto mode, red-eye reduction mode
                    hex 005D = Flash fired, auto mode, return light not detected, red-eye reduction mode
                    hex 005F = Flash fired, auto mode, return light detected, red-eye reduction mode
                 */
                return getTagFlashMap();

            case TAG_ORIENTATION:
            case TAG_GPS_ALTITUDE_REF:
            case TAG_GPS_LATITUDE_REF:
            case TAG_GPS_LONGITUDE_REF:
            case TAG_ISO_SPEED_RATINGS:
                return getDefaultMap();

            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> getTagExposureTimeMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_exposure_time_1, R.string.tag_value_1);
        map.put(R.string.tag_exposure_time_2, R.string.tag_value_2);
        map.put(R.string.tag_exposure_time_3, R.string.tag_value_3);

        return Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> getTagContrastMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_contrast_1, R.string.tag_value_1);
        map.put(R.string.tag_contrast_2, R.string.tag_value_2);
        map.put(R.string.tag_contrast_3, R.string.tag_value_3);

        return Collections.unmodifiableMap(map);
    }

    private static Map<Integer, Integer> getTagExposureModeMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_exposure_mode_0, R.string.tag_value_0);
        map.put(R.string.tag_exposure_mode_1, R.string.tag_value_1);
        map.put(R.string.tag_exposure_mode_2, R.string.tag_value_2);

        return Collections.unmodifiableMap(map);
    }

    private static Map<Integer, Integer> getTagExposureProgramMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_exposure_program_0, R.string.tag_value_0);
        map.put(R.string.tag_exposure_program_1, R.string.tag_value_1);
        map.put(R.string.tag_exposure_program_2, R.string.tag_value_2);
        map.put(R.string.tag_exposure_program_3, R.string.tag_value_3);
        map.put(R.string.tag_exposure_program_4, R.string.tag_value_4);
        map.put(R.string.tag_exposure_program_5, R.string.tag_value_5);
        map.put(R.string.tag_exposure_program_6, R.string.tag_value_6);
        map.put(R.string.tag_exposure_program_7, R.string.tag_value_7);
        map.put(R.string.tag_exposure_program_8, R.string.tag_value_8);

        return Collections.unmodifiableMap(map);
    }

    private static Map<Integer, Integer> getTagGainControlMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_gain_control_0, R.string.tag_value_0);
        map.put(R.string.tag_gain_control_1, R.string.tag_value_1);
        map.put(R.string.tag_gain_control_2, R.string.tag_value_2);
        map.put(R.string.tag_gain_control_3, R.string.tag_value_3);
        map.put(R.string.tag_gain_control_4, R.string.tag_value_4);

        return Collections.unmodifiableMap(map);
    }

    private static Map<Integer, Integer> getTagMeteringModeMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_metering_mode_0, R.string.tag_value_0);
        map.put(R.string.tag_metering_mode_1, R.string.tag_value_1);
        map.put(R.string.tag_metering_mode_2, R.string.tag_value_2);
        map.put(R.string.tag_metering_mode_3, R.string.tag_value_3);
        map.put(R.string.tag_metering_mode_4, R.string.tag_value_4);
        map.put(R.string.tag_metering_mode_5, R.string.tag_value_5);
        map.put(R.string.tag_metering_mode_6, R.string.tag_value_6);
        map.put(R.string.tag_metering_mode_255, R.string.tag_value_255);

        return Collections.unmodifiableMap(map);
    }

    private static Map<Integer, Integer> getTagSaturationMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_saturation_0, R.string.tag_value_0);
        map.put(R.string.tag_saturation_1, R.string.tag_value_1);
        map.put(R.string.tag_saturation_2, R.string.tag_value_2);

        return Collections.unmodifiableMap(map);
    }

    private static Map<Integer, Integer> getTagSceneCaptureTypeMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_scene_capture_type_0, R.string.tag_value_0);
        map.put(R.string.tag_scene_capture_type_1, R.string.tag_value_1);
        map.put(R.string.tag_scene_capture_type_2, R.string.tag_value_2);
        map.put(R.string.tag_scene_capture_type_3, R.string.tag_value_3);

        return Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> getTagSharpnessMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_sharpness_1, R.string.tag_value_1);
        map.put(R.string.tag_sharpness_2, R.string.tag_value_2);
        map.put(R.string.tag_sharpness_3, R.string.tag_value_3);

        return Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> getTagWhiteBalanceMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_white_balance_0, R.string.tag_value_0);
        map.put(R.string.tag_white_balance_1, R.string.tag_value_1);

        return Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> getTagFlashMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_flash_0000, R.string.tag_value_0000);
        map.put(R.string.tag_flash_0001, R.string.tag_value_0001);
        map.put(R.string.tag_flash_0005, R.string.tag_value_0005);
        map.put(R.string.tag_flash_0007, R.string.tag_value_0007);
        map.put(R.string.tag_flash_0009, R.string.tag_value_0009);
        map.put(R.string.tag_flash_000D, R.string.tag_value_000D);
        map.put(R.string.tag_flash_000F, R.string.tag_value_000F);
        map.put(R.string.tag_flash_0010, R.string.tag_value_0010);
        map.put(R.string.tag_flash_0018, R.string.tag_value_0018);
        map.put(R.string.tag_flash_0019, R.string.tag_value_0019);
        map.put(R.string.tag_flash_001D, R.string.tag_value_001D);
        map.put(R.string.tag_flash_001F, R.string.tag_value_001F);
        map.put(R.string.tag_flash_0020, R.string.tag_value_0020);
        map.put(R.string.tag_flash_0041, R.string.tag_value_0041);
        map.put(R.string.tag_flash_0045, R.string.tag_value_0045);
        map.put(R.string.tag_flash_0047, R.string.tag_value_0047);
        map.put(R.string.tag_flash_0049, R.string.tag_value_0049);
        map.put(R.string.tag_flash_004D, R.string.tag_value_004D);
        map.put(R.string.tag_flash_004F, R.string.tag_value_004F);
        map.put(R.string.tag_flash_0059, R.string.tag_value_0059);
        map.put(R.string.tag_flash_005D, R.string.tag_value_005D);
        map.put(R.string.tag_flash_005F, R.string.tag_value_005F);

        return Collections.unmodifiableMap(map);
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, Integer> getDefaultMap() {
        @SuppressLint("UseSparseArrays") Map<Integer, Integer> map = new HashMap<>();
        map.put(R.string.tag_value_0, R.string.tag_value_0);
        map.put(R.string.tag_value_1, R.string.tag_value_1);

        return Collections.unmodifiableMap(map);
    }
}
