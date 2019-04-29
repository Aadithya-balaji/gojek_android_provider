package com.xjek.xuberservice.serviceStatus

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Chronometer
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xjek.base.base.BaseBottomSheet
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.BottomSheetServiceBinding
import com.xjek.xuberservice.uploadImage.DialogUploadPicture

class ServiceBottomSheet : BaseBottomSheet<BottomSheetServiceBinding>(), ServiceBottomSheetNavigator, Chronometer.OnChronometerTickListener, View.OnClickListener {

    private lateinit var bottomSheetServiceBinding: BottomSheetServiceBinding
    private lateinit var bottomSheetViewModel: ServiceBottomSheetViewModel
    private var isWaitingTime: Boolean? = false
    private var lastWaitingTime: Long? = 0


    override fun getLayout(): Int {
        return R.layout.bottom_sheet_service
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }


    override fun initView(mViewDataBinding: ViewDataBinding) {
        bottomSheetServiceBinding = mViewDataBinding as BottomSheetServiceBinding
        bottomSheetViewModel = ServiceBottomSheetViewModel()
        bottomSheetViewModel.navigator = this
        bottomSheetServiceBinding.servicemodel = bottomSheetViewModel
        bottomSheetServiceBinding.setLifecycleOwner(this)
        bottomSheetServiceBinding.cmServiceTime.setOnClickListener(this)
    }

    override fun opentPictureDialog() {
        val dialogUploadPicture = DialogUploadPicture()
        dialogUploadPicture.show(activity!!.supportFragmentManager, "takepicture")
    }

    override fun onChronometerTick(chronometer: Chronometer?) {
        val time = SystemClock.elapsedRealtime() - chronometer!!.getBase()
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
        val t = (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
        bottomSheetServiceBinding.cmServiceTime.setText(t)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.cm_service_time -> {
                if (isWaitingTime == true) {
                    isWaitingTime = false
                    lastWaitingTime = SystemClock.elapsedRealtime()
                    bottomSheetServiceBinding.cmServiceTime.stop()
                } else {
                    val temp: Long = 0
                    if (lastWaitingTime != temp)
                        bottomSheetServiceBinding.cmServiceTime.base = (bottomSheetServiceBinding.cmServiceTime.base + SystemClock.elapsedRealtime()) - lastWaitingTime!!
                    else
                        bottomSheetServiceBinding.cmServiceTime.base = SystemClock.elapsedRealtime()
                    bottomSheetServiceBinding.cmServiceTime.start()
                }
            }

        }
    }


}