package com.gox.partner.views.account_card

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.creditcarddesign.CardEditActivity
import com.cooltechworks.creditcarddesign.CreditCardUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gox.base.base.BaseActivity
import com.gox.base.base.BaseApplication
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySavedCardListBinding
import com.gox.partner.models.AddCardModel
import com.gox.partner.models.CardListModel
import com.gox.partner.models.CardResponseModel
import com.gox.partner.models.ConfigPayment
import com.gox.partner.views.adapters.CardsAdapter
import com.gox.partner.views.adapters.PaymentModeAdapter
import com.gox.partner.views.wallet.WalletFragment
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token

class ActivityCardList : BaseActivity<ActivitySavedCardListBinding>(), CardListNavigator {

    private lateinit var mViewModel: CardListViewModel
    private lateinit var mBinding: ActivitySavedCardListBinding
    private lateinit var context: Context
    private lateinit var cardsAdapter: CardsAdapter

    private var mCardNumber: String? = ""
    private var mCardCVV: String? = ""
    private var mCardExpiryDate: String? = ""
    private var mCardHolderName: String? = ""
    private var selectedCardID: String? = ""
    private var paymentList: List<ConfigPayment>? = null
    private var cardList: MutableList<CardResponseModel>? = null
    private var selectedPosition: Int? = -1
    private var isFromWallet: Boolean = false
    private var paymentModeAdapter: PaymentModeAdapter? = null

    override fun getLayoutId() = R.layout.activity_saved_card_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        context = this
        mBinding = mViewDataBinding as ActivitySavedCardListBinding
        mViewModel = CardListViewModel()
        mViewModel.navigator = this
        mBinding.cardListModel = mViewModel
        mBinding.lifecycleOwner = this
        getApiResponse()
        mBinding.toolbarLayout.tvToolbarTitle.text = resources.getString(R.string.title_payment_type)
        mBinding.toolbarLayout.ivToolbarBack.setOnClickListener {
            finish()
        }
        getIntentValues()
        val payTypes = object : TypeToken<List<ConfigPayment>>() {}.type
        paymentList = Gson().fromJson<List<ConfigPayment>>(BaseApplication.getCustomPreference!!.getString(PreferencesKey.PAYMENT_LIST, ""), payTypes)
        val linearLayoutManager = LinearLayoutManager(this)
        mBinding.rvPaymentModes.layoutManager = linearLayoutManager
        paymentModeAdapter = PaymentModeAdapter(context, paymentList!!, mViewModel, isFromWallet)
        mBinding.rvPaymentModes.adapter = paymentModeAdapter
        mViewModel.loadingProgress = loadingObservable
        mViewModel.getCardList()
    }

    private fun getIntentValues() {
        isFromWallet = if (intent != null && intent.hasExtra("isFromWallet"))
            intent.getBooleanExtra("isFromWallet", false) else false
    }

    private fun getApiResponse() {
        mViewModel.addCardLiveResponse.observe(this, Observer<AddCardModel> { addCardModel ->
            if (addCardModel.getStatusCode().equals("200")) {
                mViewModel.loadingProgress.value = false
                mViewModel.getCardList()
            }
        })

        mViewModel.cardListLiveResponse.observe(this, Observer<CardListModel> { cardListModel ->
            mViewModel.loadingProgress.value = false
            if (mViewModel.cardListLiveResponse != null
                    && mViewModel.cardListLiveResponse.value!!.getResponseData() != null
                    && mViewModel.cardListLiveResponse.value!!.getResponseData()!!.size > 0) {
                mBinding.ivEmptyCard.visibility = View.GONE
                mBinding.rvCards.visibility = View.VISIBLE
                val linearLayoutManager = LinearLayoutManager(this)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                cardList = mViewModel.cardListLiveResponse.value!!.getResponseData()
                if (cardList != null && cardList!!.size > 0) {
                    cardsAdapter = CardsAdapter(context, cardList!!, mViewModel)
                    mBinding.rvCards.adapter = cardsAdapter
                    mBinding.rvCards.layoutManager = linearLayoutManager
                }
            }
        })

        mViewModel.deleteCardLivResponse.observe(this, Observer<AddCardModel> { addCardModel ->
            mViewModel.loadingProgress.value = false
            if (mViewModel.deleteCardLivResponse != null) {
                if (mViewModel.deleteCardLivResponse.value!!.getStatusCode().equals("200")) {
                    cardList?.let { selectedPosition?.let { it1 -> it.removeAt(it1) } }
                    selectedCardID = ""
                    selectedPosition = -1
                    mBinding.ivDelete.visibility = View.GONE
                    mBinding.ivRemove.visibility = View.GONE
                    if (cardList!!.size == 0) {
                        mBinding.rvCards.visibility = View.GONE
                        mBinding.ivEmptyCard.visibility = View.VISIBLE

                    }
                    cardsAdapter.notifyDataSetChanged()
                }
            }
        })
    }

