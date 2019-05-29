package com.xjek.provider.views.wallet

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xjek.base.base.BaseApplication
import com.xjek.base.base.BaseFragment
import com.xjek.base.data.Constants
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.PrefixCustomEditText
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentWalletBinding
import com.xjek.provider.models.CardResponseModel
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.provider.views.account_card.ActivityCardList
import com.xjek.provider.views.adapters.CardsAdapter
import com.xjek.provider.views.adapters.PaymentModeAdapter
import com.xjek.provider.views.manage_payment.ManagePaymentActivity

class WalletFragment : BaseFragment<FragmentWalletBinding>(), WalletNavigator {

    private lateinit var fragmentWalletBinding: FragmentWalletBinding
    private lateinit var walletViewModel: WalletViewModel
    private var strAmount: String? = null
    private var paymentList: List<ConfigResponseModel.ResponseData.AppSetting.Payments>? = null

    companion object {
        var loadingProgress: MutableLiveData<Boolean> = MutableLiveData()
    }


    override fun getLayoutId() = R.layout.fragment_wallet

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        fragmentWalletBinding = mViewDataBinding as FragmentWalletBinding
        walletViewModel = WalletViewModel(resources)
        walletViewModel.navigator = this
        fragmentWalletBinding.walletmodel = walletViewModel
        fragmentWalletBinding.lifecycleOwner = this
        walletViewModel.resources=activity!!.resources
       /* val flexboxLayoutManager = FlexboxLayoutManager(activity)
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW)
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START)*/
        // flexboxLayoutManager.justifyContent = JustifyContent.SPACE_BETWEEN
        val paytypes = object : TypeToken<List<ConfigResponseModel.ResponseData.AppSetting.Payments>>() {}.type
        paymentList = Gson().fromJson<List<ConfigResponseModel.ResponseData.AppSetting.Payments>>(BaseApplication.getCustomPreference!!.getString(PreferencesKey.PAYMENT_LIST, ""), paytypes)
        /*rvPaymentModes.apply {
            layoutManager = flexboxLayoutManager
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.rv_space).toInt()))
            adapter = PaymentModeAdapter(activity!!, paymentTypes, paymentList!!, walletViewModel)
        }*/
        fragmentWalletBinding.edtAmount.addTextChangedListener(EditListener())
        val activity: ManagePaymentActivity = activity as ManagePaymentActivity
        //loadingProgress = activity.loadingObservable as MutableLiveData<Boolean>

        observeLiveData(loadingProgress) {
            loadingObservable.value = it
        }

        observeLiveData(walletViewModel.showLoading) {
            loadingProgress.value = it
        }

        getApiRespoonse()
    }


    fun getApiRespoonse() {

        //Add Amount
        observeLiveData(walletViewModel.walletLiveResponse) {
            loadingProgress?.let { it.value = false }
            if (walletViewModel.walletLiveResponse.value!!.getStatusCode().equals("200")) {
                if (walletViewModel.walletLiveResponse.value!!.getResponseData() != null) {
                    var balance = walletViewModel.walletLiveResponse.value!!.getResponseData()!!.getWalletBalance()
                    fragmentWalletBinding.tvWalletBal.setText(String.format(resources.getString(R.string.wallet_balance), balance))
                }
            }
        }


    }

    class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            with(outRect) {
                bottom = spaceHeight
            }
        }
    }

    override fun showErrorMsg(error: String) {
        loadingProgress!!.value = false
        ViewUtils.showToast(activity!!, error, false)
    }


    override fun addAmount(view: View) {
        when (view.id) {
            R.id.bt_fifty -> {
                strAmount = "50"
            }

            R.id.bt_hundred -> {
                strAmount = "100"
            }

            R.id.bt_thousand -> {
                strAmount = "1000"
            }
        }

        walletViewModel.walletAmount.value = strAmount
    }


    override fun validate(): Boolean {
        if (walletViewModel.walletAmount.value.isNullOrEmpty()) {
            ViewUtils.showToast(activity!!, resources.getString(R.string.empty_wallet_amount), false)
            return false
        } else if (walletViewModel.selectedStripeID.value.isNullOrEmpty()) {
            ViewUtils.showToast(activity!!, resources.getString(R.string.empty_card), false)
            return false
        } else {
            return true
        }
    }

    inner class EditListener : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setPrefix(fragmentWalletBinding.edtAmount, s, "$")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode)
        {
            Constants.RequestCode.SELECTED_CARD -> {

                if(resultCode==Activity.RESULT_OK) {
                    val stripeID = if (data != null && data.hasExtra("cardStripeID")) data.getStringExtra("cardStripeID") else ""
                    walletViewModel.selectedStripeID.value = stripeID
                    walletViewModel.callAddAmtApi()
                }
            }

        }
    }
    fun setPrefix(editText: PrefixCustomEditText, s: Editable?, strPref: String) {
        if (s.toString().length > 0) {
            editText.setPrefix(strPref)
        } else {
            editText.setPrefix("")
        }
    }

    override fun getCard() {
        val intent =Intent(activity!!,ActivityCardList::class.java)
        intent.putExtra("isFromWallet",true)
        startActivityForResult(intent,Constants.RequestCode.SELECTED_CARD)
    }


}
