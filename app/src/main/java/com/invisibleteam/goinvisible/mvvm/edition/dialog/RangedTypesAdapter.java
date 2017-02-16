package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
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
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_value_view, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String value = getItem(position);
        if (value != null) {
            viewHolder.setValueText(value);
        }

        return convertView;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    class ViewHolder {
        private TextView valueTextView;

        ViewHolder(View groupView) {
            valueTextView = (TextView) groupView.findViewById(R.id.tag_value);
        }

        void setValueText(String value) {
            valueTextView.setText(value);
        }
    }
}
