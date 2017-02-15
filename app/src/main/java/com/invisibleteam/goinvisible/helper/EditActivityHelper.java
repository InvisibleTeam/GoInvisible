package com.invisibleteam.goinvisible.helper;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.NonNull;
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
import com.invisibleteam.goinvisible.model.Tag;
import com.invisibleteam.goinvisible.mvvm.edition.GoogleLocationApiEstablisher;
import com.invisibleteam.goinvisible.mvvm.edition.GpsEstablisher;
import com.invisibleteam.goinvisible.util.LatLngUtil;
import com.invisibleteam.goinvisible.util.TagUtil;

import static android.content.Context.LOCATION_SERVICE;
import static com.invisibleteam.goinvisible.mvvm.edition.EditActivity.PLACE_REQUEST_ID;

public class EditActivityHelper {

    private static final int initialMapRadius = 20000;

    @NonNull
    public static GeolocationTag prepareNewPlacePositionTag(Place place, Tag tag) {
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

    public static void openPlacePicker(Tag tag, Activity activity) {
        try {
            GeolocationTag geolocationTag = (GeolocationTag) tag;
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            if (geolocationTag.getValue() != null) {
                LatLng center = new LatLng(
                        TagUtil.parseRationalGPSToDoubleGPS(geolocationTag.getValue()),
                        TagUtil.parseRationalGPSToDoubleGPS(geolocationTag.getSecondValue())
                );
                LatLngBounds bounds = LatLngUtil.generateBoundsWithZoom(center, initialMapRadius);
                intentBuilder.setLatLngBounds(bounds);
            }

            Intent intent = intentBuilder.build(activity);
            activity.startActivityForResult(intent, PLACE_REQUEST_ID);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(activity, e.getConnectionStatusCode(), 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(activity, R.string.google_services_error,
                    Toast.LENGTH_LONG).show();
        }
    }

    public static Snackbar createGpsSnackBar(Activity activity) {
        return Snackbar.make(
                activity.findViewById(android.R.id.content),
                R.string.google_api_connection_error,
                Snackbar.LENGTH_LONG);
    }

    public static GpsEstablisher createGpsEstablisher(Activity activity, GpsEstablisher.StatusListener listener) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
        GoogleLocationApiEstablisher googleLocationApiEstablisher =
                new GoogleLocationApiEstablisher(
                        new GoogleApiClient.Builder(activity));
        GpsEstablisher gpsEstablisher = new GpsEstablisher(locationManager, googleLocationApiEstablisher, activity);
        gpsEstablisher.setStatusListener(listener);
        return gpsEstablisher;
    }
}
