package com.invisibleteam.goinvisible.model;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;

import org.junit.runner.RunWith;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.invisibleteam.goinvisible.model.TagType.REGEXP_TEXT_STRING;
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
    public void whenValueIntegerRegularExpressionVerifiesText_CorrectValuesAreReturned(String text, boolean result) {
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
    public void whenValueDoubleRegularExpressionVerifiesText_CorrectValuesAreReturned(String text, boolean result) {
        assertEquals(result, IsMatch(text, REGEXP_VALUE_DOUBLE));
    }

    @TestWith({
            "AnyText-NoRestrictions,       true"
    })
    public void whenTextStringRegularExpressionVerifiesText_CorrectValuesAreReturned(String text, boolean result) {
        assertEquals(result, IsMatch(text, REGEXP_TEXT_STRING));
    }

    private static boolean IsMatch(String text, String pattern) {
        try {
            Pattern patternObject = Pattern.compile(pattern);
            Matcher matcher = patternObject.matcher(text);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

}