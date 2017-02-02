package com.invisibleteam.goinvisible.mvvm.edition;

import android.media.ExifInterface;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import static android.media.ExifInterface.TAG_EXPOSURE_TIME;
import static android.media.ExifInterface.TAG_FLASH;
import static android.media.ExifInterface.TAG_GPS_TIMESTAMP;
import static android.media.ExifInterface.TAG_MAKE;
import static android.media.ExifInterface.TAG_MODEL;
import static com.invisibleteam.goinvisible.util.TagsMatcher.containsTag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TagsManagerTest {

    @Test
    public void whenThereIsNoImageAtPath_GetAttributeIsCalled() throws IOException {
        //Given
        ExifInterface exifInterface = mock(ExifInterface.class);
        TagsManager provider = new TagsManager(exifInterface);

        //When
        provider.getAllTags();

        //Then
        verify(exifInterface).getAttribute(TAG_MODEL);
    }

    @Test
    public void whenThereAreSomeTags_ProperTagListIsCreated() throws IOException {
        //Given
        ExifInterface exifInterface = mock(ExifInterface.class);
        when(exifInterface.getAttribute(TAG_MAKE)).thenReturn("tag1");
        when(exifInterface.getAttribute(TAG_MODEL)).thenReturn("tag2");
        TagsManager provider = new TagsManager(exifInterface);

        //When
        List<Tag> tagList = provider.getAllTags();

        //Then
        assertThat(tagList, containsTag(new Tag(TAG_MAKE, "tag1", TagType.build(InputType.TEXT_STRING))));
        assertThat(tagList, containsTag(new Tag(TAG_MODEL, "tag2", TagType.build(InputType.TEXT_STRING))));
    }

    @Test
    public void whenTimeStampTagIsCleared_DefaultValueIsReturned() throws IOException {
        //Given
        ExifInterface exifInterface = mock(ExifInterface.class);
        TagsManager tagsManager = new TagsManager(exifInterface);

        //When
        tagsManager.clearTag(new Tag(TAG_GPS_TIMESTAMP, "", TagType.build(InputType.TIMESTAMP_STRING)));

        //Then
        verify(exifInterface).setAttribute(TAG_GPS_TIMESTAMP, "00:00:00");
        verify(exifInterface).saveAttributes();
    }

    @Test
    public void whenStringTagIsCleared_DefaultValueIsReturned() throws IOException {
        //Given
        ExifInterface exifInterface = mock(ExifInterface.class);
        TagsManager tagsManager = new TagsManager(exifInterface);

        //When
        tagsManager.clearTag(new Tag(TAG_MAKE, "test", TagType.build(InputType.TEXT_STRING)));

        //Then
        verify(exifInterface).setAttribute(TAG_MAKE, "");
        verify(exifInterface).saveAttributes();
    }

    @Test
    public void whenIntegerTagIsCleared_DefaultValueIsReturned() throws IOException {
        //Given
        ExifInterface exifInterface = mock(ExifInterface.class);
        TagsManager tagsManager = new TagsManager(exifInterface);

        //When
        tagsManager.clearTag(new Tag(TAG_FLASH, "test", TagType.build(InputType.VALUE_INTEGER)));

        //Then
        verify(exifInterface).setAttribute(TAG_FLASH, "0");
        verify(exifInterface).saveAttributes();
    }

    @Test
    public void whenDoubleTagIsCleared_DefaultValueIsReturned() throws IOException {
        //Given
        ExifInterface exifInterface = mock(ExifInterface.class);
        TagsManager tagsManager = new TagsManager(exifInterface);

        //When
        tagsManager.clearTag(new Tag(TAG_EXPOSURE_TIME, "test", TagType.build(InputType.VALUE_DOUBLE)));

        //Then
        verify(exifInterface).setAttribute(TAG_EXPOSURE_TIME, "0");
        verify(exifInterface).saveAttributes();
    }
}
