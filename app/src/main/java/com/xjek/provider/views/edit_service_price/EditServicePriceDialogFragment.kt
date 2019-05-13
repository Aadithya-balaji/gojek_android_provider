package com.xjek.provider.views.edit_service_price

import android.text.Editable
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.EditServicePriceDialogBinding
import com.xjek.provider.views.set_service_category_price.SetServicePriceViewModel
import kotlinx.android.synthetic.main.edit_service_price_dialog.*

class EditServicePriceDialogFragment : BaseDialogFragment<EditServicePriceDialogBinding>(), EditServicePriceNavigator {

    private lateinit var binding: EditServicePriceDialogBinding
    private lateinit var viewModel: EditServicePriceViewModel

    override fun getLayout() = R.layout.edit_service_price_dialog

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        binding = viewDataBinding as EditServicePriceDialogBinding
        viewModel = EditServicePriceViewModel()
        viewModel.navigator = this
        binding.editServicePriceViewModel = viewModel
        val setServicePriceViewModel = ViewModelProviders.of(activity!!).get(SetServicePriceViewModel::class.java)
        setServicePriceViewModel.dialogPrice.observe(this, Observer {
            price_edt.text = Editable.Factory.getInstance().newEditable(it)
        })
        cancel_txt.setOnClickListener { dismiss() }
        submit_txt.setOnClickListener {
            if (price_edt.text.isNotEmpty() && price_edt.text.toString().toDouble() > 0) {
                setServicePriceViewModel.price.value = price_edt.text.toString()
                dismiss()
            } else {
                ViewUtils.showToast(activity!!, getString(R.string.enter_amount), false)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