//    override fun addAmount(view: View) {
//        when (view.id) {
//            R.id.bt_fifty -> strAmount = "50"
//
//            R.id.bt_hundred -> strAmount = "100"
//
//            R.id.bt_thousand -> strAmount = "1000"
//        }
//        mViewModel.amount.value = strAmount
//
//    }

    override fun addCard() {
        val intent = Intent(this, CardEditActivity::class.java)
        intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, "Name As per in your card")
        startActivityForResult(intent, Constants.RequestCode.ADD_CARD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            mCardNumber = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER)
            mCardExpiryDate = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY)
            mCardCVV = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV)
            mCardHolderName = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME)

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
                mViewModel.loadingProgress.value = true
                val stripe = Stripe(this, BaseApplication.getCustomPreference!!.getString(PreferencesKey.STRIPE_KEY, ""))
                stripe.createToken(card,
                        object : TokenCallback {
                            override fun onSuccess(token: Token) {
                                WalletFragment.loadingProgress.value = false
                                if (!TextUtils.isEmpty(token.id))
                                    mViewModel.callAddCardApi(token.id)

                            }

                            override fun onError(error: Exception) {
                                mViewModel.loadingProgress.value = false
                            }
                        }
                )
            } else WalletFragment.loadingProgress.value = false
        }
    }

    override fun cardPicked(stripeID: String, cardID: String, position: Int) {
        mBinding.ivDelete.visibility = View.VISIBLE
        mBinding.ivRemove.visibility = View.VISIBLE
        mViewModel.selectedStripeID.value = stripeID
        mViewModel.selectedCardID.value = cardID
        if (selectedPosition != -1) {
            selectedPosition?.let { cardList!!.get(it).isCardSelected = false }
            cardsAdapter.notifyItemChanged(selectedPosition!!)
        }
        this.selectedPosition = position
        selectedPosition?.let { cardList!!.get(it).isCardSelected = true }
        cardsAdapter.notifyItemChanged(selectedPosition!!)

        if (isFromWallet) {
            val intent = Intent()
            intent.putExtra("cardStripeID", mViewModel.selectedStripeID.value)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun removeCard() =
            ViewUtils.showMessageOKCancel(context, resources.getString(R.string.delete_card),
                    DialogInterface.OnClickListener { dialog, which -> mViewModel.callCardDeleteCardAPi() })

    override fun deselectCard() {
        selectedPosition?.let { cardList!!.get(it).isCardSelected = false }
        cardsAdapter.notifyItemChanged(selectedPosition!!)
        mBinding.ivRemove.visibility = View.GONE
        mBinding.ivDelete.visibility = View.GONE
        mViewModel.selectedStripeID.value = ""
    }

    override fun showErrorMsg(error: String) {
        mViewModel.loadingProgress.value = false
        ViewUtils.showToast(context, error, false)
    }
}