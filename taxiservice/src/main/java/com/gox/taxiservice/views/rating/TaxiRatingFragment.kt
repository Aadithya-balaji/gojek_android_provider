package com.gox.taxiservice.views.rating

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gox.base.base.BaseDialogFragment
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.ViewUtils
import com.gox.taxiservice.R
import com.gox.taxiservice.databinding.FragmentRatingBinding
import com.gox.taxiservice.views.invoice.TaxiInvoiceViewModel
import kotlinx.android.synthetic.main.fragment_rating.*

class TaxiRatingFragment(bundle: Bundle) : BaseDialogFragment<FragmentRatingBinding>(), TaxiRatingNavigator {

    private var appCompatActivity: AppCompatActivity? = null
    private var fragmentRatingBinding: FragmentRatingBinding? = null

    private val b = bundle

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appCompatActivity = context as AppCompatActivity
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun showErrorMessage(errorMessage: String) {
        try {
            activity!!.runOnUiThread { ViewUtils.showToast(context!!, errorMessage, false) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getLayout() = R.layout.fragment_rating

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        fragmentRatingBinding = viewDataBinding as FragmentRatingBinding
        val mTaxiInvoiceViewModel = ViewModelProviders.of(activity!!).get(TaxiInvoiceViewModel::class.java)
        val mViewModel = TaxiRatingViewModel()
        fragmentRatingBinding!!.ratingmodel = mViewModel
        mViewModel.navigator = this
        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>

        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        tvUserName.text = b.getString("name")!!
        tvBookingId.text = b.getString("bookingID")!!

        try {
            Glide.with(context!!).applyDefaultRequestOptions(RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .error(R.drawable.ic_user_place_holder))
                    .load(b.getString("profileImg")!!)
                    .into(civProfileImg)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val params: HashMap<String, String> = HashMap()
        params["id"] = b.getString("id")!!
        params["admin_service"] = b.getString("admin_service")!!

        tv_rating_submit.setOnClickListener {
            mViewModel.showLoading.value = true
            params["comment"] = tvComments.text.toString()
            params["rating"] = rbRatingBar.rating.toInt().toString()
            mViewModel.submitRating(params)
        }

        observeLiveData(mViewModel.ratingLiveData) {
            mViewModel.showLoading.value = false
            if (mViewModel.ratingLiveData.value != null)
                if (mViewModel.ratingLiveData.value!!.statusCode.equals("200")) mTaxiInvoiceViewModel.closeActivity()
        }
    }
}