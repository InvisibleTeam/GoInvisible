package com.invisibleteam.goinvisible.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class ClearingTagsReceiver extends BroadcastReceiver {

    public static final String CLEARING_TAGS_REQUEST_CODE_KEY = "request_code_key";
    public static final int CLEARING_TAGS_REQUEST_CODE = 64832;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (containsProperRequestCode(bundle)) {
            context.startService(new Intent(context, ClearingTagsService.class));
        }
    }

    private boolean containsProperRequestCode(@Nullable Bundle bundle) {
        if (bundle == null || !bundle.containsKey(CLEARING_TAGS_REQUEST_CODE_KEY)) {
            return false;
        }

        Object requestCode = bundle.get(CLEARING_TAGS_REQUEST_CODE_KEY);

        return requestCode instanceof Integer && (Integer) requestCode == CLEARING_TAGS_REQUEST_CODE;
    }
}
