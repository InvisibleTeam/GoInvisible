package com.invisibleteam.goinvisible.model;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;

import org.junit.runner.RunWith;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.invisibleteam.goinvisible.model.TagType.REGEXP_DATETIME_STRING;
import static com.invisibleteam.goinvisible.model.TagType.REGEXP_DATE_STRING;
import static com.invisibleteam.goinvisible.model.TagType.REGEXP_TEXT_STRING;
import static com.invisibleteam.goinvisible.model.TagType.REGEXP_TIMESTAMP_STRING;
import static com.invisibleteam.goinvisible.model.TagType.REGEXP_VALUE_DOUBLE;
import static com.invisibleteam.goinvisible.model.TagType.REGEXP_VALUE_INTEGER;
import static junit.framework.Assert.assertEquals;

@RunWith(ZohhakRunner.class)
public class TagTypeTest {

    @TestWith({
            "123,       true",
            "0,         true",
            "001,       false",
            "1.0,       false",
            "1234567,   false"
    })
    public void whenValueIntegerRegularExpressionVerifiesText_ReturningCorrectValues(String text, boolean result) {
        assertEquals(result, IsMatch(text, REGEXP_VALUE_INTEGER));
    }

    @TestWith({
            "123,       true",
            "0,         true",
            "1.0,       true",
            "1.01,      true",
            "01.0,      false",
            "1234567.0, false",
            "0.1234567, false"
    })
    public void whenValueDoubleRegularExpressionVerifiesText_ReturningCorrectValues(String text, boolean result) {
        assertEquals(result, IsMatch(text, REGEXP_VALUE_DOUBLE));
    }

    @TestWith({
            "AnyText-NoRestrictions,       true"
    })
    public void whenTextStringRegularExpressionVerifiesText_ReturningCorrectValues(String text, boolean result) {
        assertEquals(result, IsMatch(text, REGEXP_TEXT_STRING));
    }

    @TestWith({
            "0000-01-01,       true",
            "9999-01-01,       true",
            "9999-00-00,       false",
            "19999-00-00,      false",
            "9999-13-00,       false",
            "9999-12-32,       false",
            "2000:12:22,       false",
    })
    public void whenDateStringRegularExpressionVerifiesText_ReturningCorrectValues(String text, boolean result) {
        assertEquals(result, IsMatch(text, REGEXP_DATE_STRING));
    }

    @TestWith({
            "00:00:00,       true",
            "23:59:59,       true",
            "24:00:00,       false",
            "13:60:60,       false"
    })
    public void whenTimeStampStringRegularExpressionVerifiesText_ReturningCorrectValues(String text, boolean result) {
        assertEquals(result, IsMatch(text, REGEXP_TIMESTAMP_STRING));
    }

    @TestWith({
            "2004-11-12 00:00:00,       true",
            "2004-11-12 24:00:00,       false",
            "12004-11-12 00:00:00,      false",
            "2004-13-12 00:00:00,       false",
    })
    public void whenDateTimeStringRegularExpressionVerifiesText_ReturningCorrectValues(String text, boolean result) {
        assertEquals(result, IsMatch(text, REGEXP_DATETIME_STRING));
    }

    private static boolean IsMatch(String s, String pattern) {
        try {
            Pattern patt = Pattern.compile(pattern);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

}