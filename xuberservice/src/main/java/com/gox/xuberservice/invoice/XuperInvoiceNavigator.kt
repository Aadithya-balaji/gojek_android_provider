package com.gox.xuberservice.invoice

interface  XuperInvoiceNavigator{
     fun showErrorMessage(error:String)
     fun submit()
     fun showExtraChargePage()
     fun showRating()
}