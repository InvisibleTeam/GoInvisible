package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.view.ViewGroup;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.helper.EditItemAdapterHelper;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroupType;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static com.invisibleteam.goinvisible.util.TagsMatcher.containsKey;
import static com.invisibleteam.goinvisible.util.TagsMatcher.containsSectionName;
import static com.invisibleteam.goinvisible.util.TagsMatcher.containsTag;
import static com.invisibleteam.goinvisible.util.TagsMatcher.containsValue;
import static com.invisibleteam.goinvisible.util.TagsMatcher.notContainsTag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditItemAdapterTest {

    private static final int DEFAULT_VIEW_TYPE = -1;
    private static final int NUMBER_OF_HEADERS = 4;
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
    private EditActivity activity;

    @Before
    public void init() {
        activity = Robolectric.buildActivity(EditActivity.class).create().get();
    }

    @Test
    public void whenTagsListIsNotEmpty_TagViewHolderIsBound() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());
        adapter.updateTagList(tagList);
        ViewGroup parent = new ViewGroup(activity) {
            @Override
            protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

            }
        };

        //When
        TagViewHolder holder = (TagViewHolder) adapter.onCreateViewHolder(parent, DEFAULT_VIEW_TYPE);
        adapter.onBindViewHolder(holder, 1);
        int tagListSize = tagList.size() + NUMBER_OF_HEADERS;

        //Then
        assertThat(adapter.getTagsList().size(), is(tagListSize));
        assertThat(holder.getEditItemViewModel(), containsKey("key1"));
        assertThat(holder.getEditItemViewModel(), containsValue("value1"));
    }

    @Test
    public void whenTagsListIsNotEmpty_SectionViewHolderIsBound() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());
        adapter.updateTagList(tagList);
        ViewGroup parent = new ViewGroup(activity) {
            @Override
            protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

            }
        };

        //When
        SectionViewHolder holder = (SectionViewHolder) adapter.onCreateViewHolder(parent, TagGroupType.IMAGE_INFO.ordinal());
        adapter.onBindViewHolder(holder, 0);
        int tagListSize = tagList.size() + NUMBER_OF_HEADERS;

        //Then
        assertThat(adapter.getTagsList().size(), is(tagListSize));
        assertThat(holder.getSectionEditItemViewModel().getTagSectionName(), containsSectionName("Image info"));
    }

    @Test
    public void whenTagIsUpdated_TagsListIsUpdated() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());
        adapter.updateTagList(tagList);
        OnTagActionListener listener = mock(OnTagActionListener.class);
        adapter.setOnTagActionListener(listener);

        //When
        Tag editedTag = new Tag("key1", "editedValue", TagType.build(InputType.TEXT_STRING), TagGroupType.ADVANCED);
        adapter.updateTag(editedTag);

        //Then
        verify(listener).onTagsUpdated();
        assertThat(adapter.getTagsList(), containsTag(editedTag));
    }

    @Test
    public void whenTagsAreChanged_OnlyChangedTagsAreReturned() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());
        OnTagActionListener listener = mock(OnTagActionListener.class);
        adapter.setOnTagActionListener(listener);
        adapter.updateTagList(tagList);

        //When
        tag1.setValue("changedValue");
        tag3.setValue("changedValue");
        adapter.updateTag(tag1);
        adapter.updateTag(tag2);
        adapter.updateTag(tag3);
        List<Tag> changedTags = adapter.getChangedTags();

        //Then
        assertThat(changedTags, containsTag(tag1));
        assertThat(changedTags, notContainsTag(tag2));
        assertThat(changedTags, containsTag(tag3));
        assertThat(changedTags, notContainsTag(tag4));
    }

    @Test
    public void whenTagIsAnImageInfoSection_ImageInfoSectionViewTypeIsReturned() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());

        //When
        int viewType = adapter.getItemViewType(EditItemAdapter.IMAGE_INFO_SECTION_POSITION);

        //Then
        assertThat(viewType, is(TagGroupType.IMAGE_INFO.ordinal()));
    }

    @Test
    public void whenTagIsALocationInfoSection_LocationInfoSectionViewTypeIsReturned() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());

        //When
        int viewType = adapter.getItemViewType(EditItemAdapter.LOCATION_INFO_SECTION_POSITION);

        //Then
        assertThat(viewType, is(TagGroupType.LOCATION_INFO.ordinal()));
    }

    @Test
    public void whenTagIsADeviceInfoSection_DeviceInfoSectionViewTypeIsReturned() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());

        //When
        int viewType = adapter.getItemViewType(EditItemAdapter.DEVICE_INFO_SECTION_POSITION);

        //Then
        assertThat(viewType, is(TagGroupType.DEVICE_INFO.ordinal()));
    }

    @Test
    public void whenTagIsAnAdvancedInfoSection_AdvancedInfoSectionViewTypeIsReturned() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());

        //When
        int viewType = adapter.getItemViewType(EditItemAdapter.ADVANCED_INFO_SECTION_POSITION);

        //Then
        assertThat(viewType, is(TagGroupType.ADVANCED.ordinal()));
    }

    @Test
    public void whenTagIsNotASection_DefaultViewTypeIsReturned() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());

        //When
        int viewType = adapter.getItemViewType(10);

        //Then
        assertThat(viewType, is(EditItemAdapter.DEFAULT_VIEW_TYPE));
    }

    @Test
    public void whenTagListIsUpdated_HeadersAreAddedToListSuccessfully() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());
        adapter.updateTagList(tagList);
        int tagListWithSectionHeaders = tagList.size() + NUMBER_OF_HEADERS;

        //Then
        assertThat(adapter.getItemCount(), is(tagListWithSectionHeaders));
    }

    @Test
    public void whenTagListIsUpdatedTwice_OnlyOnTagsUpdatedMethodIsCalled() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());
        OnTagActionListener onTagActionListener = mock(OnTagActionListener.class);
        adapter.setOnTagActionListener(onTagActionListener);
        adapter.updateTagList(tagList);

        //When
        adapter.updateTagList(tagList);

        //Then
        verify(onTagActionListener).onTagsUpdated();
    }

    @Test
    public void whenTagIsUpdatedAndTagListIsEmpty_NothingIsHappened() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter(new EditItemAdapterHelper());
        OnTagActionListener onTagActionListener = mock(OnTagActionListener.class);
        adapter.setOnTagActionListener(onTagActionListener);
        adapter = spy(adapter);

        //When
        when(adapter.getTagsList()).thenReturn(new ArrayList<>());
        adapter.updateTag(tag1);

        //Then
        verify(onTagActionListener, times(0)).onTagsUpdated();
    }

    private Tag createTag(String key, String value, TagGroupType groupType) {
        return new Tag(key, value, TagType.build(InputType.TEXT_STRING), groupType);
    }
}