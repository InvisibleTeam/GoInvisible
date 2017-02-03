package com.invisibleteam.goinvisible.util;

import com.invisibleteam.goinvisible.model.Tag;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class TagsMatcher {

    public static Matcher<Tag> equals(final Tag tag) {
        return new TypeSafeMatcher<Tag>() {

            @Override
            public boolean matchesSafely(Tag expectedTag) {
                return expectedTag.getKey().equals(tag.getKey())
                        && expectedTag.getValue().equals(tag.getValue())
                        && expectedTag.getTagType().equals(tag.getTagType());
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendValue("Tags are not containing the same values");
            }
        };
    }
}
