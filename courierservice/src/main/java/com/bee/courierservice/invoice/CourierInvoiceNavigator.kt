package com.bee.courierservice.invoice

import com.bee.courierservice.model.ResponseData

interface  CourierInvoiceNavigator{
     fun showErrorMessage(error:String)
     fun submit(id:ResponseData?)
     fun showExtraChargePage()
     fun showRating()
}