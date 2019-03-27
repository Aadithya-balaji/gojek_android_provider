package com.appoets.gojek.provider.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class Enums {

    public static final int DOC_TAXI = 0;
    public static final int DOC_FOODIE = 1;
    public static final int DOC_SERVICE = 2;

   @IntDef({DOC_TAXI,DOC_FOODIE,DOC_SERVICE})
   @Retention(RetentionPolicy.SOURCE)
    public  @interface ServiceType{
   }
}
