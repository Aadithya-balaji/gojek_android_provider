package com.xjek.provider.views.uploaddocumentlist

import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityVechileDetailPageBinding
import com.xjek.provider.views.adapters.ServiceListAdapter
import com.xjek.provider.views.verifyfile.VerifyFileActivity

class VechileDetailActivity : BaseActivity<ActivityVechileDetailPageBinding>(), VechileDetailNavigator {
    private var mVechileDetailPageBinding: ActivityVechileDetailPageBinding? = null
    private lateinit var llTaxiVechileDetail: CoordinatorLayout
    private lateinit var llFoodieDetail: LinearLayout
    private lateinit var llService: LinearLayout

    private var serviceType: Int = -1


    override fun getLayoutId(): Int {
        return R.layout.activity_vechile_detail_page
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mVechileDetailPageBinding = mViewDataBinding as ActivityVechileDetailPageBinding
        val vechileDetailModel = VechileDetailModel()
        mVechileDetailPageBinding!!.vechiledetailmodel = vechileDetailModel

        llTaxiVechileDetail = findViewById(R.id.cl_taxi)
        llFoodieDetail = findViewById(R.id.ll_foodie)
        llService = findViewById(R.id.ll_service)

        //get IntentViewlues
        getIntentValues()

        //Change Page based on Service type
        changeLayout()

        val services = resources.getStringArray(R.array.document_list)
        val list = services.toList()

        val serviceListAdapter = ServiceListAdapter(this, list)
    }

    fun getIntentValues() {
        serviceType = if (intent != null && intent.hasExtra("servicetype")) intent.getIntExtra("servicetype", -1) else -1
    }


    fun changeLayout() {
        when (serviceType) {
            0 -> {
                llTaxiVechileDetail.visibility = View.VISIBLE
            }


            1 -> {
                llFoodieDetail.visibility = View.VISIBLE
            }

            2 -> {
                llService.visibility = View.VISIBLE
            }
        }
    }

    override fun gotoVerificationPage() {
        val intent = Intent(this@VechileDetailActivity, VerifyFileActivity::class.java)
        startActivity(intent)
    }

}
