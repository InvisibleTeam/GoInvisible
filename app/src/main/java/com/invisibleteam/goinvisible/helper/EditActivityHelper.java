package com.invisibleteam.goinvisible.helper;

import android.content.ContentResolver;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.invisibleteam.goinvisible.R;
import com.invisibleteam.goinvisible.model.GeolocationTag;
import com.invisibleteam.goinvisible.model.ImageDetails;
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.EditActivity;
import com.invisibleteam.goinvisible.mvvm.edition.GoogleLocationApiEstablisher;
import com.invisibleteam.goinvisible.mvvm.edition.GpsEstablisher;
import com.invisibleteam.goinvisible.util.LatLngUtil;
import com.invisibleteam.goinvisible.util.TagUtil;

import java.io.FileNotFoundException;

import static android.content.Context.LOCATION_SERVICE;
import static android.provider.MediaStore.Images.Media.insertImage;
import static com.invisibleteam.goinvisible.mvvm.edition.EditActivity.PLACE_REQUEST_ID;

public class EditActivityHelper {

    private static final int INITIAL_MAP_RADIUS = 20000;
    private static final String IMAGE_EMPTY_DESCRIPTION = "";
    private static final String SHARE_IMAGE_JPG_TYPE = "image/jpg";

    private EditActivity editActivity;

    public EditActivityHelper(EditActivity editActivity) {
        this.editActivity = editActivity;
    }

    @NonNull
    public GeolocationTag prepareNewPlacePositionTag(Place place, Tag tag) {
        GeolocationTag geolocationTag = (GeolocationTag) tag;
        double latitude = place.getLatLng().latitude;
        double longitude = place.getLatLng().longitude;

        String newLatitude = TagUtil.parseDoubleGPSToRationalGPS(latitude);
        geolocationTag.setValue(newLatitude);
        geolocationTag.setLatitudeRef(latitude > 0.0 ? "N" : "S");

        String newLongitude = TagUtil.parseDoubleGPSToRationalGPS(longitude);
        geolocationTag.setSecondValue(newLongitude);
        geolocationTag.setLongitudeRef(longitude > 0.0 ? "E" : "W");

        return geolocationTag;
    }

    public void openPlacePicker(Tag tag) {
        try {
            GeolocationTag geolocationTag = (GeolocationTag) tag;
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            if (geolocationTag.getValue() != null) {
                LatLng center = new LatLng(
                        TagUtil.parseRationalGPSToDoubleGPS(geolocationTag.getValue()),
                        TagUtil.parseRationalGPSToDoubleGPS(geolocationTag.getSecondValue())
                );
                LatLngBounds bounds = LatLngUtil.generateBoundsWithZoom(center, INITIAL_MAP_RADIUS);
                intentBuilder.setLatLngBounds(bounds);
            }

            Intent intent = intentBuilder.build(editActivity);
            editActivity.startActivityForResult(intent, PLACE_REQUEST_ID);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(editActivity, e.getConnectionStatusCode(), 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(editActivity, R.string.google_services_error,
                    Toast.LENGTH_LONG).show();
        }
    }

    public Snackbar createGpsSnackBar() {
        return Snackbar.make(
                editActivity.findViewById(android.R.id.content),
                R.string.google_api_connection_error,
                Snackbar.LENGTH_LONG);
    }

    public GpsEstablisher createGpsEstablisher(Tag tag) {
        Snackbar googleApiFailureSnackbar = createGpsSnackBar();
        GpsEstablisher.StatusListener gpsStatusListener = new GpsEstablisher.StatusListener() {
            @Override
            public void onGpsEstablished() {
                EditActivityHelper.this.openPlacePicker(tag);
            }

            @Override
            public void onGoogleLocationApiConnectionFailure() {
                googleApiFailureSnackbar.show();
            }
        };

        LocationManager locationManager = (LocationManager) editActivity.getSystemService(LOCATION_SERVICE);
        GoogleLocationApiEstablisher googleLocationApiEstablisher =
                new GoogleLocationApiEstablisher(
                        new GoogleApiClient.Builder(editActivity));
        GpsEstablisher gpsEstablisher = new GpsEstablisher(locationManager, googleLocationApiEstablisher, editActivity);
        gpsEstablisher.setStatusListener(gpsStatusListener);
        return gpsEstablisher;
    }

    public Intent buildShareImageIntent(ImageDetails imageDetails, ContentResolver contentResolver) throws FileNotFoundException {
        final String imagePath = prepareImagePathToShare(imageDetails, contentResolver);
        final Uri imageUri = Uri.parse(imagePath);
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType(SHARE_IMAGE_JPG_TYPE);

        return intent;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public String prepareImagePathToShare(ImageDetails imageDetails, ContentResolver contentResolver) throws FileNotFoundException {
        return insertImage(
                contentResolver,
                imageDetails.getPath(),
                imageDetails.getName(),
                IMAGE_EMPTY_DESCRIPTION);
    }

    /**
     * Clears activity reference.
     * Cannot use this class after calling this method.
     */
    public void onStop() {
        editActivity = null;
    }
}
