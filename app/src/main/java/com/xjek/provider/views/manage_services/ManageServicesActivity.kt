package com.xjek.provider.views.manage_services

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityManageServicesBinding
import com.xjek.provider.models.ManageServicesDataModel
import com.xjek.provider.views.setup_vehicle.SetupVehicleActivity
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class ManageServicesActivity : BaseActivity<ActivityManageServicesBinding>(), ManageServicesNavigator {

    private lateinit var binding: ActivityManageServicesBinding
    private lateinit var viewModel: ManageServicesViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_manage_services
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityManageServicesBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            ManageServicesViewModel()
        }
        viewModel.navigator = this
        binding.manageServicesViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.title_manage_services)

        val colors = resources.getIntArray(R.array.color_manage_services)
        val icons = resources.obtainTypedArray(R.array.icon_manage_services)
        val titles = resources.getStringArray(R.array.title_manage_services)
        val descriptions = resources.getStringArray(R.array.desc_manage_services)
        val services = List(titles.size) {
            ManageServicesDataModel(colors[it], icons.getResourceId(it, -1), titles[it],
                    descriptions[it])
        }
        icons.recycle()
        viewModel.setServices(services)
        viewModel.setAdapter()
    }

    override fun onMenuItemClicked(position: Int) {
        var intent: Intent? = null
        when (position) {
            0 -> {
                intent = Intent(applicationContext, SetupVehicleActivity::class.java)
            }
            1 -> {
                intent = Intent(applicationContext, SetupVehicleActivity::class.java)
            }
            2 -> {
                intent = Intent(applicationContext, SetupVehicleActivity::class.java)
            }
        }
        startActivity(intent)
    }
}
