package com.gox.xuberservice.extracharge

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.gox.base.base.BaseDialogFragment
import com.gox.base.utils.PrefixCustomEditText
import com.gox.xuberservice.R
import com.gox.xuberservice.databinding.DialogXuperExtraChargeBinding
import com.gox.xuberservice.interfaces.DialogCloseInterface
import com.gox.xuberservice.interfaces.GetExtraChargeInterface

class  DialogXuperExtraCharge:BaseDialogFragment<DialogXuperExtraChargeBinding>(),XuperExtraChargeNavigator,DialogInterface {
    private  lateinit var  dialogXuperExtraChargeBinding: DialogXuperExtraChargeBinding
    private  lateinit var  xuperExtraChargeViewModel:XuperExtraChargeViwModel

    companion object {
        private  lateinit var  onDismissListener:DialogCloseInterface
        private  var extraAmountInterface:GetExtraChargeInterface?=null
        fun newInstance(extraChargeInterface: GetExtraChargeInterface,onDialogCancelListener:DialogCloseInterface): DialogXuperExtraCharge {
            val fragment = DialogXuperExtraCharge()
            extraAmountInterface=extraChargeInterface
            onDismissListener=onDialogCancelListener
            return fragment
        }
    }
    override fun getLayout(): Int {
        return R.layout.dialog_xuper_extra_charge
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogXuperExtraChargeBinding=viewDataBinding as DialogXuperExtraChargeBinding
        xuperExtraChargeViewModel= XuperExtraChargeViwModel()
        xuperExtraChargeViewModel.navigator=this
        dialogXuperExtraChargeBinding.extraChargeModel=xuperExtraChargeViewModel
        dialogXuperExtraChargeBinding.setLifecycleOwner(this)
        dialogXuperExtraChargeBinding.edtExtraAmount.addTextChangedListener(EditListener())
    }


    override fun onStart() {
        super.onStart()
        getDialog()!!.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun submit() {
        extraAmountInterface!!.getExtraCharge(xuperExtraChargeViewModel.extraAmount.value.toString(),xuperExtraChargeViewModel.extraAmountNotes.value.toString())
        dialog!!.dismiss()
    }

    override fun cancel() {

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener.hideDialog(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog)
    }


    inner class EditListener : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setPrefix(dialogXuperExtraChargeBinding.edtExtraAmount, s, "$")
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
