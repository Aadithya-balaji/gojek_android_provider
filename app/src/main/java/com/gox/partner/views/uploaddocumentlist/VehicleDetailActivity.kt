package com.gox.partner.views.uploaddocumentlist

import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.partner.R
import com.gox.partner.databinding.ActivityVechileDetailPageBinding
import com.gox.partner.views.adapters.ServiceListAdapter
import com.gox.partner.views.verifyfile.VerifyFileActivity

class VehicleDetailActivity : BaseActivity<ActivityVechileDetailPageBinding>(), VehicleDetailNavigator {

    private var mBinding: ActivityVechileDetailPageBinding? = null

    private lateinit var llTaxiVehicleDetail: CoordinatorLayout
    private lateinit var llFoodieDetail: LinearLayout
    private lateinit var llService: LinearLayout

    private var serviceType = -1

    override fun getLayoutId() = R.layout.activity_vechile_detail_page

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityVechileDetailPageBinding
        val mViewModel = VehicleDetailModel()
        mBinding!!.vechiledetailmodel = mViewModel

        llTaxiVehicleDetail = findViewById(R.id.cl_taxi)
        llFoodieDetail = findViewById(R.id.ll_foodie)
        llService = findViewById(R.id.ll_service)

        getIntentValues()

        changeLayout()

        val services = resources.getStringArray(R.array.document_list)
        services.toList()

        ServiceListAdapter(this)
    }

    private fun getIntentValues() {
        serviceType = if (intent != null && intent.hasExtra("servicetype"))
            intent.getIntExtra("servicetype", -1) else -1
    }

    private fun changeLayout() {
        when (serviceType) {
            0 -> llTaxiVehicleDetail.visibility = View.VISIBLE

            1 -> llFoodieDetail.visibility = View.VISIBLE

            2 -> llService.visibility = View.VISIBLE
        }
    }

    override fun gotoVerificationPage() =
            startActivity(Intent(this, VerifyFileActivity::class.java))
}
