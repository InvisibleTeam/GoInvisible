package com.invisibleteam.goinvisible.mvvm.edition;

import android.media.ExifInterface;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ObjectType;
import com.invisibleteam.goinvisible.model.Tag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import static android.media.ExifInterface.TAG_MAKE;
import static android.media.ExifInterface.TAG_MODEL;
import static com.invisibleteam.goinvisible.util.TagsMatcher.equalsTag;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TagsManagerTest {

    @Test
    public void whenThereIsNoImageAtPath_ReturnEmptyList() throws IOException {
        //Given
        TagsManager provider = new TagsManager(new ExifInterface(""));

        //When
        List<Tag> tagList = provider.getAllTags();

        //Then
        int defaultIntegerValuesCount = 7;
        assertThat(tagList.size(), is(defaultIntegerValuesCount));
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
        assertThat(tagList.get(0), equalsTag(new Tag(TAG_MAKE, "tag1", ObjectType.STRING)));
        assertThat(tagList.get(1), equalsTag(new Tag(TAG_MODEL, "tag2", ObjectType.STRING)));
    }

//    @Test
//    public void whenTagIsCleared_DefaultValueIsReturned() throws IOException {
//        //Given
//        ExifInterface exifInterface = new ExifInterface("");
//        TagsManager tagsManager = new TagsManager(exifInterface);
//
//        //When
//        tagsManager.clearTag(new Tag(TAG_GPS_TIMESTAMP, "", ObjectType.STRING));
//
//        //Then
//        String attributeValue = exifInterface.getAttribute(TAG_GPS_TIMESTAMP);
//        assertThat(attributeValue, is("00:00:00"));
//    }
}
