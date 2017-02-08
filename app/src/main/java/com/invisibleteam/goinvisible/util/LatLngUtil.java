package com.invisibleteam.goinvisible.util;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

public class LatLngUtil {

    private static final int ANGLE_RIGHT_BOTTOM = 225;
    private static final int ANGLE_LEFT_TOP = 45;

    public static LatLngBounds generateBoundsWithZoom(
            LatLng center,
            double visibleRadiusInKm) {
        LatLng southwest = SphericalUtil.computeOffset(center,
                visibleRadiusInKm * Math.sqrt(2.0), ANGLE_RIGHT_BOTTOM);
        LatLng northeast = SphericalUtil.computeOffset(center,
                visibleRadiusInKm * Math.sqrt(2.0), ANGLE_LEFT_TOP);
        return new LatLngBounds(southwest, northeast);
    }
}
