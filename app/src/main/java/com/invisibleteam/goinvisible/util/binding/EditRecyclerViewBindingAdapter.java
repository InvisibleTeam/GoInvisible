package com.invisibleteam.goinvisible.util.binding;


import android.databinding.BindingAdapter;

import com.invisibleteam.goinvisible.model.TagGroup;
import com.invisibleteam.goinvisible.mvvm.edition.ObservableTagGroupDiffResultModel;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditItemGroupAdapter;
import com.invisibleteam.goinvisible.mvvm.edition.adapter.EditRecyclerView;

import java.util.List;

public class EditRecyclerViewBindingAdapter {
    @BindingAdapter("modelList")
    public static void bindList(EditRecyclerView compoundRecyclerView, List<TagGroup> resultModel) {
        compoundRecyclerView.updateList(resultModel);
    }

    @BindingAdapter("diffList")
    public static void bindDiffList(EditRecyclerView compoundRecyclerView,
                                    ObservableTagGroupDiffResultModel diffResultListModel) {
        compoundRecyclerView.updateData(diffResultListModel.getResultModels());
    }

    @BindingAdapter("onItemAction")
    public static void bindClickListener(EditRecyclerView compoundRecyclerView, EditItemGroupAdapter.ItemActionListener listener) {
        compoundRecyclerView.setOnItemActionListener(listener);
    }
}
