package com.xjek.foodservice.view

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseActivity
import com.xjek.foodservice.R
import com.xjek.foodservice.databinding.ActivtyLivetaskLaoyutBinding


class FoodLiveTaskServiceFlow : BaseActivity<ActivtyLivetaskLaoyutBinding>(), FoodLiveTaskServiceNavigator {


    lateinit var mViewDataBinding: ActivtyLivetaskLaoyutBinding
    var i = 0
    override fun getLayoutId(): Int = R.layout.activty_livetask_laoyut

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivtyLivetaskLaoyutBinding
        val foodLiveTaskviewModel = ViewModelProviders.of(this).get(FoodLiveTaskServiceViewModel::class.java)
        mViewDataBinding.foodLiveTaskviewModel = foodLiveTaskviewModel
        mViewDataBinding.orderItemListAdpter = OrderItemListAdapter(this)
        foodLiveTaskviewModel.navigator = this
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun checkOrderDeliverStatus() {
        when (i) {
            0 -> {
                mViewDataBinding.timeLeftRl.visibility = View.GONE
                mViewDataBinding.serviceFlowIconLlayout.visibility = View.VISIBLE
                mViewDataBinding.orderStatusBtn.text = getString(R.string.reached_restaurant)
            }
            1 -> {
                mViewDataBinding.orderStatusBtn.text = getString(R.string.reached_restaurant)

            }
            2 -> {
                mViewDataBinding.orderStatusBtn.text = getString(R.string.order_picked_up)
                mViewDataBinding.iconOrderPickedUp.background = ContextCompat.getDrawable(this, R.drawable.round_grey)
                mViewDataBinding.iconReachedRestaurant.background = ContextCompat.getDrawable(this, R.drawable.round_accent)
                mViewDataBinding.iconOrderPickedUp.imageTintList = ContextCompat.getColorStateList(this, R.color.white)

            }
            3 -> {
                mViewDataBinding.orderStatusBtn.text = getString(R.string.order_delivered)
                mViewDataBinding.iconOrderDelivered.background = ContextCompat.getDrawable(this, R.drawable.round_grey)
                mViewDataBinding.iconOrderDelivered.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
                mViewDataBinding.iconOrderPickedUp.background = ContextCompat.getDrawable(this, R.drawable.round_accent)


            }
            4 -> {
                mViewDataBinding.orderStatusBtn.text = getString(R.string.payment_received)

                mViewDataBinding.iconPaymentReceived.background = ContextCompat.getDrawable(this, R.drawable.round_grey)
                mViewDataBinding.iconPaymentReceived.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
                mViewDataBinding.iconOrderDelivered.background = ContextCompat.getDrawable(this, R.drawable.round_accent)
            }

            5 -> {
                mViewDataBinding.iconPaymentReceived.background = ContextCompat.getDrawable(this, R.drawable.round_accent)
                showFeedBackAlert()

            }


        }
        ++i
    }

    private fun showFeedBackAlert() {

      /*  val dialogView = LayoutInflater.from(this).inflate(R.layout.food_feedback_dialog, null)
        val feedbackdialogbinding = DataBindingUtil.bind<FoodFeedbackDialogBinding>(dialogView)
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setView(dialogView)
        mBuilder.show()
*/
    }
}