package com.xjek.xuberservice.ratingFragment

import com.xjek.base.base.BaseViewModel

class RatingViewModel : BaseViewModel<RatingNavigator>() {

    fun submitRating()
    {
        navigator.submitRating()
    }
}
