package com.invisibleteam.goinvisible.helper;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroupType;
import com.invisibleteam.goinvisible.model.TagType;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.invisibleteam.goinvisible.util.TagsMatcher.containsTag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EditItemAdapterHelperTest {

    private final Tag tag1 = createTag("key1", "value1", TagGroupType.IMAGE_INFO);
    private final Tag tag2 = createTag("key2", "value2", TagGroupType.LOCATION_INFO);
    private final Tag tag3 = createTag("key3", "value3", TagGroupType.DEVICE_INFO);
    private final Tag tag4 = createTag("key4", "value4", TagGroupType.ADVANCED);
    private final List<Tag> tagList = new ArrayList<Tag>() {
        {
            add(tag1);
            add(tag2);
            add(tag3);
            add(tag4);
        }
    };

    private EditItemAdapterHelper helper;

    @Before
    public void setUp() throws Exception {
        helper = new EditItemAdapterHelper();
    }

    @Test
    public void whenTagListIsCreated_TagListContainsSectionsHeaders() {
        //When
        helper.createGroups(tagList);
        List<Tag> tagListWithHeaders = helper.getTagsList();

        //Then
        assertThat(tagListWithHeaders.size(), is(8));
        assertThat(tagListWithHeaders, containsTag(new Tag("", "", TagType.build(InputType.INDEFINITE), TagGroupType.IMAGE_INFO)));
        assertThat(tagListWithHeaders, containsTag(new Tag("", "", TagType.build(InputType.INDEFINITE), TagGroupType.DEVICE_INFO)));
        assertThat(tagListWithHeaders, containsTag(new Tag("", "", TagType.build(InputType.INDEFINITE), TagGroupType.LOCATION_INFO)));
        assertThat(tagListWithHeaders, containsTag(new Tag("", "", TagType.build(InputType.INDEFINITE), TagGroupType.ADVANCED)));
    }

    private Tag createTag(String key, String value, TagGroupType groupType) {
        return new Tag(key, value, TagType.build(InputType.TEXT_STRING), groupType);
    }
}