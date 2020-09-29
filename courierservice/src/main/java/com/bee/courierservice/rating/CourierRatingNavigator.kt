package com.bee.courierservice.rating

interface  CourierRatingNavigator{
    fun submitRating()
    fun showErrorMessage(error:String)
}