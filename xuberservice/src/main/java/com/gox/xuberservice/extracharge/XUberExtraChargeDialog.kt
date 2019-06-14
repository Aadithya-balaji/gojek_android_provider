package com.gox.xuberservice.extracharge

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseDialogFragment
import com.gox.base.utils.PrefixCustomEditText
import com.gox.xuberservice.R
import com.gox.xuberservice.databinding.DialogXuperExtraChargeBinding
import com.gox.xuberservice.interfaces.DialogCloseInterface
import com.gox.xuberservice.interfaces.GetExtraChargeInterface

class XUberExtraChargeDialog : BaseDialogFragment<DialogXuperExtraChargeBinding>(),
        XUberExtraChargeNavigator {

    private lateinit var mBinding: DialogXuperExtraChargeBinding
    private lateinit var mViewModel: XUberExtraChargeViwModel

    companion object {
        private lateinit var onDismissListener: DialogCloseInterface
        private var extraAmountInterface: GetExtraChargeInterface? = null
        fun newInstance(extraChargeInterface: GetExtraChargeInterface,
                        onDialogCancelListener: DialogCloseInterface): XUberExtraChargeDialog {
            extraAmountInterface = extraChargeInterface
            onDismissListener = onDialogCancelListener
            return XUberExtraChargeDialog()
        }
    }

    override fun getLayout() = R.layout.dialog_xuper_extra_charge

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as DialogXuperExtraChargeBinding
        mViewModel = XUberExtraChargeViwModel()
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener.hideDialog(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    inner class EditListener : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setPrefix(mBinding.edtExtraAmount, s, "$")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    fun setPrefix(editText: PrefixCustomEditText, s: Editable?, strPref: String) =
            if (s.toString().isNotEmpty()) editText.setPrefix(strPref) else editText.setPrefix("")
}
