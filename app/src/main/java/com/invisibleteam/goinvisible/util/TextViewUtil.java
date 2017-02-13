package com.invisibleteam.goinvisible.util;

import android.text.TextUtils;
import android.widget.TextView;

public class TextViewUtil {

    public static void setEllipsizedForView(TextView textView) {
        textView.setLines(1);
        textView.setHorizontallyScrolling(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
    }
}
