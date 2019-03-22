package com.xgek.xubermodule.invoice_fragment

import androidx.lifecycle.ViewModel;
import com.appoets.basemodule.base.BaseViewModel

class InvoiceViewModel : BaseViewModel<InvoiceNavigator>() {

    fun confrimPayment()
    {
        navigator.confrimPayment()
    }
}
