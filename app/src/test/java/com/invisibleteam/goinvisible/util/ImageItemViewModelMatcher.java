package com.invisibleteam.goinvisible.util;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.images.adapter.ImageItemViewModel;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ImageItemViewModelMatcher {

    public static Matcher<ImageItemViewModel> contains(final ImageDetails imageDetails) {
        return new TypeSafeMatcher<ImageItemViewModel>() {

            @Override
            public boolean matchesSafely(ImageItemViewModel viewModel) {
                return viewModel
                        .getName()
                        .get()
                        .equals(imageDetails.getName()) &&
                        viewModel
                                .getImageUrl()
                                .get()
                                .equals(imageDetails.getPath());
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendValue("ImageItemViewModel contains different data");
            }
        };
    }
}
