package com.invisibleteam.goinvisible.util;

import com.invisibleteam.goinvisible.model.Tag;

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
                description
                        .appendValue("List doesn't contain expected tag");
            }
        };
    }
}
