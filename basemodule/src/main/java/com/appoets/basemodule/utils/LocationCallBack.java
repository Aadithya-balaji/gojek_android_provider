package com.appoets.basemodule.utils;

import android.location.Location;

public interface LocationCallBack {


    interface LastKnownLocation {
        void onSuccess(Location location);
        void onFailure(String messsage);
    }


}
