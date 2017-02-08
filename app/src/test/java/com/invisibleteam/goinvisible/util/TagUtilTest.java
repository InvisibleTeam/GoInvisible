package com.invisibleteam.goinvisible.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class TagUtilTest {

    @Test
    public void whenDoubleGPSIsGiven_GeoGPSWillBeReturn() {
        //given
        double gpsValue = 17.071711;

        //when
        String result = TagUtil.parseDoubleGPSToGeoGPS(gpsValue);

        //then
        String expectedValue = "17" + TagUtil.DEGREE_CHAR + " 4\' 18\'\'";
        assertThat(result, is(expectedValue));
    }

    @Test
    public void whenRationalGPSIsGiven_GeoGPSWillBeReturn() {
        //given
        String gpsValue = "17/1,4/1,1816/100";
        String gpsValueReference = "N";

        //when
        String result = TagUtil.parseRationalGPSToGeoGPS(gpsValue, gpsValueReference);

        //then
        String expectedValue = "17" + TagUtil.DEGREE_CHAR + " 4\' 18\'\' N";
        assertThat(result, is(expectedValue));
    }

    @Test
    public void whenNullRationalGPSIsGiven_NullValueWillBeReturn() {
        //given
        String gpsValue = null;
        String gpsValueReference = "N";

        //when
        String result = TagUtil.parseRationalGPSToGeoGPS(gpsValue, gpsValueReference);

        //then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenEmptyRationalGPSIsGiven_NullValueWillBeReturn() {
        //given
        String gpsValue = "";
        String gpsValueReference = "N";

        //when
        String result = TagUtil.parseRationalGPSToGeoGPS(gpsValue, gpsValueReference);

        //then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenIncorrectRationalGPSIsGiven_NullValueWillBeReturn() {
        //given
        String gpsValue = "18.3";
        String gpsValueReference = "N";

        //when
        String result = TagUtil.parseRationalGPSToGeoGPS(gpsValue, gpsValueReference);

        //then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenDoubleGPSIsGiven_RationalGPSWillBeReturn() {
        //given
        double gpsValue = 17.071711;

        //when
        String result = TagUtil.parseDoubleGPSToRationalGPS(gpsValue);

        //then
        String expectedValue = "17/1,4/1,18159600/1000000";
        assertThat(result, is(expectedValue));
    }

    @Test
    public void whenRationalValueIsGiven_StringValueWillBeReturn() {
        //given
        String rational = "18/5";

        //when
        String result = TagUtil.parseRationalToString(rational);

        //then
        assertThat(result, is(String.valueOf(3.6)));
    }

    @Test
    public void whenNullRationalValueIsGiven_NullValueWillBeReturn() {
        //given
        String rational = null;

        //when
        String result = TagUtil.parseRationalToString(rational);

        //then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenEmptyRationalValueIsGiven_NullValueWillBeReturn() {
        //given
        String rational = "";

        //when
        String result = TagUtil.parseRationalToString(rational);

        //then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenIncorrectRationalValueIsGiven_NullValueWillBeReturn() {
        //given
        String rational = "34#2";

        //when
        String result = TagUtil.parseRationalToString(rational);

        //then
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenRationalValueIsGiven_DoubleValueWillBeReturn() {
        //given
        String rational = "18/5";

        //when
        double result = TagUtil.parseRationalValueToDouble(rational);

        //then
        assertThat(result, is(3.6));
    }

}