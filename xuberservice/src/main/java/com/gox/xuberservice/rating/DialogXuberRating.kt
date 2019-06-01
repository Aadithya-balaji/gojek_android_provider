package com.gox.xuberservice.rating

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.Constants
import com.gox.base.utils.ViewUtils
import com.gox.xuberservice.R
import com.gox.xuberservice.databinding.DialogXuperRatingBinding
import com.gox.xuberservice.model.UpdateRequest
import com.gox.xuberservice.model.XuperCheckRequest
import com.gox.xuberservice.model.XuperRatingModel

class DialogXuberRating : BaseDialogFragment<DialogXuperRatingBinding>(), XuperRatingNavigator, RatingBar.OnRatingBarChangeListener {

    private lateinit var dialogXuberRatingBinding: DialogXuperRatingBinding
    private lateinit var xuperRatingViewModel: XuperRatingViewModel
    private var strUpdateRequestModel: String? = null
    private var updateRequestModel: UpdateRequest? = null
    private var strCheckRequestModel: String? = null
    private var xuberCheckRequest: XuperCheckRequest? = null
    private var isFromCheckRequest: Boolean? = false
    private var shown: Boolean? = false

    override fun getLayout() = R.layout.dialog_xuper_rating

    override fun onStart() {
        super.onStart()
        dialog!!.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogXuberRatingBinding = viewDataBinding as DialogXuperRatingBinding
        xuperRatingViewModel = XuperRatingViewModel()
        xuperRatingViewModel.navigator = this
        dialogXuberRatingBinding.ratingmodel = xuperRatingViewModel
        dialogXuberRatingBinding.lifecycleOwner = this
        xuperRatingViewModel.showProgress = loadingObservable as MutableLiveData<Boolean>
        getBundleValues()
        dialogXuberRatingBinding.tvRateWithUser.text = String.format(resources.getString(R.string.xuper_rate),
                xuperRatingViewModel.firstName.value.toString() + xuperRatingViewModel.lastName.value.toString())
        dialogXuberRatingBinding.rbUser.onRatingBarChangeListener = this
        getApiResponse()
    }

    private fun getBundleValues() {
        isFromCheckRequest = if (arguments!!.containsKey("isFromCheckRequest")) arguments!!.getBoolean("isFromCheckRequest") else false
        if (isFromCheckRequest == false) {
            strUpdateRequestModel = if (arguments!!.containsKey("updateRequestModel")) arguments!!.getString("updateRequestModel") else ""
            updateRequestModel = Gson().fromJson(strUpdateRequestModel, UpdateRequest::class.java)
            if (updateRequestModel != null) {
                xuperRatingViewModel.id.value = updateRequestModel!!.responseData!!.id.toString()
                xuperRatingViewModel.rating.value = updateRequestModel!!.responseData!!.user_rated.toString()
                xuperRatingViewModel.firstName.value = updateRequestModel!!.responseData!!.user!!.first_name
                xuperRatingViewModel.lastName.value = updateRequestModel!!.responseData!!.user!!.last_name
            }
        } else {
            strCheckRequestModel = if (arguments!!.containsKey("strCheckReq")) arguments!!.getString("strCheckReq") else ""
            xuberCheckRequest = Gson().fromJson(strCheckRequestModel, XuperCheckRequest::class.java)
            if (xuberCheckRequest != null) {
                xuperRatingViewModel.id.value = xuberCheckRequest!!.responseData!!.requests!!.id.toString()
                xuperRatingViewModel.rating.value = xuberCheckRequest!!.responseData!!.requests!!.user!!.rating.toString()
                xuperRatingViewModel.firstName.value = xuberCheckRequest!!.responseData!!.requests!!.user!!.first_name.toString()
                xuperRatingViewModel.lastName.value = xuberCheckRequest!!.responseData!!.requests!!.user!!.last_name.toString()
            } else Log.e("Request", "----$xuberCheckRequest")
        }
    }

    fun getApiResponse() {
        xuperRatingViewModel.ratingLiveData.observe(this, object : Observer<XuperRatingModel> {
            override fun onChanged(t: XuperRatingModel?) {
                loadingObservable.value = false
                activity!!.finish()
            }
        })
    }

    override fun sumitRating() {
        val params = HashMap<String, String>()
        params[Constants.Common.ID] = xuperRatingViewModel.id.value.toString()
        params[Constants.Common.METHOD] = "POST"
        params[Constants.Common.ADMIN_SERVICE_ID] = "3"
        params[Constants.XuperProvider.RATING] = xuperRatingViewModel.userRating.value.toString()
        params[Constants.XuperProvider.COMMENT] = xuperRatingViewModel.comment.value.toString()
        xuperRatingViewModel.callRatingApi(params)
    }

    override fun showErrorMessage(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(activity!!, error, false)
    }

    fun isShown(): Boolean {
        return shown!!
    }

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
        xuperRatingViewModel.userRating.value = rating.toInt().toString()
    }
}