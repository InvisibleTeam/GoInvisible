package com.invisibleteam.goinvisible.util;


import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

public class TagUtil {

    private static final String GEO_MINUTES_CHAR = "\'";
    private static final String GEO_SECONDS_CHAR = "\'\'";
    private static final String DEFAULT_DENOMINATOR = "/1,";
    private static final String DENOMINATOR_CHAR = "/";
    private static final int MAX_DENOMINATOR_VALUE = 1000000;
    private static final int SIX_DECIMAL_NUMBER_SCALE = 6;
    private static final double DEFAULT_DOUBLE_VALUE = 0.0;

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    static final char DEGREE_CHAR = (char) 0x00B0;

    static String parseDoubleGPSToGeoGPS(double value) {
        final StringBuilder geoGPSBuilder = buildGeoGPSValueFromDoubleValue(value);
        return geoGPSBuilder.toString();
    }

    private static StringBuilder buildGeoGPSValueFromDoubleValue(double value) {
        final int[] geoGPS = convertDoubleGPSValueToGeoGPSValue(value);

        int degrees = geoGPS[0];
        int minutes = geoGPS[1];
        int seconds = geoGPS[2];

        return new StringBuilder()
                .append(degrees)
                .append(DEGREE_CHAR)
                .append(" ")
                .append(minutes)
                .append(GEO_MINUTES_CHAR)
                .append(" ")
                .append(seconds)
                .append(GEO_SECONDS_CHAR);
    }

    /**
     * Convert double GPS value to geo GPS value.
     *
     * @param value of GPS represented by double
     * @return an int array with geo GPS components:
     * 0 - degrees value
     * 1 - minutes value
     * 2 - seconds value
     */
    private static int[] convertDoubleGPSValueToGeoGPSValue(double value) {
        final int[] result = new int[3];
        result[0] = (int) value;
        result[1] = (int) (TimeUnit.HOURS.toMinutes(1) * (value - result[0]));
        result[2] = (int) ((TimeUnit.HOURS.toSeconds(1) * (value - result[0])) - (TimeUnit.HOURS.toMinutes(1) * result[1]));

        return result;
    }

    @Nullable
    public static String parseRationalGPSToGeoGPS(@Nullable String rational, @Nullable String rationalRef) {
        if (isRationalValueCorrect(rational)) {
            return null;
        }

        final double rationalGPS = parseRationalGPSToDoubleGPS(rational);
        final StringBuilder geoGPSBuilder = buildGeoGPSValueFromDoubleValue(rationalGPS);

        if (rationalRef != null) {
            geoGPSBuilder
                    .append(" ")
                    .append(rationalRef);
        } else {
            geoGPSBuilder.append("");
        }

        return geoGPSBuilder.toString();
    }

    public static double parseRationalGPSToDoubleGPS(String rational) {
        String[] rationalParts = rational.split(",");

        double degrees = parseRationalValueToDouble(rationalParts[0]);
        double minutes = parseRationalValueToDouble(rationalParts[1]);
        minutes /= TimeUnit.HOURS.toMinutes(1);
        double seconds = parseRationalValueToDouble(rationalParts[2]);
        seconds /= TimeUnit.HOURS.toSeconds(1);

        double result = degrees + minutes + seconds;

        return new BigDecimal(result).setScale(SIX_DECIMAL_NUMBER_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double parseRationalValueToDouble(@Nullable String rational) {
        if (rational == null) {
            return DEFAULT_DOUBLE_VALUE;
        }

        String[] rationals = rational.split("/");

        return Double.parseDouble(rationals[0]) / Double.parseDouble(rationals[1]);
    }

    public static String parseDoubleGPSToRationalGPS(double value) {
        long degrees = (long) value;
        long minutes = (long) (TimeUnit.HOURS.toMinutes(1) * (value - degrees));
        double seconds = Math.abs((TimeUnit.HOURS.toSeconds(1) * (value - degrees)) - (TimeUnit.HOURS.toMinutes(1) * minutes));

        long exactSecondsValue = parseDoubleToLong(seconds);

        return new StringBuilder()
                .append(degrees)
                .append(DEFAULT_DENOMINATOR)
                .append(minutes)
                .append(DEFAULT_DENOMINATOR)
                .append(exactSecondsValue)
                .append(DENOMINATOR_CHAR)
                .append(MAX_DENOMINATOR_VALUE)
                .toString();
    }

    private static long parseDoubleToLong(double value) {
        BigDecimal big = new BigDecimal(value).setScale(SIX_DECIMAL_NUMBER_SCALE, BigDecimal.ROUND_HALF_DOWN);
        String bigString = big.toString().replace(".", "");
        return Long.parseLong(bigString);
    }

    @Nullable
    static String parseRationalToString(@Nullable String rational) {
        if (isRationalValueCorrect(rational)) {
            return null;
        }

        double result = parseRationalValueToDouble(rational);

        return String.valueOf(result);
    }

    public static String parseDoubleValueToRational(double value) {
        long numerator = parseDoubleToLong(value);

        return new StringBuilder()
                .append(numerator)
                .append(DENOMINATOR_CHAR)
                .append(MAX_DENOMINATOR_VALUE)
                .toString();
    }

    private static boolean isRationalValueCorrect(@Nullable String rational) {
        return TextUtils.isEmpty(rational) || !rational.contains("/");
    }

}
