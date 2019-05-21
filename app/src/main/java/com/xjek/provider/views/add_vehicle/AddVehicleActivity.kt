package com.xjek.provider.views.add_vehicle

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ImageCropperUtils
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityAddVehicleBinding
import com.xjek.provider.models.SetupRideResponseModel
import com.xjek.provider.utils.Constant
import com.xjek.provider.utils.Enums
import kotlinx.android.synthetic.main.activity_add_vehicle.*
import kotlinx.android.synthetic.main.layout_app_bar.view.*
import java.io.File


class AddVehicleActivity : BaseActivity<ActivityAddVehicleBinding>(), AddVehicleNavigator {

    private lateinit var binding: ActivityAddVehicleBinding
    private lateinit var viewModel: AddVehicleViewModel

    private lateinit var permissions: Array<String>
    private var requestCode: Int = -1


    private lateinit var vehicleData:ArrayList<SetupRideResponseModel.ResponseData.ServiceList>

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
        viewModel.setCategoryId(intent.getIntExtra(Constant.CATEGORY_ID, -1))


        if (intent.hasExtra(Constant.PROVIDER_TRANSPORT_VEHICLE) || intent.hasExtra(Constant.PROVIDER_ORDER_VEHICLE)) {
            viewModel.setIsEdit(true)
        } else {
            viewModel.setIsEdit(false)
        }

        if (intent.hasExtra(Constant.TRANSPORT_VEHICLES)) {
            vehicleData = intent.getSerializableExtra(Constant.TRANSPORT_VEHICLES) as ArrayList<SetupRideResponseModel.ResponseData.ServiceList>
            setVehicle(vehicleData)
        }

        if (intent.hasExtra(Constant.PROVIDER_TRANSPORT_VEHICLE)) {
            viewModel.setVehicleLiveData(intent.getParcelableExtra(Constant.PROVIDER_TRANSPORT_VEHICLE))
        } else if (intent.hasExtra(Constant.PROVIDER_ORDER_VEHICLE)) {
            viewModel.setVehicleLiveData(intent.getParcelableExtra(Constant.PROVIDER_ORDER_VEHICLE))
        }

        binding.addVehicleViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text = resources.getString(R.string.title_add_vehicle)

        observeViewModel()

       /* if (viewModel.getServiceId() == viewModel.getTransportId())
            getVehicleCategories()*/

        txt_category_selection.setOnClickListener {
            spinnerCarCategory.expand()
        }

        observeLiveData(viewModel.loadingObservable) {
            loadingObservable.value = it
        }

