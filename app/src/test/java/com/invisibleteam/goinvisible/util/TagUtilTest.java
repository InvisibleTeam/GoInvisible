package com.invisibleteam.goinvisible.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(RobolectricTestRunner.class)
public class TagUtilTest {

    @Test
    public void whenDoubleGPSIsGiven_GeoGPSIsReturned() {
        //Given
        double gpsValue = 17.071711;

        //When
        String result = TagUtil.parseDoubleGPSToGeoGPS(gpsValue);

        //Then
        String expectedValue = "17" + TagUtil.DEGREE_CHAR + " 4\' 18\'\'";
        assertThat(result, is(expectedValue));
    }

    @Test
    public void whenRationalGPSIsGiven_GeoGPSIsReturned() {
        //Given
        String gpsValue = "17/1,4/1,1816/100";
        String gpsValueReference = "N";

        //When
        String result = TagUtil.parseRationalGPSToGeoGPS(gpsValue, gpsValueReference);

        //Then
        String expectedValue = "17" + TagUtil.DEGREE_CHAR + " 4\' 18\'\' N";
        assertThat(result, is(expectedValue));
    }

    @Test
    public void whenNullRationalGPSIsGiven_NullValueIsReturned() {
        //Given
        String gpsValue = null;
        String gpsValueReference = "N";

        //When
        String result = TagUtil.parseRationalGPSToGeoGPS(gpsValue, gpsValueReference);

        //Then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenRationalGPSIsGivenWithNullRationalRef_GeoGPSValueWithEmptyRefIsReturned() {
        //Given
        String gpsValue = "17/1,4/1,1816/100";
        String gpsValueReference = null;

        //When
        String result = TagUtil.parseRationalGPSToGeoGPS(gpsValue, gpsValueReference);

        //Then
        String expectedValue = "17" + TagUtil.DEGREE_CHAR + " 4\' 18\'\'";
        assertThat(result, is(expectedValue));
    }

    @Test
    public void whenEmptyRationalGPSIsGiven_NullValueIsReturned() {
        //Given
        String gpsValue = "";
        String gpsValueReference = "N";

        //When
        String result = TagUtil.parseRationalGPSToGeoGPS(gpsValue, gpsValueReference);

        //Then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenIncorrectRationalGPSIsGiven_NullValueIsReturned() {
        //Given
        String gpsValue = "18.3";
        String gpsValueReference = "N";

        //When
        String result = TagUtil.parseRationalGPSToGeoGPS(gpsValue, gpsValueReference);

        //Then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenDoubleGPSIsGiven_RationalGPSIsReturned() {
        //Given
        double gpsValue = 17.071711;

        //When
        String result = TagUtil.parseDoubleGPSToRationalGPS(gpsValue);

        //Then
        String expectedValue = "17/1,4/1,18159600/1000000";
        assertThat(result, is(expectedValue));
    }

    @Test
    public void whenRationalValueIsGiven_StringValueIsReturned() {
        //Given
        String rational = "18/5";

        //When
        String result = TagUtil.parseRationalToString(rational);

        //Then
        assertThat(result, is(String.valueOf(3.6)));
    }

    @Test
    public void whenNullRationalValueIsGiven_NullValueIsReturned() {
        //Given
        String rational = null;

        //When
        String result = TagUtil.parseRationalToString(rational);

        //Then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenEmptyRationalValueIsGiven_NullValueIsReturned() {
        //Given
        String rational = "";

        //When
        String result = TagUtil.parseRationalToString(rational);

        //Then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenIncorrectRationalValueIsGiven_NullValueIsReturned() {
        //Given
        String rational = "34#2";

        //When
        String result = TagUtil.parseRationalToString(rational);

        //Then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenRationalValueIsGiven_DoubleValueIsReturned() {
        //Given
        String rational = "18/5";

        //When
        double result = TagUtil.parseRationalValueToDouble(rational);

        //Then
        assertThat(result, is(3.6));
    }

    @Test
    public void whenNullRationalValueIsGiven_DoubleZeroValueIsReturned() {
        //Given
        String rational = null;

        //When
        double result = TagUtil.parseRationalValueToDouble(rational);

        //Then
        assertThat(result, is(0.0));
    }

    @Test
    public void whenDoubleValueIsGiven_RationalValueIsReturned() {
        //Given
        double value = 1.6;

        //When
        String result = TagUtil.parseDoubleValueToRational(value);

        //Then
        assertThat(result, is("1600000/1000000"));
    }

}