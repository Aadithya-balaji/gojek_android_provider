package com.gox.partner.views.wallet

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseFragment
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.PrefixCustomEditText
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.FragmentWalletBinding
import com.gox.partner.models.ConfigPayment
import com.gox.partner.models.ProfileResponse
import com.gox.partner.views.account_card.PaymentTypesActivity

class WalletFragment : BaseFragment<FragmentWalletBinding>(), WalletNavigator {

    private lateinit var mBinding: FragmentWalletBinding
    private lateinit var mViewModel: WalletViewModel

    private var strAmount: String? = null
    private var paymentList: List<ConfigPayment>? = null
    private var currencySymbol:String? = PreferencesHelper.get(PreferencesKey.CURRENCY_SYMBOL, "₹")

    override fun getLayoutId() = R.layout.fragment_wallet

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as FragmentWalletBinding
        mViewModel = WalletViewModel()
        mViewModel.navigator = this
        mBinding.walletmodel = mViewModel
        mBinding.lifecycleOwner = this
        mViewModel.resources = activity!!.resources

        val payTypes = object : TypeToken<List<ConfigPayment>>() {}.type
        paymentList = Gson().fromJson<List<ConfigPayment>>(BaseApplication.getCustomPreference!!.getString(PreferencesKey.PAYMENT_LIST, ""), payTypes)
        mBinding.edtAmount.addTextChangedListener(EditListener())

        observeLiveData(mViewModel.showLoading) {
            loadingObservable.value = it
        }

        getApiResponse()

        mViewModel.getProfile()
    }



    private fun getApiResponse() {
        observeLiveData(mViewModel.walletLiveResponse) {
            mViewModel.showLoading.value = false
            if (mViewModel.walletLiveResponse.value!!.getStatusCode().equals("200")) {
                if (mViewModel.walletLiveResponse.value!!.getResponseData() != null) {
                    val balance = mViewModel.walletLiveResponse.value!!.getResponseData()!!.getWalletBalance()
                    mBinding.tvWalletBal.text = String.format(resources.getString(R.string.wallet_balance), balance, PreferencesHelper.get(PreferencesKey.CURRENCY_SYMBOL, "₹"))
                    mViewModel.walletAmount.value = ""
                }
            }
        }

        mViewModel.mProfileResponse.observe(this, Observer<ProfileResponse> { profileResposne ->
            mViewModel.showLoading.value = false
            if (profileResposne.statusCode == "200") {
                val walletBalance = profileResposne.profileData.wallet_balance
                mBinding.tvWalletBal.text = String.format(resources.getString(R.string.wallet_balance), walletBalance, currencySymbol)
                mBinding.btFifty.text = "${currencySymbol}$AMOUNT_1"
                mBinding.btHundred.text = "${currencySymbol}$AMOUNT_2"
                mBinding.btThousand.text = "${currencySymbol}$AMOUNT_3"
            }
        })
    }

    override fun showErrorMsg(error: String) {
        mViewModel.showLoading.value = false
        ViewUtils.showToast(activity!!, error, false)
    }

    override fun addAmount(view: View) {
        when (view.id) {
            R.id.bt_fifty -> strAmount = AMOUNT_1

            R.id.bt_hundred -> strAmount = AMOUNT_2

            R.id.bt_thousand -> strAmount = AMOUNT_3
        }

        mViewModel.walletAmount.value = strAmount
    }

    override fun validate(): Boolean {
        return when {
            mViewModel.walletAmount.value.isNullOrEmpty() -> {
                ViewUtils.showToast(activity!!, resources.getString(R.string.empty_wallet_amount), false)
                false
            }
            mViewModel.selectedStripeID.value.isNullOrEmpty() -> {
                ViewUtils.showToast(activity!!, resources.getString(R.string.empty_card), false)
                false
            }
            else -> true
        }
    }

    inner class EditListener : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            setPrefix(mBinding.edtAmount, s, "$")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.RequestCode.SELECTED_CARD -> if (resultCode == Activity.RESULT_OK) {
                val stripeID = if (data != null && data.hasExtra("cardStripeID"))
                    data.getStringExtra("cardStripeID") else ""
                mViewModel.selectedStripeID.value = stripeID
                mViewModel.callAddAmtApi()
            }
        }
    }

    fun setPrefix(editText: PrefixCustomEditText, s: Editable?, strPref: String) {
        if (s.toString().isNotEmpty()) editText.setPrefix(strPref)
        else editText.setPrefix("")
    }

    override fun getCard() {
        val intent = Intent(activity!!, PaymentTypesActivity::class.java)
        intent.putExtra("isFromWallet", true)
        startActivityForResult(intent, Constants.RequestCode.SELECTED_CARD)
    }

    companion object {
        private const val AMOUNT_1:String = "50"
        private const val AMOUNT_2:String = "100"
        private const val AMOUNT_3:String = "1000"
    }

}