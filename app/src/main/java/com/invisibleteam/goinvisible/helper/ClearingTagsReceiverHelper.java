package com.invisibleteam.goinvisible.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.invisibleteam.goinvisible.model.ClearingInterval;
import com.invisibleteam.goinvisible.service.ClearingTagsReceiver;
import com.invisibleteam.goinvisible.service.ClearingTagsService;
import com.invisibleteam.goinvisible.util.SharedPreferencesUtil;

public class ClearingTagsReceiverHelper {

    public static void startClearingServiceAlarm(Context context) {
        boolean isServiceEnabled = SharedPreferencesUtil.isClearingServiceActivated(context);
        if (isServiceEnabled) {
            return;
        }

        SharedPreferencesUtil.setClearingServiceActivation(context, true);

        ClearingInterval interval = SharedPreferencesUtil.getInterval(context);

        Intent alarmIntent = new Intent(context, ClearingTagsReceiver.class);
        alarmIntent.putExtra(ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE_KEY,
                ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        manager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                interval.getInterval(),
                pendingIntent);
    }

    public static void stopClearingServiceAlarm(Context context) {
        SharedPreferencesUtil.setClearingServiceActivation(context, false);

        Intent alarmIntent = new Intent(context, ClearingTagsReceiver.class);

        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, ClearingTagsReceiver.CLEARING_TAGS_REQUEST_CODE, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);

        Intent intent = new Intent(context, ClearingTagsService.class);
        context.stopService(intent);
    }
}
