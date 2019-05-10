package com.xjek.foodservice.ui.rating

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.utils.ViewUtils
import com.xjek.foodservice.R
import com.xjek.foodservice.databinding.FoodieFragmentRatingBinding
import com.xjek.foodservice.ui.dashboard.FoodLiveTaskServiceViewModel
import kotlinx.android.synthetic.main.foodie_fragment_rating.*

class FoodieRatingFragment(bundle: Bundle) : BaseDialogFragment<FoodieFragmentRatingBinding>(), FoodieRatingNavigator {

    private var appCompatActivity: AppCompatActivity? = null
    private var fragmentRatingBinding: FoodieFragmentRatingBinding? = null

    val b = bundle

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appCompatActivity = context as AppCompatActivity
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun showErrorMessage(errorMessage: String) {
        try {
            ViewUtils.showToast(context!!, errorMessage, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getLayout(): Int = R.layout.foodie_fragment_rating

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        fragmentRatingBinding = viewDataBinding as FoodieFragmentRatingBinding
        val mTaxiInvoiceViewModel = ViewModelProviders.of(activity!!).get(FoodLiveTaskServiceViewModel::class.java)
        val mViewModel = FoodieRatingViewModel()
        fragmentRatingBinding!!.viewModel = mViewModel
        mViewModel.navigator = this
        tvUserName.text = b.getString("name")!!
        tvBookingId.text = b.getString("bookingID")!!
        try {
            Glide
                    .with(context!!)
                    .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                            .placeholder(R.drawable.foodie_profile_placeholder)
                            .error(R.drawable.foodie_profile_placeholder))
                    .load(b.getString("profileImg")!!)
                    .into(civProfileImg)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        tv_rating_submit.setOnClickListener {
            mTaxiInvoiceViewModel.callFoodieRatingRequest(rbRatingBar.rating.toInt().toString(), tvComments.text.toString())
            dismiss()
        }
    }
}
