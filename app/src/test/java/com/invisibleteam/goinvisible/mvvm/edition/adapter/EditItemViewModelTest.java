package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import com.invisibleteam.goinvisible.model.InputType;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.model.TagType;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EditItemViewModelTest {

    private final Tag tag = new Tag("key", "value", TagType.build(InputType.TEXT_STRING));
    private final Tag unmodifiableTag = new Tag("key", "value", TagType.build(InputType.UNMODIFIABLE));

    private EditItemViewModel editItemViewModel;

    @Before
    public void setUp() {
        editItemViewModel = new EditItemViewModel();
    }

    @Test
    public void whenTagTypeIsUnmodifiable_ClearButtonIsInvisible() {
        //When
        editItemViewModel.setModel(unmodifiableTag);

        //Then
        assertThat(editItemViewModel.getIsModifiable().get(), is(false));
    }

    @Test
    public void whenTagTypeModifiable_ClearButtonIsVisible() {
        //When
        editItemViewModel.setModel(tag);

        //Then
        assertThat(editItemViewModel.getIsModifiable().get(), is(true));
    }
}