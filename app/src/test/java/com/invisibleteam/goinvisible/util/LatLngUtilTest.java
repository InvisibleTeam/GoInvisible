package com.invisibleteam.goinvisible.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;

import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(ZohhakRunner.class)
public class LatLngUtilTest {

    @TestWith({
            "17.5,      17.5,       20000,      17.679774668960636,     17.688780123834924",
            "0,         0,          1000,       0.00899320331800169,    0.008993203428783377"
    })
    public void whenLatLngAndRangedAreProvided_CorrectBoundsAreGenerated(double lat, double lng, long radius, double boundsLat, double boundsLng) {
        //when
        LatLngBounds bounds = LatLngUtil.generateBoundsWithZoom(new LatLng(lat, lng), radius);

        //then
        assertEquals(boundsLat, bounds.northeast.latitude, 0.000000001);
        assertEquals(boundsLng, bounds.northeast.longitude, 0.000000001);
    }

}