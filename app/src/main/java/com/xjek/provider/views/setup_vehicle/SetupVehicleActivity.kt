package com.xjek.provider.views.setup_vehicle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityManageServicesBinding
import com.xjek.provider.databinding.ActivitySetupVehicleBinding
import com.xjek.provider.views.manage_services.ManageServicesViewModel
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class SetupVehicleActivity : BaseActivity<ActivitySetupVehicleBinding>(), SetupVehicleNavigator {

    private lateinit var binding: ActivitySetupVehicleBinding
    private lateinit var viewModel: SetupVehicleViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_setup_vehicle
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySetupVehicleBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            SetupVehicleViewModel()
        }
        viewModel.navigator = this
        binding.setupVehicleViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.title_setup_vehicle)

        viewModel.setAdapter()
    }

    override fun onMenuItemClicked(position: Int) {

    }
}
