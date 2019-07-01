package com.gox.partner.views.edit_service_price

import android.text.Editable
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.Constants.FareType.DISTANCE_TIME
import com.gox.base.data.Constants.FareType.FIXED
import com.gox.base.data.Constants.FareType.HOURLY
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.EditServicePriceDialogBinding
import com.gox.partner.views.set_service_category_price.SetServicePriceActivity
import com.gox.partner.views.set_service_category_price.SetServicePriceViewModel
import kotlinx.android.synthetic.main.edit_service_price_dialog.*

class EditServicePriceDialogFragment : BaseDialogFragment<EditServicePriceDialogBinding>(), EditServicePriceNavigator {

    private lateinit var mBinding: EditServicePriceDialogBinding
    private lateinit var mViewModel: EditServicePriceViewModel

    override fun getLayout() = R.layout.edit_service_price_dialog

    var fareType = ""

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as EditServicePriceDialogBinding
        mViewModel = EditServicePriceViewModel()
        mViewModel.navigator = this
        mBinding.editServicePriceViewModel = mViewModel
        val setServicePriceViewModel =
                ViewModelProviders.of(activity!!).get(SetServicePriceViewModel::class.java)
        setServicePriceViewModel.dialogPrice.observe(this, Observer {
            fareType = it.fareType.toString()
            when (it.fareType) {
                DISTANCE_TIME -> {
                    miles_lt.visibility = VISIBLE
                    price_miles_edt.text = Editable.Factory.getInstance().newEditable(it.perMiles.toString())
                    price_edt.text = Editable.Factory.getInstance().newEditable(it.perMin.toString())
                }
                FIXED -> {
                    price_edt.text = Editable.Factory.getInstance().newEditable(it.baseFare.toString())
                    label.text = getString(R.string.fixed)
                }
                HOURLY -> {
                    price_edt.text = Editable.Factory.getInstance().newEditable(it.perMin.toString())
                }
            }
        })

        cancel_txt.setOnClickListener { dismiss() }

        submit_txt.setOnClickListener {
            val service = SetServicePriceActivity.SelectedService()
            if (price_edt.text.isNotEmpty() && price_edt.text.toString().toDouble() > 0) {
                if (miles_lt.visibility == VISIBLE) if (price_miles_edt.text.isNotEmpty()
                        && price_miles_edt.text.toString().toDouble() > 0) {
                    service.perMiles = Integer.parseInt(price_miles_edt.text.toString())
                    price_miles_edt.setText(service.perMiles.toString())
                    service.perMiles = Integer.parseInt(price_miles_edt.text.toString())
                } else {
                    ViewUtils.showToast(activity!!, getString(R.string.enter_amount), false)
                    return@setOnClickListener
                }
                if (fareType == FIXED)
                    service.baseFare = Integer.parseInt(price_edt.text.toString())
                else service.perMin = Integer.parseInt(price_edt.text.toString())
                service.fareType = fareType
                setServicePriceViewModel.listPrice.value = service
                dismiss()
            } else ViewUtils.showToast(activity!!, getString(R.string.enter_amount), false)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}