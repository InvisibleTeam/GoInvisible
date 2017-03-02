package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.invisibleteam.goinvisible.BuildConfig;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.invisibleteam.goinvisible.util.TagsMatcher.containsSectionName;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SectionViewHolderTest {

    private View itemView;

    @Before
    public void setUp() throws Exception {
        EditActivity activity = Robolectric.buildActivity(EditActivity.class).create().get();
        ViewGroup parent = new ViewGroup(activity) {
            @Override
            protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

            }
        };
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_edit_item_view, parent, false);
    }

    @Test
    public void whenSectionViewHolderHasSetSectionName_ItsViewModelHasSetSectionName() {
        //Given
        SectionViewHolder holder = new SectionViewHolder(itemView);

        //When
        holder.setSectionName("Image Info");

        //Then
        assertThat(holder.getSectionEditItemViewModel().getTagSectionName(), containsSectionName("Image Info"));
    }

}