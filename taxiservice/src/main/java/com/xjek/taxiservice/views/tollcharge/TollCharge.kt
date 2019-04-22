package com.xjek.taxiservice.views.tollcharge

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.utils.PrefixCustomEditText
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.DialogTollChargeBinding
import com.xjek.taxiservice.views.invoice.InvoiceNavigator

class TollCharge : BaseDialogFragment<DialogTollChargeBinding>(), TollChargeNavigator {


    private lateinit var dialogTollChargeBinding: DialogTollChargeBinding
    private lateinit var tollChargeViewModel: TollChargeViewModel
    private lateinit var invoiceNavigator: InvoiceNavigator


    override fun getLayout(): Int {
        return R.layout.dialog_toll_charge
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogTollChargeBinding = viewDataBinding as DialogTollChargeBinding
        tollChargeViewModel = TollChargeViewModel()
        tollChargeViewModel.navigator = this
        dialogTollChargeBinding.tollmodel = tollChargeViewModel
        dialogTollChargeBinding.setLifecycleOwner(this)
        dialogTollChargeBinding.edtAmount.addTextChangedListener(EditListener())
    }

    override fun addTollCharge() {
        invoiceNavigator.tollCharge(tollChargeViewModel.tollChargeLiveData.value.toString())
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        invoiceNavigator = context as InvoiceNavigator
    }

    inner class EditListener : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setPrefix(dialogTollChargeBinding.edtAmount, s, "$")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    fun setPrefix(editText: PrefixCustomEditText, s: Editable?, strPref: String) {
        if (s.toString().length > 0) {
            editText.setPrefix(strPref)
        } else {
            editText.setPrefix("")
        }
    }

}