        spinnerCarCategory.setOnItemSelectedListener { view, position, id, item ->
            run {
                txt_category_selection.setText(item.toString())
                viewModel.getVehicleData()!!.vehicleId = vehicleData[position].id
            }
        }


    }


    private fun observeViewModel() {
        observeLiveData(viewModel.getVehicleCategoryObservable()) {
            loadingObservable.value = false
            //setVehicle(it)
        }
        observeLiveData(viewModel.getVehicleResponseObservable()) {
            loadingObservable.value = false
            if (!viewModel.getIsEdit()) {
                ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.vehicle_added_success), true)
            } else {
                ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.vehicle_update_success), true)
            }
            finish()
        }

        observeLiveData(viewModel.getVehicleDataObservable()) { vehicleData ->
            run {

                Glide.with(this@AddVehicleActivity)
                        .load(vehicleData.vehicleImage)
                        .placeholder(R.drawable.vehicle_place_holder)
                        .into(iv_vehicle)

                Glide.with(this@AddVehicleActivity)
                        .load(vehicleData.vehicleRcBook)
                        .into(iv_rc_book)

                Glide.with(this@AddVehicleActivity)
                        .load(vehicleData.vehicleInsurance)
                        .into(iv_insurance)

                Log.v("SK_TEST", "SK_TEST VEHICLE ${vehicleData.vehicleMake}")

            }
        }
    }

    /*private fun setVehicle(it: VehicleCategoryResponseModel) {
        val vehicleData = it.responseData.transport
        spinnerCarCategory.setItems(vehicleData)
        if (viewModel.getVehicleData()!!.vehicleId!! > 0) {
            val vehiclePosition = vehicleData.indexOfFirst { data -> data.id == viewModel.getVehicleData()!!.vehicleId }
            spinnerCarCategory.selectedIndex = vehiclePosition
        }
    }*/

    private fun setVehicle(vehicleData:ArrayList<SetupRideResponseModel.ResponseData.ServiceList>) {
        spinnerCarCategory.setItems(vehicleData)
        if (viewModel.getVehicleData()!!.vehicleId!! > 0) {
            val vehiclePosition = vehicleData.indexOfFirst { data -> data.id == viewModel.getVehicleData()!!.vehicleId }
            spinnerCarCategory.selectedIndex = vehiclePosition
            txt_category_selection.setText(vehicleData[vehiclePosition].vehicleName)
        }else{
            spinnerCarCategory.selectedIndex = 0
            txt_category_selection.setText(vehicleData[0].vehicleName)
        }
    }

    private fun getVehicleCategories() {
        loadingObservable.value = true
        viewModel.getVehicleCategories()
    }

    private fun performValidation() {
        ViewUtils.hideSoftInputWindow(this)

        val isTransport = viewModel.getServiceId() == viewModel.getTransportId()
        val data = viewModel.getVehicleData()
        if (!isTransport) {
            when {
                data?.vehicleMake.isNullOrEmpty() -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_enter_vehicle_name), false)
                }
                data?.vehicleNumber.isNullOrEmpty() -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_enter_vehicle_number), false)
                }
                (!viewModel.getIsEdit() && viewModel.getRcBookUri() == null) -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_select_rc_book_document), false)
                }
                (!viewModel.getIsEdit() && viewModel.getInsuranceUri() == null) -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_select_insurance_document), false)
                }
                else -> {
                    viewModel.postVehicle()
                }
            }
        } else {
            when {
                /*(data?.vehicleId==0) -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_enter_vehicle_name), false)
                }*/
                data?.vehicleModel.isNullOrEmpty() -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_enter_vehicle_model), false)
                }
                data?.vehicleYear.isNullOrEmpty() -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_enter_vehicle_year), false)
                }
                data?.vehicleColor.isNullOrEmpty() -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_enter_vehicle_color), false)
                }
                data?.vehicleNumber.isNullOrEmpty() -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_enter_vehicle_plate_number), false)
                }
                data?.vehicleMake.isNullOrEmpty() -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_enter_vehicle_make), false)
                }
                (!viewModel.getIsEdit() && viewModel.getRcBookUri() == null) -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_select_rc_book_document), false)
                }
                (!viewModel.getIsEdit() && viewModel.getInsuranceUri() == null) -> {
                    ViewUtils.showToast(this@AddVehicleActivity, getString(R.string.please_select_insurance_document), false)
                }
                else -> {
                    viewModel.postVehicle()
                }
            }
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

                            Glide.with(this@AddVehicleActivity)
                                    .load(File(result.uri.path))
                                    .placeholder(R.drawable.vehicle_place_holder)
                                    .into(iv_vehicle)
                            // binding.ivVehicle.setImageBitmap(result.bitmap)

                        }
                        Enums.RC_RC_BOOK_IMAGE -> {
                            viewModel.setRcBookUri(result.uri)
                            //binding.ivRcBook.setImageURI(result.uri)
                            //  binding.ivRcBook.setImageBitmap(result.bitmap)
                            Glide.with(this@AddVehicleActivity)
                                    .load(File(result.uri.path))
                                    .into(iv_rc_book)
                            tvRcBook.visibility = View.GONE
                        }
                        Enums.RC_INSURANCE_IMAGE -> {
                            viewModel.setInsuranceUri(result.uri)
//                            binding.ivInsurance.setImageURI(result.uri)
                            //binding.ivInsurance.setImageBitmap(result.bitmap)
                            Glide.with(this@AddVehicleActivity)
                                    .load(File(result.uri.path))
                                    .into(iv_insurance)
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
