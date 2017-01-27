package com.invisibleteam.goinvisible.util;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

public class ImageUtil {

    public static void grayedOut(ImageView view) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  // 0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        view.setColorFilter(cf);
        view.setImageAlpha(128);   // 128 = 0.5
    }
}
