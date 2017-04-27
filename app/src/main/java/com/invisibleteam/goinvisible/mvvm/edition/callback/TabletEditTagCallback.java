package com.invisibleteam.goinvisible.mvvm.edition.callback;

import android.content.Intent;

import com.invisibleteam.goinvisible.mvvm.common.TagsUpdateStatusCallback;

public interface TabletEditTagCallback extends TagsUpdateStatusCallback {
    void onShare(Intent intent);

    void showViewInEditStateInformation();
}
