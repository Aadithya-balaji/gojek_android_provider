package com.bee.courierservice.invoice

interface  CourierInvoiceNavigator{
     fun showErrorMessage(error:String)
     fun submit()
     fun showExtraChargePage()
     fun showRating()
}