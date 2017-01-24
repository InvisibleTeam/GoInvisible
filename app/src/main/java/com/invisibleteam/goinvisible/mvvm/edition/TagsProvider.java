package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;

import java.util.Arrays;
import java.util.List;

public class TagsProvider {

    private final ImageDetails imageDetails;

    public TagsProvider(ImageDetails imageDetails) {
        this.imageDetails = imageDetails;
    }

    public List<Tag> getTags() {
        return Arrays.asList(
                new Tag("key1", "value1"),
                new Tag("key2", "value2"),
                new Tag("key3", "value3"),
                new Tag("key4", "value4"),
                new Tag("key5", "value5"),
                new Tag("key6", "value6"));
    }
}
