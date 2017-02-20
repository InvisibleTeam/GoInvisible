package com.invisibleteam.goinvisible.model;

import android.content.Context;

import com.invisibleteam.goinvisible.R;

import java.util.concurrent.TimeUnit;

public enum ClearingInterval {

    DAY(R.string.interval_value_day, TimeUnit.DAYS.toMillis(1)),
    WEEK(R.string.interval_value_week, TimeUnit.DAYS.toMillis(7)),
    MONTH(R.string.interval_value_month, TimeUnit.DAYS.toMillis(28));

    private final int intevalValueResId;
    private final long interval;

    ClearingInterval(int intevalValueResId, long interval) {
        this.intevalValueResId = intevalValueResId;
        this.interval = interval;
    }

    public String getIntervalFormattedName(Context context) {
        return context.getString(intevalValueResId);
    }

    public long getInterval() {
        return interval;
    }
}
