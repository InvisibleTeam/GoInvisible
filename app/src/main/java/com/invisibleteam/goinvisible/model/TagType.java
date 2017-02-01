package com.invisibleteam.goinvisible.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TagType implements Parcelable {

    private static final String REGEXP_TEXT_STRING = "";
    private static final String REGEXP_DATE_STRING = "";
    private static final String REGEXP_BOOLEAN_INTEGER = "";
    private static final String REGEXP_VALUE_INTEGER = "";
    private static final String REGEXP_POSITION_DOUBLE = "";
    private static final String REGEXP_VALUE_DOUBLE = "";

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
            case BOOLEAN_INTEGER:
                return new TagType(inputType, REGEXP_BOOLEAN_INTEGER);
            case VALUE_INTEGER:
                return new TagType(inputType, REGEXP_VALUE_INTEGER);
            case VALUE_DOUBLE:
                return new TagType(inputType, REGEXP_VALUE_DOUBLE);
            case POSITION_DOUBLE:
                return new TagType(inputType, REGEXP_POSITION_DOUBLE);
            default:
                throw new IllegalStateException("Wrong input type");
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
