package com.invisibleteam.goinvisible.mvvm.edition.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;

import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.EditViewModel;
import com.invisibleteam.goinvisible.util.DateFormatterUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;

class DateDialog {

    private final EditViewModel viewModel;
    private final Context context;
    private Calendar calendar;
    private Tag tag;

    DateDialog(Context context, Tag tag, EditViewModel viewModel) {
        this.context = context;
        this.tag = tag;
        this.viewModel = viewModel;
        this.calendar = Calendar.getInstance();
    }

    Dialog createDateDialog() {
        DatePickerDialog.OnDateSetListener listener = (view, year, month, day) -> {
            Calendar calendar = new GregorianCalendar(year, month, day, 0, 0, 0);
            tag.setValue(DateFormatterUtil.format(tag, calendar));
            viewModel.onEditEnded(tag);
        };

        return createDateTimeDialog(listener);
    }

    Dialog createTimeDialog() {
        TimePickerDialog.OnTimeSetListener listener = (view, hour, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            tag.setValue(DateFormatterUtil.format(tag, calendar));
            viewModel.onEditEnded(tag);
        };
        TimePickerDialog dialog = new TimePickerDialog(
                context,
                listener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        dialog.setTitle("");
        return dialog;
    }

    Dialog createDateTimeDialog() {
        DatePickerDialog.OnDateSetListener listener = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            createTimeDialog().show();
        };

        return createDateTimeDialog(listener);
    }

    private Dialog createDateTimeDialog(DatePickerDialog.OnDateSetListener listener) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(context, listener, year, month, day);
    }
}
