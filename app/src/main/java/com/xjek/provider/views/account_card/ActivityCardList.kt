package com.xjek.provider.views.account_card

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cooltechworks.creditcarddesign.CardEditActivity
import com.cooltechworks.creditcarddesign.CreditCardUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.xjek.base.base.BaseActivity
import com.xjek.base.base.BaseApplication
import com.xjek.base.data.Constants
import com.xjek.base.data.PreferencesKey
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySavedCardListBinding
import com.xjek.provider.models.AddCardModel
import com.xjek.provider.models.CardListModel
import com.xjek.provider.models.CardResponseModel
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.provider.views.adapters.CardsAdapter
import com.xjek.provider.views.adapters.PaymentModeAdapter
import com.xjek.provider.views.wallet.WalletFragment

class ActivityCardList : BaseActivity<ActivitySavedCardListBinding>(), CardListNavigator {

    private lateinit var cardListViewModel: CardListViewModel
    private lateinit var activitySavedCardListBinding: ActivitySavedCardListBinding
    private lateinit var context: Context
    private var strAmount: String? = null
    private var mCardNumber: String? = ""
    private var mCardCVV: String? = ""
    private var mCardExpiryDate: String? = ""
    private var mCardHolderName: String? = ""
    private var selectedCardID: String? = ""
    private lateinit var cardsAdapter: CardsAdapter
    private var paymentList: List<ConfigResponseModel.ResponseData.AppSetting.Payments>? = null
    private lateinit var rvPaymentModes: RecyclerView
    private var cardList: MutableList<CardResponseModel>? = null
    private var selectedPosition: Int? = -1
    private var isFromWallet: Boolean = false
    private  var  paymentModeAdapter:PaymentModeAdapter?=null


    override fun getLayoutId(): Int {
        return R.layout.activity_saved_card_list
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        context = this
        activitySavedCardListBinding = mViewDataBinding as ActivitySavedCardListBinding
        cardListViewModel = CardListViewModel()
        cardListViewModel.navigator = this
        activitySavedCardListBinding.cardListModel = cardListViewModel
        activitySavedCardListBinding.setLifecycleOwner(this)
        getApiResponse()
        activitySavedCardListBinding.toolbarLayout.tvToolbarTitle.setText(resources.getString(R.string.title_payment_type))
        activitySavedCardListBinding.toolbarLayout.ivToolbarBack.setOnClickListener {
            finish()
        }
        getIntentValues()
        val paytypes = object : TypeToken<List<ConfigResponseModel.ResponseData.AppSetting.Payments>>() {}.type
        paymentList = Gson().fromJson<List<ConfigResponseModel.ResponseData.AppSetting.Payments>>(BaseApplication.getCustomPreference!!.getString(PreferencesKey.PAYMENT_LIST,""), paytypes)
        val linearLayoutManager = LinearLayoutManager(this)
        activitySavedCardListBinding.rvPaymentModes.layoutManager=linearLayoutManager
        paymentModeAdapter = PaymentModeAdapter(context, paymentList!!, cardListViewModel)
        activitySavedCardListBinding.rvPaymentModes.adapter=paymentModeAdapter
        cardListViewModel.loadingProgress = loadingObservable as MutableLiveData<Boolean>
        cardListViewModel.getCardList()
    }

    fun getIntentValues() {
        isFromWallet = if (intent != null && intent.hasExtra("isFromWallet")) intent.getBooleanExtra("isFromWallet", false) else false
    }

