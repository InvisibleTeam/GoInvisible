package com.invisibleteam.goinvisible.util;

import com.invisibleteam.goinvisible.model.Tag;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class TagsMatcher {

    public static Matcher<Tag> equalsTag(final Tag expectedTag) {
        return new TypeSafeMatcher<Tag>() {
            @Override
            public boolean matchesSafely(Tag tag) {
                return tag.getKey().equals(expectedTag.getKey()) &&
                        tag.getValue().equals(expectedTag.getValue());
            }

            @Override
            public void describeTo(Description description) {
                description
                        .appendText("tag doesn't equals expected one: ")
                        .appendValue("key: ")
                        .appendValue(expectedTag.getKey())
                        .appendValue(", value: ")
                        .appendValue(expectedTag.getValue());
            }
        };
    }
}
