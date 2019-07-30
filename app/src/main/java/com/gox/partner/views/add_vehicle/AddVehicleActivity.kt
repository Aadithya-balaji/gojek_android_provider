package com.gox.partner.views.add_vehicle

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ImageCropperUtils
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityAddVehicleBinding
import com.gox.partner.models.SetupRideResponseModel
import com.gox.partner.utils.Enums
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_add_vehicle.*
import kotlinx.android.synthetic.main.layout_app_bar.view.*
import java.io.File

class AddVehicleActivity : BaseActivity<ActivityAddVehicleBinding>(), AddVehicleNavigator {

    private lateinit var mBinding: ActivityAddVehicleBinding
    private lateinit var mViewModel: AddVehicleViewModel
    private lateinit var permissions: Array<String>

    private var requestCode: Int = -1

    private lateinit var vehicleData: ArrayList<SetupRideResponseModel.ResponseData.ServiceList>

    override fun getLayoutId() = R.layout.activity_add_vehicle

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        mBinding = mViewDataBinding as ActivityAddVehicleBinding
        mBinding.lifecycleOwner = this

        mViewModel = provideViewModel {
            AddVehicleViewModel()
        }
        mViewModel.navigator = this

        mViewModel.setServiceName(intent.getStringExtra(Constants.SERVICE_ID))
        mViewModel.setCategoryId(intent.getIntExtra(Constants.CATEGORY_ID, -1))


        if (intent.hasExtra(Constants.PROVIDER_TRANSPORT_VEHICLE) || intent.hasExtra(Constants.PROVIDER_ORDER_VEHICLE))
            mViewModel.setIsEdit(true) else mViewModel.setIsEdit(false)

        if (intent.hasExtra(Constants.PROVIDER_TRANSPORT_VEHICLE))
            mViewModel.setVehicleLiveData(intent.getParcelableExtra(Constants.PROVIDER_TRANSPORT_VEHICLE))
        else if (intent.hasExtra(Constants.PROVIDER_ORDER_VEHICLE))
            mViewModel.setVehicleLiveData(intent.getParcelableExtra(Constants.PROVIDER_ORDER_VEHICLE))


        if (intent.hasExtra(Constants.TRANSPORT_VEHICLES)) {
            vehicleData = intent.getSerializableExtra(Constants.TRANSPORT_VEHICLES)
                    as ArrayList<SetupRideResponseModel.ResponseData.ServiceList>
            setVehicle(vehicleData)
        }else{
            mViewModel.specialSeatLiveData.value = false
        }

        mBinding.addVehicleViewModel = mViewModel

        setSupportActionBar(mBinding.toolbar.tbApp)
        mBinding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        mBinding.toolbar.tbApp.tv_toolbar_title.text = resources.getString(R.string.title_add_vehicle)

        observeViewModel()


        txt_category_selection.setOnClickListener {
            spinnerCarCategory.expand()
        }

        observeLiveData(mViewModel.showLoading) {
            loadingObservable.value = it
        }

