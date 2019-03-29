package com.xjek.xuberservice.invoice_fragment

import com.appoets.base.base.BaseViewModel

class InvoiceViewModel : BaseViewModel<InvoiceNavigator>() {

    fun confrimPayment()
    {
        navigator.confrimPayment()
    }
}
