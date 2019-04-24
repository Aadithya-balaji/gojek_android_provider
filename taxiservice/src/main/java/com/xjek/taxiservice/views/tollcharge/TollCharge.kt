package com.xjek.taxiservice.views.tollcharge

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.utils.PrefixCustomEditText
import com.xjek.base.utils.ViewUtils
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.DialogTollChargeBinding
import com.xjek.taxiservice.views.invoice.InvoiceNavigator


class TollCharge : BaseDialogFragment<DialogTollChargeBinding>(), TollChargeNavigator {


    private lateinit var dialogTollChargeBinding: DialogTollChargeBinding
    private lateinit var tollChargeViewModel: TollChargeViewModel
    private lateinit var invoiceNavigator: InvoiceNavigator
    private lateinit var strRequestID: String


    override fun getLayout(): Int {
        return com.xjek.taxiservice.R.layout.dialog_toll_charge
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.share_dialog)
        getBundleValues()
    }

    fun getBundleValues() {
        strRequestID = if (arguments != null && arguments!!.containsKey("requestID")) arguments!!.getString("requestID") else ""
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogTollChargeBinding = viewDataBinding as DialogTollChargeBinding
        tollChargeViewModel = TollChargeViewModel()
        tollChargeViewModel.navigator = this
        dialogTollChargeBinding.tollmodel = tollChargeViewModel
        dialogTollChargeBinding.setLifecycleOwner(this)
        dialogTollChargeBinding.edtAmount.addTextChangedListener(EditListener())
        tollChargeViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>
        if (!strRequestID.isNullOrEmpty()) {
            tollChargeViewModel.requestID.value = strRequestID
        }
    }

    override fun addTollCharge() {
        tollChargeViewModel.callUpdateRequestApi()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (dialog != null && dialog!!.getWindow() != null) {
            dialog!!.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            dialog!!.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            val params = dialog!!.getWindow().attributes
            params.horizontalMargin = 150.0f
            dialog!!.window.attributes = params

        }

        return super.onCreateView(inflater, container, savedInstanceState)
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

    override fun dismissDialog() {
        dialog!!.dismiss()
    }

    override fun showErrorMessage(error: String) {
        ViewUtils.showToast(activity!!, error, false)
    }

    override fun isValidCharge(): Boolean {
        if (tollChargeViewModel.tollChargeLiveData.value.isNullOrEmpty()) {
            showErrorMessage(activity!!.getString(R.string.empty_toll_charge))
            return false
        } else if (tollChargeViewModel.tollChargeLiveData.value!!.toInt() <= 0) {
            showErrorMessage(activity!!.getString(R.string.invalid_toll_charge))
            return false
        } else {
            return true
        }
    }

}