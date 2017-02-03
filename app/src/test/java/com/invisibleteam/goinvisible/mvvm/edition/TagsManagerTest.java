package com.invisibleteam.goinvisible.mvvm.edition;

import android.media.ExifInterface;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static android.media.ExifInterface.TAG_DATETIME;
import static android.media.ExifInterface.TAG_EXPOSURE_TIME;
import static android.media.ExifInterface.TAG_FLASH;
import static android.media.ExifInterface.TAG_GPS_DATESTAMP;
import static android.media.ExifInterface.TAG_GPS_LATITUDE_REF;
import static android.media.ExifInterface.TAG_GPS_TIMESTAMP;
import static android.media.ExifInterface.TAG_MAKE;
import static android.media.ExifInterface.TAG_MODEL;
import static android.media.ExifInterface.TAG_WHITE_BALANCE;
import static com.invisibleteam.goinvisible.util.TagsMatcher.containsTag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagsManagerTest {

    @Mock
    private ExifInterface exifInterface;

    private TagsManager tagsManager;

    @Before
    public void setUp() {
        tagsManager = new TagsManager(exifInterface);
    }

    @Test
    public void whenThereIsNoImageAtPath_GetAttributeIsCalled() throws IOException {
        //When
        tagsManager.getAllTags();

        //Then
        verify(exifInterface).getAttribute(TAG_MODEL);
    }

    @Test
    public void whenThereAreSomeTags_ProperTagListIsCreated() throws IOException {
        //Given
        when(exifInterface.getAttribute(TAG_MAKE)).thenReturn("tag1");
        when(exifInterface.getAttribute(TAG_MODEL)).thenReturn("tag2");

        //When
        List<Tag> tagList = tagsManager.getAllTags();

        //Then
        assertThat(tagList, containsTag(new Tag(TAG_MAKE, "tag1", TagType.build(InputType.TEXT_STRING))));
        assertThat(tagList, containsTag(new Tag(TAG_MODEL, "tag2", TagType.build(InputType.TEXT_STRING))));
    }

    @Test
    public void whenTimestampTagIsCleared_DefaultValueIsReturned() throws IOException {
        //When
        tagsManager.clearTag(new Tag(TAG_GPS_TIMESTAMP, "13:53:20", TagType.build(InputType.TIMESTAMP_STRING)));

        //Then
        verify(exifInterface).setAttribute(TAG_GPS_TIMESTAMP, "00:00:00");
        verify(exifInterface).saveAttributes();
    }

    @Test
    public void whenStringTagIsCleared_DefaultValueIsReturned() throws IOException {
        //When
        tagsManager.clearTag(new Tag(TAG_MAKE, "text", TagType.build(InputType.TEXT_STRING)));

        //Then
        verify(exifInterface).setAttribute(TAG_MAKE, "");
        verify(exifInterface).saveAttributes();
    }

    @Test
    public void whenIntegerValueTagIsCleared_DefaultValueIsReturned() throws IOException {
        //When
        tagsManager.clearTag(new Tag(TAG_FLASH, "15", TagType.build(InputType.VALUE_INTEGER)));

        //Then
        verify(exifInterface).setAttribute(TAG_FLASH, "0");
        verify(exifInterface).saveAttributes();
    }

    @Test
    public void whenDoubleValueTagIsCleared_DefaultValueIsReturned() throws IOException {
        //When
        tagsManager.clearTag(new Tag(TAG_EXPOSURE_TIME, "30.5", TagType.build(InputType.VALUE_DOUBLE)));

        //Then
        verify(exifInterface).setAttribute(TAG_EXPOSURE_TIME, "0.0");
        verify(exifInterface).saveAttributes();
    }

    @Test
    public void whenDateTimeStringTagIsCleared_DefaultValueIsReturned() throws IOException {
        //When
        tagsManager.clearTag(new Tag(TAG_DATETIME, "2015-12-12 12:55:00", TagType.build(InputType.DATETIME_STRING)));

        //Then
        verify(exifInterface).setAttribute(TAG_DATETIME, "2001-01-01 00:00:00");
        verify(exifInterface).saveAttributes();
    }

    @Test
    public void whenDateStringTagIsCleared_DefaultValueIsReturned() throws IOException {
        //When
        tagsManager.clearTag(new Tag(TAG_GPS_DATESTAMP, "2015-12-12", TagType.build(InputType.DATE_STRING)));

        //Then
        verify(exifInterface).setAttribute(TAG_GPS_DATESTAMP, "2001-01-01");
        verify(exifInterface).saveAttributes();
    }

    @Test
    public void whenRangedStringTagIsCleared_DefaultValueIsReturned() throws IOException {
        //When
        tagsManager.clearTag(new Tag(TAG_GPS_LATITUDE_REF, "35.09234", TagType.build(InputType.RANGED_STRING)));

        //Then
        verify(exifInterface).setAttribute(TAG_GPS_LATITUDE_REF, "0.00000");
        verify(exifInterface).saveAttributes();
    }

    @Test
    public void whenRangedIntegerTagIsCleared_DefaultValueIsReturned() throws IOException {
        //When
        tagsManager.clearTag(new Tag(TAG_WHITE_BALANCE, "1", TagType.build(InputType.RANGED_INTEGER)));

        //Then
        verify(exifInterface).setAttribute(TAG_WHITE_BALANCE, "0");
        verify(exifInterface).saveAttributes();
    }
}
