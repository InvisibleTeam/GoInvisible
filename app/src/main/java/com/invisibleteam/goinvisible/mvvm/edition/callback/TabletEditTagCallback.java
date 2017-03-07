package com.invisibleteam.goinvisible.mvvm.edition.callback;

import com.invisibleteam.goinvisible.helper.SharingHelper;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.mvvm.common.TagsUpdateStatusCallback;

public interface TabletEditTagCallback extends TagsUpdateStatusCallback {
    void onShare(ImageDetails details, SharingHelper sharingHelper);
}
