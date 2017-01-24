package com.invisibleteam.goinvisible.mvvm.edition;

import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagsProvider {

    private ImageDetails imageDetails;

    public TagsProvider(ImageDetails imageDetails) {
        this.imageDetails = imageDetails;
    }

    public List<Tag> getTags() {
        return new ArrayList<Tag>() {
            {
                add(new Tag("key1", "value1"));
                add(new Tag("key2", "value2"));
                add(new Tag("key3", "value3"));
                add(new Tag("key4", "value4"));
                add(new Tag("key5", "value5"));
                add(new Tag("key6", "value6"));
            }
        };
    }
}
