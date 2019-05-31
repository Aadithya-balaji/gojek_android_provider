package com.gox.partner.views.payment

import android.content.Intent
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.cooltechworks.creditcarddesign.CardEditActivity
import com.cooltechworks.creditcarddesign.CreditCardUtils
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.gox.base.base.BaseActivity
import com.gox.partner.R
import com.gox.partner.databinding.ActivityPaymentBinding
import com.gox.partner.views.transaction_status.TransactionStatusActivity
import com.gox.xjek.ui.payment.PaymentNavigator
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class PaymentActivity : BaseActivity<ActivityPaymentBinding>(), PaymentNavigator {

    lateinit var mViewDataBinding: ActivityPaymentBinding
    private var mCardNumber: String? = ""
    private var mCardCVV: String? = ""
    private var mCardExpiryDate: String? = ""
    private var mCardHolderName: String? = ""

    override fun getLayoutId(): Int = R.layout.activity_payment

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityPaymentBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(com.gox.partner.R.string.title_payment)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }
        val paymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)
        paymentViewModel.navigator = this
        mViewDataBinding.paymentViewModel = paymentViewModel
    }

    override fun addWalletAmount() {
        val intent = Intent(this, CardEditActivity::class.java)
        startActivityForResult(intent, 123)

    }

    override fun goToTransactionStatusActivty() {
        launchNewActivity(TransactionStatusActivity::class.java, true)
    }

    override fun addCard() {
        val intent = Intent(this, CardEditActivity::class.java)
        intent.putExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, "Name As per in your card");
        startActivityForResult(intent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCardNumber = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER)
        mCardExpiryDate = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY)
        mCardCVV = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV)
        mCardHolderName = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME)

        // Your processing goes here.
        val temp = mCardExpiryDate!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val month = Integer.parseInt(temp[0])
        val year = Integer.parseInt(temp[1])

        val card = Card(mCardNumber, month, year, mCardCVV)

        card.name = mCardHolderName

        if (card.validateNumber() && card.validateCVC()) {
            //showSnackBar("Card Added Successfully");
            val stripe = Stripe(this, resources.getString(R.string.stripe_key))
            stripe.createToken(
                    card,
                    object : TokenCallback {
                        override fun onSuccess(token: Token) {
                            // Send token to your server
                            Log.e("payment", "-----" + token.id)

                        }

                        override fun onError(error: Exception) {
                            // Show localized error message

                        }
                    }
            )
        }
    }
}