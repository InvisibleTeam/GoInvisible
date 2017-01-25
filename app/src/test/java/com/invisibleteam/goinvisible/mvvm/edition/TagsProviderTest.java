package com.invisibleteam.goinvisible.mvvm.edition;

import android.media.ExifInterface;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static android.media.ExifInterface.TAG_EXPOSURE_TIME;
import static android.media.ExifInterface.TAG_GPS_DATESTAMP;
import static com.invisibleteam.goinvisible.util.TagsMatcher.equalsTag;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TagsProviderTest {

    @Test
    public void whenThereIsNoImageAtPath_ReturnEmptyList() {
        //Given
        TagsProvider provider = new TagsProvider(new ImageDetails("", ""));

        //When
        List<Tag> tagList = provider.getAllTags();

        //Then
        assertThat(tagList.size(), is(0));
    }

    @Test
    public void whenThereAreSomeTags_ProperTagListIsCreated() {
        //Given
        ExifInterface exifInterface = mock(ExifInterface.class);
        when(exifInterface.getAttribute(TAG_GPS_DATESTAMP)).thenReturn("tag1");
        when(exifInterface.getAttribute(TAG_EXPOSURE_TIME)).thenReturn("tag2");
        TagsProvider provider = new TagsProvider(new ImageDetails("", ""));

        //When
        List<Tag> tagList = provider.getAllTags(exifInterface);

        //Then
        assertThat(tagList.get(0), equalsTag(new Tag(TAG_GPS_DATESTAMP, "tag1")));
        assertThat(tagList.get(1), equalsTag(new Tag(TAG_EXPOSURE_TIME, "tag2")));
    }


}