        spinnerCarCategory.setOnItemSelectedListener { view, position, id, item ->
            run {
                txt_category_selection.setText(item.toString())
                mViewModel.getVehicleData()!!.vehicleId = vehicleData[position].id
                val capacity:Int? = vehicleData[position].capacity
                mViewModel.specialSeatLiveData.value = capacity!=null && capacity >3
            }
        }
    }

    private fun observeViewModel() {
        observeLiveData(mViewModel.getVehicleCategoryObservable()) {
            loadingObservable.value = false
        }
        observeLiveData(mViewModel.getVehicleResponseObservable()) {
            loadingObservable.value = false
            if (!mViewModel.getIsEdit())
                ViewUtils.showToast(this, getString(R.string.vehicle_added_success), true)
            else ViewUtils.showToast(this, getString(R.string.vehicle_update_success), true)
            finish()
        }

        observeLiveData(mViewModel.getVehicleDataObservable()) { vehicleData ->
            run {
                Glide.with(this)
                        .load(vehicleData.vehicleImage)
                        .placeholder(R.drawable.ic_car_placeholder)
                        .circleCrop()
                        .into(iv_vehicle)

                Glide.with(this)
                        .load(vehicleData.vehicleRcBook)
                        .into(this.iv_rc_book)

                Glide.with(this)
                        .load(vehicleData.vehicleInsurance)
                        .into(iv_insurance)
            }
        }
    }

    private fun setVehicle(vehicleData: ArrayList<SetupRideResponseModel.ResponseData.ServiceList>) {
        if (!vehicleData.isNullOrEmpty()) {
            spinnerCarCategory.setItems(vehicleData)
            if (mViewModel.getVehicleData()!!.vehicleId != 0) {
                val vehiclePosition = vehicleData.indexOfFirst { data -> data.id == mViewModel.getVehicleData()!!.vehicleId }
                spinnerCarCategory.selectedIndex = vehiclePosition
                txt_category_selection.setText(vehicleData[vehiclePosition].vehicleName)
                val capacity:Int? = vehicleData[vehiclePosition].capacity
                mViewModel.specialSeatLiveData.value = capacity!=null && capacity >3
            } else {
                spinnerCarCategory.selectedIndex = 0
                txt_category_selection.setText(vehicleData[0].vehicleName)
                mViewModel.getVehicleData()!!.vehicleId = vehicleData[0].id
                mViewModel.specialSeatLiveData.value = false
            }
        }
    }

    private fun performValidation() {
        ViewUtils.hideSoftInputWindow(this)
        val data = mViewModel.getVehicleData()
        val isTransport = mViewModel.getServiceName() == mViewModel.getTransportServiceName()
        if (!isTransport) {
            when {
                data?.vehicleMake.isNullOrEmpty() ->
                    ViewUtils.showToast(this,
                            getString(R.string.please_enter_vehicle_name), false)
                data?.vehicleNumber.isNullOrEmpty() ->
                    ViewUtils.showToast(this,
                            getString(R.string.please_enter_vehicle_number), false)
                (!mViewModel.getIsEdit() && mViewModel.getRcBookUri() == null) ->
                    ViewUtils.showToast(this,
                            getString(R.string.please_select_rc_book_document), false)
                (!mViewModel.getIsEdit() && mViewModel.getInsuranceUri() == null) ->
                    ViewUtils.showToast(this,
                            getString(R.string.please_select_insurance_document), false)
                else -> mViewModel.postVehicle()
            }
        } else when {
            /*(data?.vehicleId==0) -> {
                ViewUtils.showLog(this, getString(R.string.please_enter_vehicle_name), false)
            }*/
            data?.vehicleModel.isNullOrEmpty() -> ViewUtils.showToast(this,
                    getString(R.string.please_enter_vehicle_model), false)

            data?.vehicleYear.isNullOrEmpty() -> ViewUtils.showToast(this,
                    getString(R.string.please_enter_vehicle_year), false)

            data?.vehicleColor.isNullOrEmpty() -> ViewUtils.showToast(this,
                    getString(R.string.please_enter_vehicle_color), false)

            data?.vehicleNumber.isNullOrEmpty() -> ViewUtils.showToast(this,
                    getString(R.string.please_enter_vehicle_plate_number), false)

            data?.vehicleMake.isNullOrEmpty() -> ViewUtils.showToast(this,
                    getString(R.string.please_enter_vehicle_make), false)

            (!mViewModel.getIsEdit() && mViewModel.getRcBookUri() == null) -> ViewUtils.showToast(this,
                    getString(R.string.please_select_rc_book_document), false)

            (!mViewModel.getIsEdit() && mViewModel.getInsuranceUri() == null) -> ViewUtils.showToast(this,
                    getString(R.string.please_select_insurance_document), false)

            else -> mViewModel.postVehicle()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) when (this.requestCode) {
                    Enums.RC_VEHICLE_IMAGE -> {
                        mViewModel.setVehicleUri(result.uri)
                        Glide.with(this).load(File(result.uri.path))
                                .placeholder(R.drawable.ic_car_placeholder)
                                .circleCrop().into(iv_vehicle)
                    }
                    Enums.RC_RC_BOOK_IMAGE -> {
                        mViewModel.setRcBookUri(result.uri)
                        Glide.with(this).load(File(result.uri.path))
                                .into(iv_rc_book)
                        tvRcBook.visibility = View.GONE
                    }
                    Enums.RC_INSURANCE_IMAGE -> {
                        mViewModel.setInsuranceUri(result.uri)
                        Glide.with(this).load(File(result.uri.path))
                                .into(iv_insurance)
                        tvInsurance.visibility = View.GONE
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
