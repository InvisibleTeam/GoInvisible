package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static com.invisibleteam.goinvisible.util.TagsMatcher.containsItem;
import static com.invisibleteam.goinvisible.util.TagsMatcher.containsKey;
import static com.invisibleteam.goinvisible.util.TagsMatcher.containsTag;
import static com.invisibleteam.goinvisible.util.TagsMatcher.containsValue;
import static com.invisibleteam.goinvisible.util.TagsMatcher.notContainsTag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditItemAdapterTest {

    private Tag TAG1 = createTag("key1", "value1");
    private Tag TAG2 = createTag("key2", "value2");
    private Tag TAG3 = createTag("key3", "value3");
    private Tag TAG4 = createTag("key4", "value4");
    private Tag TAG5 = createTag("key5", "value5");
    private Tag TAG6 = createTag("key6", "value6");
    private Tag TAG7 = createTag("key7", "value7");

    private final List<Tag> tagList = new ArrayList<Tag>() {
        {
            add(TAG1);
            add(TAG2);
            add(TAG3);
            add(TAG4);
            add(TAG5);
            add(TAG6);
            add(TAG7);
        }
    };
    private Context context;

    @Before
    public void init() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void whenTagsListIsNotEmpty_ViewHolderIsBound() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter();
        adapter.updateImageList(tagList);
        ViewGroup view = mock(ViewGroup.class);
        when(view.getContext()).thenReturn(context);

        //When
        EditItemAdapter.ViewHolder holder = adapter.onCreateViewHolder(view, 0);
        adapter.onBindViewHolder(holder, 0);

        //Then
        EditItemViewModel editItemViewModel = holder.editItemViewModel;

        assertThat(adapter.getTagsList().size(), is(tagList.size()));
        assertThat(holder.itemView, containsItem(tagList.get(0)));
        assertThat(editItemViewModel, containsKey("key1"));
        assertThat(editItemViewModel, containsValue("value1"));
    }

    @Test
    public void whenTagIsUpdated_TagsListIsUpdated() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter();
        adapter.updateImageList(tagList);
        OnTagActionListener listener = mock(OnTagActionListener.class);
        adapter.setOnTagActionListener(listener);

        //When
        Tag editedTag = new Tag("key1", "editedValue", TagType.build(InputType.TEXT_STRING));
        adapter.updateTag(editedTag);

        //Then
        verify(listener).onTagsUpdated();
        assertThat(adapter.getTagsList(), containsTag(editedTag));
    }

    @Test
    public void whenTagsAreChanged_OnlyChangedTagsAreReturned() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter();
        OnTagActionListener listener = mock(OnTagActionListener.class);
        adapter.setOnTagActionListener(listener);
        adapter.updateImageList(tagList);

        //When
        TAG1.setValue("changedValue");
        TAG3.setValue("changedValue");
        adapter.updateTag(TAG1);
        adapter.updateTag(TAG2);
        adapter.updateTag(TAG3);
        List<Tag> changedTags = adapter.getChangedTags();

        //Then
        assertThat(changedTags, containsTag(TAG1));
        assertThat(changedTags, notContainsTag(TAG2));
        assertThat(changedTags, containsTag(TAG3));
        assertThat(changedTags, notContainsTag(TAG4));
        assertThat(changedTags, notContainsTag(TAG5));
        assertThat(changedTags, notContainsTag(TAG6));
        assertThat(changedTags, notContainsTag(TAG7));
    }

    private Tag createTag(String key, String value) {
        return new Tag(key, value, TagType.build(InputType.TEXT_STRING));
    }
}