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

class DialogXuperRating : BaseDialogFragment<DialogXuperRatingBinding>(), XuperRatingNavigator, RatingBar.OnRatingBarChangeListener {
    private lateinit var dialogXuperRatingBinding: DialogXuperRatingBinding
    private lateinit var xuperRatingViewModel: XuperRatingViewModel
    private var strUpdateRequestModel: String? = null
    private var updateRequesModel: UpdateRequest? = null
    private var strCheckRequestModel: String? = null
    private var xuperCheckRequest: XuperCheckRequest? = null
    private var isFromCheckRequest: Boolean? = false
    private var shown: Boolean? = false


    override fun getLayout(): Int {
        return R.layout.dialog_xuper_rating
    }

    override fun onStart() {
        super.onStart()
        getDialog()!!.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog)
    }


    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogXuperRatingBinding = viewDataBinding as DialogXuperRatingBinding
        xuperRatingViewModel = XuperRatingViewModel()
        xuperRatingViewModel.navigator = this
        dialogXuperRatingBinding.ratingmodel = xuperRatingViewModel
        dialogXuperRatingBinding.setLifecycleOwner(this)
        xuperRatingViewModel.showProgress = loadingObservable as MutableLiveData<Boolean>
        getBundleValues()
        dialogXuperRatingBinding.tvRateWithUser.setText(String.format(resources.getString(R.string.xuper_rate), xuperRatingViewModel.firstName.value.toString() + xuperRatingViewModel.lastName.value.toString()))
        dialogXuperRatingBinding.rbUser.onRatingBarChangeListener = this
        getApiResponse()
    }

    fun getBundleValues() {
        isFromCheckRequest = if (arguments!!.containsKey("isFromCheckRequest")) arguments!!.getBoolean("isFromCheckRequest") else false
        if (isFromCheckRequest == false) {
            strUpdateRequestModel = if (arguments!!.containsKey("updateRequestModel")) arguments!!.getString("updateRequestModel") else ""
            updateRequesModel = Gson().fromJson(strUpdateRequestModel, UpdateRequest::class.java)
            if (updateRequesModel != null) {
                xuperRatingViewModel.id.value = updateRequesModel!!.responseData!!.id.toString()
                xuperRatingViewModel.rating.value = updateRequesModel!!.responseData!!.user_rated.toString()
                xuperRatingViewModel.firstName.value = updateRequesModel!!.responseData!!.user!!.first_name
                xuperRatingViewModel.lastName.value = updateRequesModel!!.responseData!!.user!!.last_name
            }
        } else {
            strCheckRequestModel = if (arguments!!.containsKey("strCheckReq")) arguments!!.getString("strCheckReq") else ""
            xuperCheckRequest = Gson().fromJson(strCheckRequestModel, XuperCheckRequest::class.java)
            if (xuperCheckRequest != null) {
                xuperRatingViewModel.id.value = xuperCheckRequest!!.responseData!!.requests!!.id.toString()
                xuperRatingViewModel.rating.value = xuperCheckRequest!!.responseData!!.requests!!.user!!.rating.toString()
                xuperRatingViewModel.firstName.value = xuperCheckRequest!!.responseData!!.requests!!.user!!.first_name.toString()
                xuperRatingViewModel.lastName.value = xuperCheckRequest!!.responseData!!.requests!!.user!!.last_name.toString()
            } else {
                var request = xuperCheckRequest
                Log.e("Request", "----" + null)
            }
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
        params.put(com.gox.base.data.Constants.Common.ID, xuperRatingViewModel.id.value.toString())
        params.put(com.gox.base.data.Constants.Common.METHOD, "POST")
        params.put(com.gox.base.data.Constants.Common.ADMIN_SERVICE_ID, "3")
        params.put(com.gox.base.data.Constants.XuperProvider.RATING, xuperRatingViewModel.userRating.value.toString())
        params.put(Constants.XuperProvider.COMMENT, xuperRatingViewModel.comment.value.toString())
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
        val rating = rating
    }

}
