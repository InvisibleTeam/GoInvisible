package com.invisibleteam.goinvisible.mvvm.edition.adapter;

import com.invisibleteam.goinvisible.BuildConfig;

import org.robolectric.annotation.Config;

//@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EditItemAdapterTest {

    /*private Tag TAG = new Tag("key", "value", InputType.STRING);
    private List<Tag> TAGS_LIST = new ArrayList<Tag>() {
        {
            add(TAG);
        }
    };
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

    @Test
    public void whenTagIsUpdated_TagsListIsUpdated() {
        //Given
        EditItemAdapter adapter = new EditItemAdapter();
        adapter.updateImageList(TAGS_LIST);

        //When
        Tag editedTag = new Tag("key", "editedValue", InputType.STRING);
        adapter.updateTag(editedTag);

        //Then
        assertThat(adapter.getTagsList(), containsTag(editedTag));
    }*/
}