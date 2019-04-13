package com.xjek.provider.views.wallet

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.creditcarddesign.CardEditActivity
import com.cooltechworks.creditcarddesign.CreditCardUtils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.xjek.base.base.BaseFragment
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentWalletBinding
import com.xjek.provider.models.CardResponseModel
import com.xjek.provider.views.adapters.CardsAdapter
import com.xjek.provider.views.adapters.PaymentModeAdapter
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.manage_payment.ManagePaymentActivity

class WalletFragment : BaseFragment<FragmentWalletBinding>(), WalletNavigator {
    private lateinit var fragmentWalletBinding: FragmentWalletBinding
    private lateinit var walletViewModel: WalletViewModel
    private lateinit var rvPaymentModes: RecyclerView
    private lateinit var cardsAdapter: CardsAdapter
    private var strAmount: String? = null
    private var mCardNumber: String? = ""
    private var mCardCVV: String? = ""
    private var mCardExpiryDate: String? = ""
    private var mCardHolderName: String? = ""
    private var selectedCardID: String? = ""
    private var selectedPosition: Int? = -1
    private var cardList: MutableList<CardResponseModel>? = null
    companion object {
        var loadingProgress: MutableLiveData<Boolean>?=null

    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_wallet
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        fragmentWalletBinding = mViewDataBinding as FragmentWalletBinding
        walletViewModel = WalletViewModel(resources)
        walletViewModel.navigator = this
        fragmentWalletBinding.walletmodel = walletViewModel
        fragmentWalletBinding.lifecycleOwner = this
        var paymentTypes = resources.getStringArray(R.array.payment_mode).toMutableList()
        val flexboxLayoutManager = FlexboxLayoutManager(activity)
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW)
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START)
        flexboxLayoutManager.justifyContent = JustifyContent.SPACE_BETWEEN
        rvPaymentModes = activity!!.findViewById(R.id.rv_payment_modes)
        rvPaymentModes.apply {
            layoutManager = flexboxLayoutManager
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.rv_space).toInt()))
            adapter = PaymentModeAdapter(activity!!, paymentTypes)
        }

        val  activity: ManagePaymentActivity = activity as ManagePaymentActivity
        loadingProgress=activity.loadingObservable as MutableLiveData<Boolean>
        walletViewModel.showLoading= loadingProgress as MutableLiveData<Boolean>

        walletViewModel.getCardList()
        getApiRespoonse()
    }


    fun getApiRespoonse() {

        //getCardList
        observeLiveData(walletViewModel.cardResponseData) {
            loadingProgress?.value =false
            if (walletViewModel.cardResponseData != null && walletViewModel.cardResponseData.value!!.getResponseData() != null && walletViewModel!!.cardResponseData.value!!.getResponseData()!!.size > 0) {
                fragmentWalletBinding.ivEmptyCard.visibility = View.GONE
                fragmentWalletBinding.rvCards.visibility = View.VISIBLE
                val linearLayoutManager = LinearLayoutManager(activity!!)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                cardList = walletViewModel.cardResponseData.value!!.getResponseData()
                if (cardList != null && cardList!!.size > 0) {
                    cardsAdapter = CardsAdapter(context!!, cardList!!, walletViewModel)
                    fragmentWalletBinding.rvCards.adapter = cardsAdapter
                    fragmentWalletBinding.rvCards.layoutManager = linearLayoutManager
                }

            }
        }


        //Addard
        observeLiveData(walletViewModel.addCardLiveResposne) {
            loadingProgress?.let { it.value=false }
            if (walletViewModel.addCardLiveResposne.value?.getStatusCode().equals("200")) {
                walletViewModel.getCardList()
            }
        }

        //Add Amount
        observeLiveData(walletViewModel.walletLiveResponse) {
            loadingProgress?.let { it.value=false }
            if (walletViewModel.walletLiveResponse.value!!.getStatusCode().equals("200")) {
                if (walletViewModel.walletLiveResponse.value!!.getResponseData() != null) {
                    var balance = walletViewModel.walletLiveResponse.value!!.getResponseData()!!.getWalletBalance()
                    fragmentWalletBinding.tvWalletBal.setText(String.format(resources.getString(R.string.wallet_balance), balance))
                }
            }
        }


        //DeleteCard
        observeLiveData(walletViewModel.deleCardLivResponse) {
            loadingProgress?.let { it.value=false }
            if (walletViewModel.deleCardLivResponse != null) {
                if (walletViewModel.deleCardLivResponse.value!!.getStatusCode().equals("200")) {
                    cardList?.let { selectedPosition?.let { it1 -> it.removeAt(it1) } }
                    cardsAdapter.notifyDataSetChanged()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK) {
            mCardNumber = data?.let { it.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER) }
            mCardExpiryDate = data?.let { it.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY) }
            mCardCVV = data?.let { it.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV) }
            mCardHolderName = data?.let { it.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME) }

            // Your processing goes here.
            val temp = mCardExpiryDate!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val month = Integer.parseInt(temp[0])
            val year = Integer.parseInt(temp[1])

            val card = Card(
                    mCardNumber,
                    month,
                    year,
                    mCardCVV
            )

            card.name = mCardHolderName
            if (card.validateNumber() && card.validateCVC()) {
                //showSnackBar("Card Added Successfully");
                loadingProgress?.value =true
                val stripe = Stripe(activity!!, resources.getString(R.string.stripe_key))
                stripe.createToken(
                        card,
                        object : TokenCallback {
                            override fun onSuccess(token: Token) {
                                // Send token to your server
                                if (!TextUtils.isEmpty(token.id))
                                    Log.e("card", "-----" + token.id)

                                walletViewModel.callAddCardApi(token.id)

                            }

                            override fun onError(error: Exception) {
                                // Show localized error message
                                loadingProgress?.value =false
                                Log.e("card", "-----" + error.message.toString())


                            }
                        }
                )
            }
        }
    }

    override fun validate(): Boolean {
        if (walletViewModel.walletAmount.value.isNullOrEmpty()) {
            ViewUtils.showToast(activity!!, resources.getString(R.string.empty_wallet_amount), false)
            return false
        } else if(walletViewModel.selectedStripeID.value.isNullOrEmpty()) {
            ViewUtils.showToast(activity!!, resources.getString(R.string.empty_card), false)
            return false
        }else{
            return true
        }
    }

    override fun addCard() {
        val intent = Intent(activity, CardEditActivity::class.java)
        intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, "Name As per in your card");
        startActivityForResult(intent, 125)
    }

    override fun cardPicked(stripeID: String,cardID:String ,position: Int) {
        fragmentWalletBinding.ivDelete.visibility = View.VISIBLE
        fragmentWalletBinding.ivRemove.visibility = View.VISIBLE
        walletViewModel.selectedStripeID.value = stripeID
        walletViewModel.selectedCardID.value=cardID
        if (selectedPosition != -1) {
            selectedPosition?.let { cardList!!.get(it).isCardSelected = false }
            cardsAdapter.notifyItemChanged(selectedPosition!!)
        }
        this.selectedPosition = position
        selectedPosition?.let { cardList!!.get(it).isCardSelected = true }
        cardsAdapter.notifyItemChanged(selectedPosition!!)
    }

    override fun removeCard() {
        walletViewModel.callCardDeleteCardAPi()
    }

    override fun deselectCard() {
        selectedPosition?.let { cardList!!.get(it).isCardSelected = false }
        cardsAdapter.notifyItemChanged(selectedPosition!!)
        fragmentWalletBinding.ivRemove.visibility = View.GONE
        fragmentWalletBinding.ivDelete.visibility = View.GONE
        walletViewModel.selectedStripeID.value = ""
    }
}
