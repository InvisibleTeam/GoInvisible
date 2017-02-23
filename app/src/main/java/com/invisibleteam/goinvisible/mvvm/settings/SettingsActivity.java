package com.invisibleteam.goinvisible.mvvm.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.databinding.SettingsActivityBinding;
import com.invisibleteam.goinvisible.helper.ClearingTagsReceiverHelper;
import com.invisibleteam.goinvisible.model.ClearingInterval;
import com.invisibleteam.goinvisible.mvvm.common.RadioDialogBuilder;
import com.invisibleteam.goinvisible.util.SharedPreferencesUtil;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import static com.invisibleteam.goinvisible.mvvm.settings.SettingsViewModel.SettingsViewModelCallback;

public class SettingsActivity extends AppCompatActivity {

    public static Intent buildIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    private final SettingsViewModelCallback viewModelCallback = new SettingsViewModelCallback() {

        @Override
        public void onOpenIntervalsDialog() {
            openIntervalsDialog();
        }

        @Override
        public void onEnableClearingService() {
            ClearingTagsReceiverHelper.startClearingServiceAlarm(getApplicationContext());
        }

        @Override
        public void onDisableClearingService() {
            ClearingTagsReceiverHelper.stopClearingServiceAlarm(getApplicationContext());
        }
    };

    private final SettingsViewModel viewModel = new SettingsViewModel(viewModelCallback);
    private Dialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        prepareViews();
        prepareActionBar();
    }

    private void prepareActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.settings));
        }
    }

    private void prepareViews() {
        SettingsActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.settings_activity);
        binding.setViewModel(viewModel);

        ClearingInterval interval = SharedPreferencesUtil.getInterval(this);
        viewModel.setIntervalName(interval.getIntervalFormattedName(this));

        boolean isClearingServiceEnabled = SharedPreferencesUtil.isClearingServiceActivated(this);

        if (isClearingServiceEnabled) {
            viewModel.setIsClearServiceEnabled(true);
        }
        alertDialog = createIntervalsDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void openIntervalsDialog() {
        alertDialog.show();
    }

    private Dialog createIntervalsDialog() {
        List<ClearingInterval> intervals = Arrays.asList(ClearingInterval.values());
        String[] intervalNames = new String[intervals.size()];

        ClearingInterval currentInterval = SharedPreferencesUtil.getInterval(this);
        int selectedIntervalIndex = 0;

        for (int index = 0; index < intervals.size(); index++) {
            ClearingInterval interval = intervals.get(index);
            intervalNames[index] = interval.getIntervalFormattedName(this);
            if (currentInterval.getInterval() == interval.getInterval()) {
                selectedIntervalIndex = index;
            }
        }

        RadioDialogBuilder.RadioDialogCallback callback = createCallback(intervals);

        return new RadioDialogBuilder(SettingsActivity.this, intervalNames, selectedIntervalIndex, callback)
                .setTitle(getString(R.string.intervals_dialog_title))
                .setPositiveButton(getText(android.R.string.ok))
                .setNegativeButton(getText(android.R.string.cancel))
                .build();
    }

    private RadioDialogBuilder.RadioDialogCallback createCallback(List<ClearingInterval> intervals) {
        return new RadioDialogBuilder.RadioDialogCallback() {
            @Override
            public void onClick(int index) {
            }

            @Override
            public void onApprove(DialogInterface dialog, int index) {
                dialog.dismiss();
                updateClearingServiceInterval(intervals.get(index));
            }

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        };
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    void updateClearingServiceInterval(ClearingInterval interval) {
        viewModel.setIntervalName(interval.getIntervalFormattedName(this));
        SharedPreferencesUtil.saveInterval(this, interval);
    }

    @VisibleForTesting
    SettingsViewModel getViewModel() {
        return viewModel;
    }

    @VisibleForTesting
    SettingsViewModelCallback getViewModelCallback() {
        return viewModelCallback;
    }

    @VisibleForTesting
    void setAlertDialog(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }
}
