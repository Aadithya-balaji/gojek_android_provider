package com.xjek.xuberservice.rating

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.data.Constants
import com.xjek.base.utils.ViewUtils
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.DialogXuperRatingBinding
import com.xjek.xuberservice.model.UpdateRequest
import com.xjek.xuberservice.model.XuperRatingModel

class DialogXuperRating : BaseDialogFragment<DialogXuperRatingBinding>(), XuperRatingNavigator {
    private lateinit var dialogXuperRatingBinding: DialogXuperRatingBinding
    private lateinit var xuperRatingViewModel: XuperRatingViewModel
    private var strUpdateRequestModel: String? = null
    private var updateRequesModel: UpdateRequest? = null

    override fun getLayout(): Int {
        return R.layout.dialog_xuper_rating
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleValues()
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogXuperRatingBinding = viewDataBinding as DialogXuperRatingBinding
        xuperRatingViewModel = XuperRatingViewModel()
        dialogXuperRatingBinding.ratingmodel = xuperRatingViewModel
        dialogXuperRatingBinding.setLifecycleOwner(this)
        xuperRatingViewModel.showProgress = loadingObservable as MutableLiveData<Boolean>
        getApiResponse()
    }

    fun getBundleValues() {
        strUpdateRequestModel = if (arguments!!.containsKey("updateRequestModel")) arguments!!.getString("updateRequestModel") else ""
        updateRequesModel = Gson().fromJson(strUpdateRequestModel, UpdateRequest::class.java)
        if (updateRequesModel != null) {
            xuperRatingViewModel.id.value = updateRequesModel!!.responseData!!.id.toString()
            xuperRatingViewModel.rating.value = updateRequesModel!!.responseData!!.user_rated.toString()

        }
    }


    fun getApiResponse() {
        xuperRatingViewModel.ratingLiveData.observe(this, object : Observer<XuperRatingModel> {
            override fun onChanged(t: XuperRatingModel?) {
            }

        })
    }


    override fun sumitRating() {
        val params = HashMap<String, String>()
        params.put(com.xjek.base.data.Constants.Common.ID, xuperRatingViewModel.id.value.toString())
        params.put(com.xjek.base.data.Constants.Common.METHOD, "POST")
        params.put(com.xjek.base.data.Constants.Common.ADMIN_SERVICE_ID, "3")
        params.put(com.xjek.base.data.Constants.XuperProvider.RATING, "5")
        params.put(Constants.XuperProvider.COMMENT,xuperRatingViewModel.comment.value.toString())
        xuperRatingViewModel.callRatingApi(params)
    }

    override fun showErrorMessage(error: String) {
        ViewUtils.showToast(activity!!, error, false)
    }

}
