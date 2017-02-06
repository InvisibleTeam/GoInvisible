package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.OnTagActionListener;
import com.invisibleteam.goinvisible.util.DateFormatterUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

class DateDialog {

    private final OnTagActionListener listener;
    private final Activity activity;
    private Calendar calendar;
    private Tag tag;

    DateDialog(Tag tag, OnTagActionListener listener, Activity activity) {
        this.tag = tag;
        this.listener = listener;
        this.activity = activity;
        this.calendar = Calendar.getInstance();
    }

    Dialog createDateDialog() {
        DatePickerDialog dialog = new DatePickerDialog(activity);
        dialog.setOnDateSetListener((view, year, month, day) -> {
            Calendar calendar = new GregorianCalendar(year, month, day, 0, 0, 0);
            tag.setValue(DateFormatterUtil.format(tag, calendar));
            this.listener.onEditEnded(tag);
        });
        return dialog;
    }

    Dialog createTimeDialog() {
        TimePickerDialog.OnTimeSetListener listener = (view, hour, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            tag.setValue(DateFormatterUtil.format(tag, calendar));
            this.listener.onEditEnded(tag);
        };

        return new TimePickerDialog(
                activity,
                listener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
    }

    Dialog createDateTimeDialog() {
        DatePickerDialog dialog = new DatePickerDialog(activity);
        dialog.setOnDateSetListener((view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            createTimeDialog().show();
        });
        return dialog;
    }
}
