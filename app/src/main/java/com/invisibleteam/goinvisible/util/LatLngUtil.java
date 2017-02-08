package com.invisibleteam.goinvisible.util;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

public class LatLngUtil {
    public static LatLngBounds generateBoundsWithZoom(
            LatLng center,
            double visibleRadiusInKm) {
        LatLng southwest = SphericalUtil.computeOffset(center,
                visibleRadiusInKm * Math.sqrt(2.0), 225); // 225 - right, bottom corner
        LatLng northeast = SphericalUtil.computeOffset(center,
                visibleRadiusInKm * Math.sqrt(2.0), 45); // 45 - left, top corner
        return new LatLngBounds(southwest, northeast);
    }
}
