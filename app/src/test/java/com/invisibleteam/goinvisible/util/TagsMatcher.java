package com.invisibleteam.goinvisible.util;

import android.view.View;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditItemViewModel;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.List;

public class TagsMatcher {

    public static Matcher<List<Tag>> containsTag(final Tag tag) {
        return new TypeSafeMatcher<List<Tag>>() {

            @Override
            public boolean matchesSafely(List<Tag> tagList) {
                for (Tag expectedTag : tagList) {
                    if (expectedTag.getKey().equals(tag.getKey())
                            && expectedTag.getValue().equals(tag.getValue())) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue("List doesn't contain expected tag");
            }
        };
    }

    public static Matcher<EditItemViewModel> containsKey(final String key) {
        return new TypeSafeMatcher<EditItemViewModel>() {

            @Override
            public boolean matchesSafely(EditItemViewModel model) {
                return model.getKey().get().equals(key);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue("ViewModel doesn't contain expected key");
            }
        };
    }

    public static Matcher<EditItemViewModel> containsValue(final String value) {
        return new TypeSafeMatcher<EditItemViewModel>() {

            @Override
            public boolean matchesSafely(EditItemViewModel model) {
                return model.getValue().get().equals(value);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue("ViewModel doesn't contain expected value");
            }
        };
    }

    public static Matcher<View> containsItem(final Tag tag) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                Tag item = (Tag) view.getTag();
                return item == tag;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue("ViewModel doesn't contain expected value");
            }
        };
    }
}
