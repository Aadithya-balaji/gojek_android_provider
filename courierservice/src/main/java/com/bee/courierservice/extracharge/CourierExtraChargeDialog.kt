package com.bee.courierservice.extracharge

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup

import androidx.databinding.ViewDataBinding
import com.bee.courierservice.R
import com.bee.courierservice.databinding.CDialogCourierExtraChargeBinding
import com.bee.courierservice.interfaces.GetExtraChargeInterface
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.base.utils.PrefixCustomEditText

class CourierExtraChargeDialog : BaseDialogFragment<CDialogCourierExtraChargeBinding>(),
        CourierExtraChargeNavigator {

    private lateinit var mBinding: CDialogCourierExtraChargeBinding
    private lateinit var mViewModel: CourierExtraChargeViwModel

    companion object {
        private var extraAmountInterface: GetExtraChargeInterface? = null
        fun newInstance(extraChargeInterface: GetExtraChargeInterface): CourierExtraChargeDialog {
            extraAmountInterface = extraChargeInterface
            return CourierExtraChargeDialog()
        }
    }

    override fun getLayout() = R.layout.c_dialog_courier_extra_charge

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as CDialogCourierExtraChargeBinding
        mViewModel = CourierExtraChargeViwModel()
        mViewModel.navigator = this
        mBinding.extraChargeModel = mViewModel
        mBinding.lifecycleOwner = this
        mBinding.edtExtraAmount.addTextChangedListener(EditListener())
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun submit() {
        extraAmountInterface!!.getExtraCharge(mViewModel.extraAmount.value.toString(),
                mViewModel.extraAmountNotes.value.toString())
        dialog!!.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    inner class EditListener : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setPrefix(mBinding.edtExtraAmount, s, readPreferences(PreferencesKey.CURRENCY_SYMBOL))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    fun setPrefix(editText: PrefixCustomEditText, s: Editable?, strPref: String) =
            if (s.toString().isNotEmpty()) editText.setPrefix(strPref) else editText.setPrefix("")
}