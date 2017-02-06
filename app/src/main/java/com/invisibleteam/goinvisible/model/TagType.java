package com.invisibleteam.goinvisible.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;

public class TagType implements Parcelable {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    static final String REGEXP_VALUE_INTEGER = "0|^[1-9]([0-9]{0,5})$";
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    static final String REGEXP_VALUE_DOUBLE = "^(0|[1-9]([0-9]{0,5}))(\\.[0-9]{1,6}){0,1}$";
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    static final String REGEXP_TEXT_STRING = ".*";
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    static final String REGEXP_DATE_STRING = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    static final String REGEXP_TIMESTAMP_STRING = "^([01][0-9]|2[0-3])\\:([0-5][0-9])\\:([0-5][0-9])$";
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    static final String REGEXP_DATETIME_STRING;
    private static final String REGEXP_EMPTY = "";

    static {
        REGEXP_DATETIME_STRING =
                "^\\d{4}\\-(0?[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])" +
                        " ([01][0-9]|2[0-3])\\:([0-5][0-9])\\:([0-5][0-9])$";
    }

    public static final Parcelable.Creator<TagType> CREATOR = new Parcelable.Creator<TagType>() {
        @Override
        public TagType createFromParcel(Parcel in) {
            return new TagType(in);
        }

        @Override
        public TagType[] newArray(int size) {
            return new TagType[size];
        }
    };

    public static TagType build(InputType inputType) {
        switch (inputType) {
            case TEXT_STRING:
                return new TagType(inputType, REGEXP_TEXT_STRING);
            case DATE_STRING:
                return new TagType(inputType, REGEXP_DATE_STRING);
            case TIMESTAMP_STRING:
                return new TagType(inputType, REGEXP_TIMESTAMP_STRING);
            case DATETIME_STRING:
                return new TagType(inputType, REGEXP_DATETIME_STRING);
            case RANGED_STRING:
                return new TagType(inputType, REGEXP_TEXT_STRING);
            case RANGED_INTEGER:
                return new TagType(inputType, REGEXP_VALUE_INTEGER);
            case VALUE_INTEGER:
                return new TagType(inputType, REGEXP_VALUE_INTEGER);
            case VALUE_DOUBLE:
                return new TagType(inputType, REGEXP_VALUE_DOUBLE);
            case POSITION_DOUBLE:
                return new TagType(inputType, REGEXP_VALUE_DOUBLE);
            default:
                return new TagType(InputType.INDEFINITE, REGEXP_EMPTY);
        }
    }

    private final InputType inputType;
    private final String validationRegexp;

    private TagType(InputType inputType, String validationRegexp) {
        this.inputType = inputType;
        this.validationRegexp = validationRegexp;
    }

    public InputType getInputType() {
        return inputType;
    }

    public String getValidationRegexp() {
        return validationRegexp;
    }

    protected TagType(Parcel in) {
        inputType = (InputType) in.readValue(InputType.class.getClassLoader());
        validationRegexp = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(inputType);
        dest.writeString(validationRegexp);
    }
}
