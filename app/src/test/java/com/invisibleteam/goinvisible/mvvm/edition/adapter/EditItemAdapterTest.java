package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.google.common.collect.ImmutableList;
import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.model.ObjectType;
import com.invisibleteam.goinvisible.model.Tag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditItemAdapterTest {

    private static final List<Tag> TAGS_LIST = ImmutableList.of(new Tag("key", "value", ObjectType.STRING));
    private Context context;

    @Before
    public void init() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void whenTagsListIsNotEmpty_ViewHolderIsBinded() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter();
        adapter.updateImageList(TAGS_LIST);
        ViewGroup view = mock(ViewGroup.class);
        when(view.getContext()).thenReturn(context);

        //When
        EditItemAdapter.ViewHolder holder = adapter.onCreateViewHolder(view, 0);
        adapter.onBindViewHolder(holder, 0);

        //Then
        boolean isItemViewTagProperlySet = holder.itemView.getTag() == TAGS_LIST.get(0);
        String isItemViewModelKeyProperlySet = holder
                .editItemViewModel
                .getKey()
                .get();
        String isItemViewModelValueProperlySet = holder
                .editItemViewModel
                .getValue()
                .get();

        assertTrue(isItemViewTagProperlySet);
        assertThat(isItemViewModelKeyProperlySet, is("key"));
        assertThat(isItemViewModelValueProperlySet, is("value"));
    }
}