package com.bee.courierservice.rating

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.bee.courierservice.R
import com.bee.courierservice.databinding.DialogCourierRatingBinding
import com.bee.courierservice.model.UpdateRequest
import com.bee.courierservice.model.CourierCheckRequest
import com.bee.courierservice.model.CourierRatingModel
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.Constants
import com.gox.base.utils.ViewUtils
import kotlinx.android.synthetic.main.dialog_courier_rating.*

class DialogCourierRating : BaseDialogFragment<DialogCourierRatingBinding>(),
        CourierRatingNavigator, RatingBar.OnRatingBarChangeListener {

    private lateinit var mBinding: DialogCourierRatingBinding
    private lateinit var mViewModel: CourierRatingViewModel
    private var strUpdateRequestModel: String? = null
    private var updateRequestModel: UpdateRequest? = null
    private var strCheckRequestModel: String? = null
    private var xuberCheckRequest: CourierCheckRequest? = null
    private var isFromCheckRequest: Boolean? = false
    private var shown: Boolean? = false

    override fun getLayout() = R.layout.dialog_courier_rating

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as DialogCourierRatingBinding
        mViewModel = CourierRatingViewModel()
        mViewModel.navigator = this
        mBinding.ratingmodel = mViewModel
        mBinding.lifecycleOwner = this
        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>
        getBundleValues()
        mBinding.tvRateWithUser.text = String.format(resources.getString(R.string.xuper_rate),
                mViewModel.firstName.value.toString() + mViewModel.lastName.value.toString())
        mBinding.rbUser.onRatingBarChangeListener = this
        getApiResponse()
    }

    private fun getBundleValues() {
        isFromCheckRequest = if (arguments!!.containsKey("isFromCheckRequest"))
            arguments!!.getBoolean("isFromCheckRequest") else false
        if (isFromCheckRequest == false) {
            strUpdateRequestModel = if (arguments!!.containsKey("updateRequestModel"))
                arguments!!.getString("updateRequestModel") else ""
            updateRequestModel = Gson().fromJson(strUpdateRequestModel, UpdateRequest::class.java)
            if (updateRequestModel != null) {
                mViewModel.id.value = updateRequestModel!!.responseData!!.id.toString()
                mViewModel.rating.value = updateRequestModel!!.responseData!!.user_rated.toString()
                mViewModel.firstName.value = updateRequestModel!!.responseData!!.user!!.first_name
                mViewModel.lastName.value = updateRequestModel!!.responseData!!.user!!.last_name
            }
        } else {
            strCheckRequestModel = if (arguments!!.containsKey("strCheckReq"))
                arguments!!.getString("strCheckReq") else ""
            xuberCheckRequest = Gson().fromJson(strCheckRequestModel, CourierCheckRequest::class.java)
            if (xuberCheckRequest != null) {
                mViewModel.id.value = xuberCheckRequest!!.responseData!!.request!!.id.toString()
                mViewModel.rating.value = xuberCheckRequest!!.responseData!!.request!!.user!!.rating.toString()
                mViewModel.firstName.value = xuberCheckRequest!!.responseData!!.request!!.user!!.first_name.toString()
                mViewModel.lastName.value = xuberCheckRequest!!.responseData!!.request!!.user!!.last_name.toString()
            } else Log.e("Request", "----$xuberCheckRequest")
        }

        loadProfile()
    }

    private fun loadProfile() {
        try {
            Glide.with(context!!).applyDefaultRequestOptions(RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .error(R.drawable.ic_user_place_holder))
                    .load(updateRequestModel!!.responseData!!.user!!.picture)
                    .into(ivXuperRatingUser)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getApiResponse() {
        mViewModel.ratingLiveData.observe(this, Observer<CourierRatingModel> {
            loadingObservable.value = false
            ViewUtils.showToast(activity!!, getString(R.string.rated_success), true)
            activity!!.finish()
        })
    }

    override fun submitRating() {
        val params = HashMap<String, String>()
        params[Constants.Common.ID] = mViewModel.id.value.toString()
        params[Constants.Common.METHOD] = "POST"
        params[Constants.Common.ADMIN_SERVICE] = "SERVICE"
        params[Constants.XUberProvider.RATING] = mViewModel.userRating.value.toString()
        params[Constants.XUberProvider.COMMENT] = mViewModel.comment.value.toString()
        mViewModel.callRatingApi(params)
    }

    override fun showErrorMessage(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(activity!!, error, false)
    }

    fun isShown() = shown!!

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (shown == false) {
                this.shown = true
                super.show(manager, tag)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (shown == false) {
            this.shown = true
            return super.show(transaction, tag)
        }
        return -1
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        this.shown = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        this.shown = false
    }

    override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
        mViewModel.userRating.value = rating.toInt().toString()
    }
}