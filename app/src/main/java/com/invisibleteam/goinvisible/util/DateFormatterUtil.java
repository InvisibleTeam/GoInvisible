package com.invisibleteam.goinvisible.util;

import android.text.format.DateFormat;

import com.invisibleteam.goinvisible.model.Tag;

import java.util.Calendar;

public class DateFormatterUtil {

    private static final String DATE_STRING_FORMATTER = "yyyy-MM-dd";
    private static final String TIMESTAMP_STRING_FORMATTER = "hh:mm:ss";
    private static final String DATETIME_STRING_FORMATTER = DATE_STRING_FORMATTER + " " + TIMESTAMP_STRING_FORMATTER;
    private static final String EMPTY_FORMATTER = "";

    public static String format(Tag tag, Calendar calendar) {
        return DateFormat
                .format(getFormatter(tag), calendar)
                .toString();
    }

    private static String getFormatter(Tag tag) {

        switch (tag.getTagType().getInputType()) {
            case DATE_STRING:
                return DATE_STRING_FORMATTER;

            case TIMESTAMP_STRING:
                return TIMESTAMP_STRING_FORMATTER;

            case DATETIME_STRING:
                return DATETIME_STRING_FORMATTER;

            default:
                return EMPTY_FORMATTER;

        }
    }

}
