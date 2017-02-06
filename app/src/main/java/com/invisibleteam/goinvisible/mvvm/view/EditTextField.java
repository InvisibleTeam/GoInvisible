package com.invisibleteam.goinvisible.mvvm.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.AutoCompleteTextView;

public class EditTextField extends AutoCompleteTextView {

    public EditTextField(Context context) {
        super(context);
    }

    public EditTextField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_UP) {
            clearFocus();
            return false;
        }
        return dispatchKeyEvent(event);
    }
}
