package com.appoets.gojek.provider.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class Enums {

    public static final int DOC_TAXI = 0;
    public static final int DOC_FOODIE = 1;
    public static final int DOC_SERVICE = 2;

    public static final int DOC_TYPE_LICENSE = 1;
    public static final int DOC_TYPE_BIRTH = 2;

    public static final int RC_COUNTRY_CODE_PICKER = 100;
    public static final int RC_GOOGLE_SIGN_IN = 101;

    @IntDef({DOC_TAXI, DOC_FOODIE, DOC_SERVICE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ServiceType {
    }

    @IntDef({DOC_TYPE_LICENSE, DOC_TYPE_BIRTH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DocumentType {

    }

    @IntDef({RC_GOOGLE_SIGN_IN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RequestCode {

    }
}
