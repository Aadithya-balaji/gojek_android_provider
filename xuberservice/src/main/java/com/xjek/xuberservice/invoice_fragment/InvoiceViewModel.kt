package com.xjek.xuberservice.invoice_fragment

import com.xjek.base.base.BaseViewModel

class InvoiceViewModel : BaseViewModel<InvoiceNavigator>() {

    fun confrimPayment()
    {
        navigator.confrimPayment()
    }

   /* fun openTollDialog(){
        navigator.showTollDialog()
    }*/
}
