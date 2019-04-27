package com.xjek.xuberservice.serviceStatus

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xjek.base.base.BaseBottomSheet
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.BottomSheetServiceBinding
import com.xjek.xuberservice.uploadImage.DialogUploadPicture

class  ServiceBottomSheet:BaseBottomSheet<BottomSheetServiceBinding>(),ServiceBottomSheetNavigator{

    private  lateinit var  bottomSheetServiceBinding: BottomSheetServiceBinding
    private lateinit var  bottomSheetViewModel: ServiceBottomSheetViewModel
    override fun getLayout(): Int {
        return R.layout.bottom_sheet_service
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }


    override fun initView(mViewDataBinding: ViewDataBinding) {
        bottomSheetServiceBinding =mViewDataBinding as BottomSheetServiceBinding
        bottomSheetViewModel= ServiceBottomSheetViewModel()
        bottomSheetViewModel.navigator=this
        bottomSheetServiceBinding.servicemodel=bottomSheetViewModel
        bottomSheetServiceBinding.setLifecycleOwner(this)
    }

    override fun opentPictureDialog() {
         val dialogUploadPicture =DialogUploadPicture()
         dialogUploadPicture.show(activity!!.supportFragmentManager,"takepicture")
    }

}