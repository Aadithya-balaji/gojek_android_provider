package com.xjek.provider.views.wallet

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.xjek.base.base.BaseActivity
import com.xjek.base.base.BaseFragment
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentWalletBinding
import com.xjek.provider.views.adapters.CardsAdapter
import com.xjek.provider.views.adapters.PaymentModeAdapter

class  WalletFragment:BaseFragment<FragmentWalletBinding>(),WalletNavigator{

    private  lateinit var  fragmentWalletBinding: FragmentWalletBinding
    private  lateinit var  walletViewModel:WalletViewModel
    private  lateinit var  rvPaymentModes:RecyclerView
    private  lateinit var  cardsAdapter:CardsAdapter
    private  var strAmount:String?=null

    override fun getLayoutId(): Int {
        return R.layout.fragment_wallet
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        fragmentWalletBinding=mViewDataBinding as FragmentWalletBinding
        walletViewModel= WalletViewModel()
        walletViewModel.navigator=this
        fragmentWalletBinding.walletmodel=walletViewModel
        fragmentWalletBinding.lifecycleOwner=this
        var paymentTypes=resources.getStringArray(R.array.payment_mode).toMutableList()
        val flexboxLayoutManager = FlexboxLayoutManager(activity)
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW)
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START)
        flexboxLayoutManager.justifyContent=JustifyContent.SPACE_BETWEEN
       // flexboxLayoutManager.setMeasuredDimension(150,75)
       // flexboxLayoutManager.layoutDecoratedWithMargins(,10,10,10)
        rvPaymentModes=activity!!.findViewById(R.id.rv_payment_modes)
        rvPaymentModes.apply {
            layoutManager = flexboxLayoutManager
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.rv_space).toInt()))
            adapter = PaymentModeAdapter(activity!!,paymentTypes)
        }
        walletViewModel.getCardList()
        getApiRespoonse()
    }


    fun getApiRespoonse(){
        observeLiveData(walletViewModel.cardResponseData){
            if(walletViewModel.cardResponseData!=null && walletViewModel.cardResponseData.value!!.getResponseData()!=null&&walletViewModel!!.cardResponseData.value!!.getResponseData()!!.size>0){
                   fragmentWalletBinding.ivEmptyCard.visibility=View.GONE
                    fragmentWalletBinding.rvCards.visibility=View.VISIBLE
                   val linearLayoutManager=LinearLayoutManager(activity!!)
                   linearLayoutManager.orientation=LinearLayoutManager.HORIZONTAL
                   cardsAdapter= CardsAdapter(context!!,walletViewModel.cardResponseData.value!!.getResponseData()!!)
                   fragmentWalletBinding.rvCards.adapter=cardsAdapter
                   fragmentWalletBinding.rvCards.layoutManager=linearLayoutManager

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
        ViewUtils.showToast(activity!!,error,false)
    }


    override fun addAmount(view: View) {
        when(view.id){
            R.id.bt_fifty ->{
               strAmount="50"
            }

            R.id.bt_hundred -> {
                strAmount="100"
            }

            R.id.bt_thousand ->{
                strAmount="1000"
            }
        }
        walletViewModel.walletAmount.value=strAmount

        Log.e("amount","----"+walletViewModel.walletAmount.value.toString())
    }

    override fun validate():Boolean{
        if(walletViewModel.walletAmount.value.isNullOrEmpty()){
            ViewUtils.showToast(activity!!,resources.getString(R.string.empty_wallet_amount),false)
            return  false
        }else{
            return true
        }
    }

}
