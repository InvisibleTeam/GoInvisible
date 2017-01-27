package com.invisibleteam.goinvisible.util.binding;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.util.ObservableString;

public class TextViewBindingAdapter {
    @BindingAdapter({"android:text"})
    public static void bindEditText(TextView view, ObservableString observableString) {
        if (view.getTag(R.id.binded) == null) {
            view.setTag(R.id.binded, true);
            view.addTextChangedListener(new TextViewBindingTextWatcher(observableString));
        }
        String newValue = observableString.get();
        if (!view.getText().toString().equals(newValue)) {
            view.setText(newValue);
        }
    }
}
