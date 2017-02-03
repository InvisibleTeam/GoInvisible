package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;

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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditItemAdapterTest {

    private final Tag tag = new Tag("key", "value", TagType.build(InputType.TEXT_STRING));
    private final List<Tag> tagList = new ArrayList<Tag>() {
        {
            add(tag);
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
        assertThat(editItemViewModel, containsKey("key"));
        assertThat(editItemViewModel, containsValue("value"));
    }

    @Test
    public void whenTagIsUpdated_TagsListIsUpdated() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter();
        adapter.updateImageList(tagList);

        //When
        Tag editedTag = new Tag("key", "editedValue", TagType.build(InputType.TEXT_STRING));
        adapter.updateTag(editedTag);

        //Then
        assertThat(adapter.getTagsList(), containsTag(editedTag));
    }
}