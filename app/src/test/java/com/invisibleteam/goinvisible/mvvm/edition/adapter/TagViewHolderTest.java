package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagGroupType;
import com.invisibleteam.goinvisible.model.TagType;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.edition.callback.EditViewModelCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.invisibleteam.goinvisible.util.TagsMatcher.containsItem;
import static com.invisibleteam.goinvisible.util.TagsMatcher.containsKey;
import static com.invisibleteam.goinvisible.util.TagsMatcher.containsValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TagViewHolderTest {

    private final Tag tag = new Tag("key", "value", TagType.build(InputType.TEXT_STRING), TagGroupType.IMAGE_INFO);
    private View itemView;

    @Before
    public void setUp() throws Exception {
        EditActivity activity = Robolectric.buildActivity(EditActivity.class).create().get();
        LinearLayout parent = new LinearLayout(activity);
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item_view, parent, false);
    }

    @Test
    public void whenViewHolderHasSetTag_ItsViewModelHasSetTag() {
        //Given
        TagViewHolder holder = new TagViewHolder(itemView, null);

        //When
        holder.setTag(tag);

        //Then
        assertThat(holder.getEditItemViewModel(), containsKey("key"));
        assertThat(holder.getEditItemViewModel(), containsValue("value"));
        assertThat(holder.itemView, containsItem(tag));
    }

    @Test
    public void whenViewHolderHasSetTagActionListenerAndClearButtonIsClicked_OnClearMethodIsCalled() {
        //Given
        EditViewModelCallback editViewModelCallback = mock(EditViewModelCallback.class);
        TagViewHolder holder = new TagViewHolder(itemView, editViewModelCallback);
        holder.setTag(tag);

        //When
        holder.itemView.findViewById(R.id.clear_button).performClick();

        //Then
        verify(editViewModelCallback).onClear(tag);
    }

    @Test
    public void whenViewHolderHasSetTagActionListenerAndItemViewIsClicked_OnEditStartedMethodIsCalled() {
        //Given
        EditViewModelCallback editViewModelCallback = mock(EditViewModelCallback.class);
        TagViewHolder holder = new TagViewHolder(itemView, editViewModelCallback);
        holder.setTag(tag);

        //When
        holder.itemView.performClick();

        //Then
        verify(editViewModelCallback).onEditStarted(tag);
    }

}