package com.xjek.provider.views.add_vehicle

import android.app.Activity
import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ImageCropperUtils
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
        binding.addVehicleViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.title_add_vehicle)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun onVehicleImageClicked() {
        ImageCropperUtils.launchImageCropperActivity(this)
    }

    override fun onRcBookClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onInsuranceClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onVehicleSubmitClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
