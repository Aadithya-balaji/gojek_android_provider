package com.gox.base.utils;

import android.util.Patterns;

public final class ValidationUtils {

    private ValidationUtils(){

    }


    public static boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isMinLength(String input,int minLength){
        return (minLength >= input.length());
    }

}

