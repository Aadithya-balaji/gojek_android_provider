package com.xgek.xubermodule.ratingFragment

import androidx.lifecycle.ViewModel;
import com.appoets.basemodule.base.BaseViewModel

class RatingViewModel : BaseViewModel<RatingNavigator>() {

    fun submitRating()
    {
        navigator.submitRating()
    }
}