    fun getApiResponse() {
        cardListViewModel.addCardLiveResposne.observe(this, Observer<AddCardModel> { addCardModel ->
            if (addCardModel.getStatusCode().equals("200")) {
                cardListViewModel.loadingProgress.value=false
                cardListViewModel.getCardList()
            }
        })

        cardListViewModel.cardListLiveResponse.observe(this, Observer<CardListModel> { cardListModel ->
            cardListViewModel.loadingProgress.value=false
            if (cardListViewModel.cardListLiveResponse != null && cardListViewModel.cardListLiveResponse.value!!.getResponseData() != null && cardListViewModel!!.cardListLiveResponse.value!!.getResponseData()!!.size > 0) {
                activitySavedCardListBinding.ivEmptyCard.visibility = View.GONE
                activitySavedCardListBinding.rvCards.visibility = View.VISIBLE
                val linearLayoutManager = LinearLayoutManager(this)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                cardList = cardListViewModel.cardListLiveResponse.value!!.getResponseData()
                if (cardList != null && cardList!!.size > 0) {
                    cardsAdapter = CardsAdapter(context!!, cardList!!, cardListViewModel)
                    activitySavedCardListBinding.rvCards.adapter = cardsAdapter
                    activitySavedCardListBinding.rvCards.layoutManager = linearLayoutManager
                }

            }

        })

        cardListViewModel.deleCardLivResponse.observe(this, Observer<AddCardModel> { addCardModel ->
            cardListViewModel.loadingProgress.value=false
            if (cardListViewModel.deleCardLivResponse != null) {
                if (cardListViewModel.deleCardLivResponse.value!!.getStatusCode().equals("200")) {
                    cardList?.let { selectedPosition?.let { it1 -> it.removeAt(it1) } }
                    selectedCardID = ""
                    selectedPosition = -1
                    activitySavedCardListBinding.ivDelete.visibility = View.GONE
                    activitySavedCardListBinding.ivRemove.visibility = View.GONE
                    if (cardList!!.size == 0) {
                        activitySavedCardListBinding.rvCards.visibility = View.GONE
                        activitySavedCardListBinding.ivEmptyCard.visibility = View.VISIBLE

                    }
                    cardsAdapter.notifyDataSetChanged()
                }
            }

        })

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
        cardListViewModel.amount.value = strAmount

    }

    override fun addCard() {
        val intent = Intent(this, CardEditActivity::class.java)
        intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, "Name As per in your card");
        startActivityForResult(intent, Constants.RequestCode.ADDCARD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
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
                cardListViewModel.loadingProgress.value = true
                val stripe = Stripe(this, BaseApplication.getCustomPreference!!.getString(PreferencesKey.STRIPE_KEY, ""))
                stripe.createToken(
                        card,
                        object : TokenCallback {
                            override fun onSuccess(token: Token) {
                                Log.e("card", "-----" + token.id)
                                WalletFragment.loadingProgress!!.value = false
                                // Send token to your server
                                if (!TextUtils.isEmpty(token.id))
                                    cardListViewModel.callAddCardApi(token.id)

                            }

                            override fun onError(error: Exception) {
                                // Show localized error message
                                cardListViewModel.loadingProgress?.value = false
                                Log.e("card", "-----" + error.message.toString())


                            }
                        }
                )
            } else {
                WalletFragment.loadingProgress!!.value = false
            }
        }
    }

    override fun cardPicked(stripeID: String, cardID: String, position: Int) {
        activitySavedCardListBinding.ivDelete.visibility = View.VISIBLE
        activitySavedCardListBinding.ivRemove.visibility = View.VISIBLE
        cardListViewModel.selectedStripeID.value = stripeID
        cardListViewModel.selectedCardID.value = cardID
        if (selectedPosition != -1) {
            selectedPosition?.let { cardList!!.get(it).isCardSelected = false }
            cardsAdapter.notifyItemChanged(selectedPosition!!)
        }
        this.selectedPosition = position
        selectedPosition?.let { cardList!!.get(it).isCardSelected = true }
        cardsAdapter.notifyItemChanged(selectedPosition!!)

        if (isFromWallet) {
            val intent = Intent()
            intent.putExtra("cardStripeID", cardListViewModel.selectedStripeID.value)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }


    override fun removeCard() {
        cardListViewModel.callCardDeleteCardAPi()
    }

    override fun deselectCard() {
        selectedPosition?.let { cardList!!.get(it).isCardSelected = false }
        cardsAdapter.notifyItemChanged(selectedPosition!!)
        activitySavedCardListBinding.ivRemove.visibility = View.GONE
        activitySavedCardListBinding.ivDelete.visibility = View.GONE
        cardListViewModel.selectedStripeID.value = ""
    }

    override fun paymentType(type: Int) {
    }

    override fun showErrorMsg(error: String) {
        ViewUtils.showToast(context, error, false)
    }

    override fun changePaymentMode(paymentId: Int) {

    }
}