package com.xjek.taxiservice.views.tollcharge

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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.PrefixCustomEditText
import com.xjek.base.utils.ViewUtils
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.DialogTollChargeBinding
import com.xjek.taxiservice.views.main.TaxiDashboardViewModel

class TollChargeDialog : BaseDialogFragment<DialogTollChargeBinding>(), TollChargeNavigator {

    private lateinit var dialogTollChargeBinding: DialogTollChargeBinding
    private lateinit var mViewModel: TollChargeViewModel
    private lateinit var mDashboardViewModel: TaxiDashboardViewModel

    override fun getLayout(): Int = R.layout.dialog_toll_charge

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.share_dialog)
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogTollChargeBinding = viewDataBinding as DialogTollChargeBinding
        mViewModel = TollChargeViewModel()
        mDashboardViewModel = ViewModelProviders.of(activity!!).get(TaxiDashboardViewModel::class.java)

        mViewModel.navigator = this
        dialogTollChargeBinding.tollmodel = mViewModel
        dialogTollChargeBinding.lifecycleOwner = this
        dialogTollChargeBinding.edtAmount.addTextChangedListener(EditListener())
        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>

        val strRequestID = if (arguments != null && arguments!!.containsKey("requestID")) arguments!!.getString("requestID") else ""

        if (strRequestID.isNotEmpty()) mViewModel.requestID.value = strRequestID

        observeLiveData(mViewModel.mLiveData) {
            if (mViewModel.mLiveData.value != null)
                if (mViewModel.mLiveData.value!!.statusCode == "200") {
                    activity!!.runOnUiThread {
                        mViewModel.showLoading.value = false
                        dialog!!.dismiss()
                        mDashboardViewModel.callTaxiCheckStatusAPI()
                    }
                }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE)
            val params = dialog!!.window?.attributes
            params!!.horizontalMargin = 150.0f
            dialog!!.window?.attributes = params
        }

        return super.onCreateView(inflater, container, savedInstanceState)
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
        println("RRR :: s = [$s], strPref = [$strPref]")
        mViewModel.tollChargeLiveData.value = s.toString()
        if (s.toString().isNotEmpty()) editText.setPrefix(strPref) else editText.setPrefix("")
    }

    override fun showErrorMessage(error: String) {
        activity!!.runOnUiThread {
            mViewModel.showLoading.value = false
            ViewUtils.showToast(activity!!, error, false)
        }
    }

    override fun isValidCharge(): Boolean {
        return when {
            mViewModel.tollChargeLiveData.value.isNullOrEmpty() -> {
                showErrorMessage(activity!!.getString(R.string.empty_toll_charge))
                false
            }
            mViewModel.tollChargeLiveData.value!!.toInt() <= 0 -> {
                showErrorMessage(activity!!.getString(R.string.invalid_toll_charge))
                false
            }
            else -> true
        }
    }
}