package com.invisibleteam.goinvisible.util;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import static com.invisibleteam.goinvisible.model.InputType.DATETIME_STRING;
import static com.invisibleteam.goinvisible.model.InputType.DATE_STRING;
import static com.invisibleteam.goinvisible.model.InputType.TIMESTAMP_STRING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DateFormatterUtilTest {

    private Calendar calendar;

    @Before
    public void setUp() {
        calendar = Calendar.getInstance();
    }

    @Test
    public void whenInputTypeIsDateString_TextIsProperlyFormatted() {
        //Given
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 21);
        Tag tag = createTag(DATE_STRING);

        //When
        String formatedDate = DateFormatterUtil.format(tag, calendar);

        //Then
        assertThat(formatedDate, is("2017-06-21"));
    }

    @Test
    public void whenInputTypeIsTimestampString_TextIsProperlyFormatted() {
        //Given
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 21);
        Tag tag = createTag(TIMESTAMP_STRING);

        //When
        String formatedDate = DateFormatterUtil.format(tag, calendar);

        //Then
        assertThat(formatedDate, is("15:45:21"));
    }

    @Test
    public void whenInputTypeIsDatetimeString_TextIsProperlyFormatted() {
        //Given
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 5);
        calendar.set(Calendar.DAY_OF_MONTH, 21);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 21);
        Tag tag = createTag(DATETIME_STRING);

        //When
        String formatedDate = DateFormatterUtil.format(tag, calendar);

        //Then
        assertThat(formatedDate, is("2017-06-21 15:45:21"));
    }

    private Tag createTag(InputType textString) {
        TagType tagType = TagType.build(textString);
        return new Tag("key", "value", tagType);
    }
}
