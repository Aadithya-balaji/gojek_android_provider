package com.xjek.provider.views.add_vehicle

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.PopupWindow
import androidx.databinding.ViewDataBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.xjek.base.base.BaseActivity
import com.xjek.base.data.PreferencesHelper.message
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ImageCropperUtils
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityAddVehicleBinding
import com.xjek.provider.utils.Constant
import com.xjek.provider.utils.Enums
import kotlinx.android.synthetic.main.activity_add_vehicle.*
import kotlinx.android.synthetic.main.layout_app_bar.view.*


class AddVehicleActivity : BaseActivity<ActivityAddVehicleBinding>(), AddVehicleNavigator {

    private lateinit var binding: ActivityAddVehicleBinding
    private lateinit var viewModel: AddVehicleViewModel

    private lateinit var permissions: Array<String>
    private var requestCode: Int = -1


    override fun getLayoutId(): Int {
        return R.layout.activity_add_vehicle
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        binding = mViewDataBinding as ActivityAddVehicleBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            AddVehicleViewModel()
        }
        viewModel.navigator = this
        viewModel.setServiceId(intent.getIntExtra(Constant.SERVICE_ID, -1))
        if (intent.hasExtra(Constant.PROVIDER_VEHICLE))
            viewModel.setVehicleLiveData(intent.getParcelableExtra(Constant.PROVIDER_VEHICLE))
        binding.addVehicleViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.title_add_vehicle)

        observeViewModel()

        getVehicleCategories()

        txt_category_selection.setOnClickListener {
            val popupWindow = PopupWindow(this)
            popupWindow.showAsDropDown(it,-5,0)
        }
    }


    private fun observeViewModel() {
        observeLiveData(viewModel.getVehicleCategoryObservable()) {
            loadingObservable.value = false

        }
        observeLiveData(viewModel.getVehicleResponseObservable()) {
            loadingObservable.value = false
        }
    }

    private fun getVehicleCategories() {
        loadingObservable.value = true
        viewModel.getVehicleCategories()
    }

    private fun performValidation() {
        ViewUtils.hideSoftInputWindow(this)
        if (viewModel.isVehicleDataValid()) {
            loadingObservable.value = true
            viewModel.postVehicle()
        } else {
            ViewUtils.showToast(applicationContext, message, false)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    when (this.requestCode) {
                        Enums.RC_VEHICLE_IMAGE -> {
                            viewModel.setVehicleUri(result.uri)
                            binding.ivVehicle.setImageURI(result.uri)
                        }
                        Enums.RC_RC_BOOK_IMAGE -> {
                            viewModel.setRcBookUri(result.uri)
                            binding.ivRcBook.setImageURI(result.uri)
                            tvRcBook.visibility = View.GONE
                        }
                        Enums.RC_INSURANCE_IMAGE -> {
                            viewModel.setInsuranceUri(result.uri)
                            binding.ivInsurance.setImageURI(result.uri)
                            tvInsurance.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onVehicleImageClicked() {
        if (getPermissionUtil().hasPermission(this, permissions)) {
            requestCode = Enums.RC_VEHICLE_IMAGE
            ImageCropperUtils.launchImageCropperActivity(this)
        } else {
            getPermissionUtil().requestPermissions(this, permissions, Enums.FILE_REQ_CODE)
        }
    }

    override fun onRcBookClicked() {
        if (getPermissionUtil().hasPermission(this, permissions)) {
            requestCode = Enums.RC_RC_BOOK_IMAGE
            ImageCropperUtils.launchImageCropperActivity(this)
        } else {
            getPermissionUtil().requestPermissions(this, permissions, Enums.FILE_REQ_CODE)
        }
    }

    override fun onInsuranceClicked() {
        if (getPermissionUtil().hasPermission(this, permissions)) {
            requestCode = Enums.RC_INSURANCE_IMAGE
            ImageCropperUtils.launchImageCropperActivity(this)
        } else {
            getPermissionUtil().requestPermissions(this, permissions, Enums.FILE_REQ_CODE)
        }
    }

    override fun onVehicleSubmitClicked() {
        performValidation()
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}
