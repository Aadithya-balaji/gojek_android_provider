package com.gox.taxiservice.views.tollcharge

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
import com.google.gson.Gson
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.PrefixCustomEditText
import com.gox.base.utils.ViewUtils
import com.gox.taxiservice.R
import com.gox.taxiservice.databinding.DialogTollChargeBinding
import com.gox.taxiservice.model.DroppedStatusModel
import com.gox.taxiservice.model.LocationPoint
import com.gox.taxiservice.views.main.TaxiDashboardViewModel
import kotlinx.android.synthetic.main.dialog_toll_charge.*

class TollChargeDialog : BaseDialogFragment<DialogTollChargeBinding>(), TollChargeNavigator {

    private lateinit var mBinding: DialogTollChargeBinding
    private lateinit var mViewModel: TollChargeViewModel
    private lateinit var mDashboardViewModel: TaxiDashboardViewModel
    private var tollCharge = ""

    override fun getLayout() = R.layout.dialog_toll_charge

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.share_dialog)
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as DialogTollChargeBinding
        mViewModel = TollChargeViewModel()
        mDashboardViewModel = ViewModelProviders.of(activity!!).get(TaxiDashboardViewModel::class.java)

        mViewModel.navigator = this
        mBinding.tollmodel = mViewModel
        mBinding.lifecycleOwner = this
        mBinding.edtAmount.addTextChangedListener(EditListener())
        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>

        val strRequestID = if (arguments != null && arguments!!.containsKey("requestID"))
            arguments!!.getString("requestID") else ""

        val locationPoint: ArrayList<LocationPoint> = arrayListOf()

        tv_toll_submit.setOnClickListener {
            if (isValidCharge()) {

                val model = DroppedStatusModel()
                try {
                    mViewModel.showLoading.value = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                for (points in mDashboardViewModel.iteratePointsForApi)
                    locationPoint.add(LocationPoint(points.latitude, points.longitude))

                model.id = strRequestID
                model.status = Constants.RideStatus.DROPPED
                model._method = "PATCH"
                model.toll_price = tollCharge
                model.distance = mDashboardViewModel.distanceMeter.value!!
                model.latitude = mDashboardViewModel.latitude.value!!
                model.longitude = mDashboardViewModel.longitude.value!!
                model.location_points = locationPoint

                println("RRR::" + Gson().toJson(model))
                mViewModel.callUpdateRequestApi(model)
            }
        }

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
//            setPrefix(mBinding.edtAmount, s, "$")
            setPrefix(mBinding.edtAmount, s, "")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    fun setPrefix(editText: PrefixCustomEditText, s: Editable?, strPref: String) {
        println("RRR :: s = [$s], strPref = [$strPref]")
        mViewModel.tollChargeLiveData.value = s.toString()
        tollCharge = s.toString()
        if (s.toString().isNotEmpty()) editText.setPrefix(strPref) else editText.setPrefix("")
    }

    override fun showErrorMessage(error: String) {
        activity!!.runOnUiThread {
            mViewModel.showLoading.value = false
            ViewUtils.showToast(activity!!, error, false)
        }
    }

    override fun isValidCharge() = when {
        tollCharge.isEmpty() -> {
            showErrorMessage(activity!!.getString(R.string.empty_toll_charge))
            false
        }
        tollCharge.toDouble() <= 0 -> {
            showErrorMessage(activity!!.getString(R.string.invalid_toll_charge))
            false
        }
        else -> true
    }
}