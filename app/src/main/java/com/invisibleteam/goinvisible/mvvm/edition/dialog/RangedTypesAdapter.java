package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.invisibleteam.goinvisible.R;

import java.util.List;

class RangedTypesAdapter extends ArrayAdapter<String> {

    RangedTypesAdapter(Context context, List<String> values) {
        super(context, 0, values);
    }

    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_value_view, parent, false);
        }
        TextView tagValue = (TextView) convertView.findViewById(R.id.tag_value);
        tagValue.setText(getItem(position));

        return convertView;
    }
}
