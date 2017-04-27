package com.invisibleteam.goinvisible.mvvm.images.adapter;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ImageDetails;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.invisibleteam.goinvisible.util.ImageItemViewModelMatcher.contains;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ImageItemViewModelTest {

    @Test
    public void WhenImageItemViewModelIsCreated_ProperValuesAreSet() {
        //Given
        ImageDetails imageDetails = new ImageDetails("path", "name", 0);
        ImageItemViewModel imageItemViewModel = new ImageItemViewModel();

        //When
        imageItemViewModel.setModel(imageDetails);

        //Then
        assertThat(imageItemViewModel, contains(imageDetails));
    }
}
