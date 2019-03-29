package com.xjek.xuberservice.ratingFragment

import com.appoets.base.base.BaseViewModel

class RatingViewModel : BaseViewModel<RatingNavigator>() {

    fun submitRating()
    {
        navigator.submitRating()
    }
}
