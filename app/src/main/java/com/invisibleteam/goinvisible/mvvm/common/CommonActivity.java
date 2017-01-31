package com.invisibleteam.goinvisible.mvvm.common;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.invisibleteam.goinvisible.R;

import java.util.Locale;

public abstract class CommonActivity extends AppCompatActivity {

    private static final int REQUEST_FOR_PERMISSIONS_REQUEST_CODE = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestForPermissions();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public abstract void prepareView();

    private void requestForPermissions() {
        final String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!arePermissionsGranted(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_FOR_PERMISSIONS_REQUEST_CODE);
        } else {
            prepareView();
        }
    }

    private boolean arePermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FOR_PERMISSIONS_REQUEST_CODE: {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        showMissingPermissionsDialog();
                        return;
                    }
                }
                prepareView();
                break;
            }
            default: {
                showMissingPermissionsDialog();
            }
        }
    }

    private void showMissingPermissionsDialog() {
        new AlertDialog
                .Builder(this, R.style.AlertDialogStyle)
                .setTitle(R.string.permissions_dialog_title)
                .setMessage(R.string.permissions_dialog_message)
                .setPositiveButton(getString(android.R.string.ok)
                        .toUpperCase(Locale.getDefault()), (dialog, which) -> requestForPermissions())
                .setNegativeButton(getString(R.string.exit_app)
                        .toUpperCase(Locale.getDefault()), (dialog, which) -> System.exit(0))
                .setCancelable(false)
                .show();
    }
}
