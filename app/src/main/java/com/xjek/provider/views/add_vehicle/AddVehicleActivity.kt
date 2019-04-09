package com.xjek.provider.views.add_vehicle

import androidx.cardview.widget.CardView
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityAddVehicleBinding
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class AddVehicleActivity : BaseActivity<ActivityAddVehicleBinding>(), AddVehicleNavigator {

    private lateinit var binding: ActivityAddVehicleBinding
    private lateinit var viewModel: AddVehicleViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_add_vehicle
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityAddVehicleBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            AddVehicleViewModel()
        }
        viewModel.navigator = this

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.title_add_vehicle)
    }
}
