package com.invisibleteam.goinvisible.mvvm.edition.adapter;


import android.content.Context;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.TagGroup;
import com.invisibleteam.goinvisible.mvvm.edition.TagDiffResultModel;

import java.util.ArrayList;
import java.util.List;

public class EditRecyclerView extends FrameLayout {

    private final ArrayList<TagGroup> arrayList = new ArrayList<>();
    private EditItemGroupAdapter adapter;

    public EditRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        View view = LayoutInflater
                .from(getContext())
                .inflate(R.layout.listview, this, true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EditItemGroupAdapter(arrayList);
        recyclerView.setAdapter(adapter);
    }

    public void updateList(List<TagGroup> tagList) {
        arrayList.clear();
        arrayList.addAll(tagList);
        adapter.notifyParentDataSetChanged(true);
    }

    public void setOnItemActionListener(EditItemGroupAdapter.ItemActionListener listener) {
        adapter.setItemActionListener(listener);
    }

    public void updateData(List<TagDiffResultModel> diffResultList) {
        for (int i = 0; i < diffResultList.size(); i++) {
            TagDiffResultModel resultModel = diffResultList.get(i);
            resultModel.getDiffResult().dispatchUpdatesTo(buildListUpdateCallback(resultModel.getGroupPosition()));
        }
    }

    private ListUpdateCallback buildListUpdateCallback(int groupPosition) {
        return new ListUpdateCallback() {
            @Override
            public void onInserted(int position, int count) {
            }

            @Override
            public void onRemoved(int position, int count) {
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
            }

            @Override
            public void onChanged(int position, int count, Object payload) {
                adapter.notifyChildChanged(groupPosition, position);
            }
        };
    }
}
