package com.invisibleteam.goinvisible.util;

import javax.annotation.Nullable;

import static android.media.ExifInterface.*;

public class DialogRangedValuesUtil {

    @Nullable
    public static String[] getValues(String key) {
        switch (key) {
            case TAG_RESOLUTION_UNIT:
                /*
                    1 = No absolute unit of measurement. Used for images that may have a non-square aspect ratio, but no
                    meaningful absolute dimensions.
                    2 = Inch.
                    3 = Centimeter.
                */
                return new String[]{"1", "2", "3"};
            case TAG_CONTRAST:
                /*
                   0 = Normal
                   1 = Soft
                   2 = Hard
                 */
                return new String[]{"0", "1", "2"};
            case TAG_EXPOSURE_MODE:
                /*
                   0 = Auto exposure
                   1 = Manual exposure
                   2 = Auto bracket
                 */
                return new String[]{"0", "1", "2"};
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
                return new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
            case TAG_GAIN_CONTROL:
                /*
                    0 = None
                    1 = Low gain up
                    2 = High gain up
                    3 = Low gain down
                    4 = High gain down
                 */
                return new String[]{"0", "1", "2", "3", "4"};
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
                return new String[]{"0", "1", "2", "3", "4", "5", "6", "255"};
            case TAG_SATURATION:
                /*
                    0 = Normal
                    1 = Low saturation
                    2 = High saturation
                 */
                return new String[]{"0", "1", "2"};
            case TAG_SCENE_CAPTURE_TYPE:
                /*
                    0 = Standard
                    1 = Landscape
                    2 = Portrait
                    3 = Night scene
                 */
                return new String[]{"0", "1", "2", "3"};
            case TAG_SHARPNESS:
                /*
                    0 = Normal
                    1 = Soft
                    2 = Hard
                 */
                return new String[]{"0", "1", "2"};
            case TAG_WHITE_BALANCE:
                /*
                    0 = Auto white balance
                    1 = Manual white balance
                 */
                return new String[]{"0", "1"};
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
                return new String[]{
                        "hex 0000",
                        "hex 0001",
                        "hex 0005",
                        "hex 0007",
                        "hex 0009",
                        "hex 000D",
                        "hex 000F",
                        "hex 0010",
                        "hex 0018",
                        "hex 0019",
                        "hex 001D",
                        "hex 001F",
                        "hex 0020",
                        "hex 0041",
                        "hex 0045",
                        "hex 0047",
                        "hex 0049",
                        "hex 004D",
                        "hex 004F",
                        "hex 0059",
                        "hex 005D",
                        "hex 005F"};
            case TAG_ORIENTATION:
            case TAG_GPS_ALTITUDE_REF:
            case TAG_ISO_SPEED_RATINGS:
                return new String[]{"0", "1"};

            default:
                return null;
        }
    }
}
