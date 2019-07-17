package com.gox.foodservice.ui.rating

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gox.base.base.BaseDialogFragment
import com.gox.base.utils.ViewUtils
import com.gox.foodservice.R
import com.gox.foodservice.databinding.FoodieFragmentRatingBinding
import com.gox.foodservice.ui.dashboard.FoodieDashboardViewModel
import kotlinx.android.synthetic.main.foodie_fragment_rating.*

class FoodieRatingFragment(bundle: Bundle) : BaseDialogFragment<FoodieFragmentRatingBinding>(),
        FoodieRatingNavigator {

    private var appCompatActivity: AppCompatActivity? = null
    private var mBinding: FoodieFragmentRatingBinding? = null

    val b = bundle

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
            ViewUtils.showToast(context!!, errorMessage, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getLayout() = R.layout.foodie_fragment_rating

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as FoodieFragmentRatingBinding
        val mFoodieViewModel = ViewModelProviders.of(activity!!).get(FoodieDashboardViewModel::class.java)
        val mViewModel = FoodieRatingViewModel()
        mBinding!!.viewModel = mViewModel
        mViewModel.navigator = this
        tvUserName.text = b.getString("name")!!
        tvBookingId.text = b.getString("bookingID")!!
        try {
            Glide
                    .with(context!!)
                    .applyDefaultRequestOptions(RequestOptions()
                            .circleCrop()
                            .placeholder(R.drawable.ic_user_place_holder)
                            .error(R.drawable.ic_user_place_holder))
                    .load(b.getString("profileImg")!!)
                    .into(civProfileImg)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        tv_rating_submit.setOnClickListener {
            mFoodieViewModel.callFoodieRatingRequest(rbRatingBar.rating.toInt().toString(), tvComments.text.toString())
            dismiss()
        }
    }
}
