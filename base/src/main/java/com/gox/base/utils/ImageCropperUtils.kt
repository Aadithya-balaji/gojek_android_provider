package com.gox.base.utils

import android.app.Activity
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView

object ImageCropperUtils {
    fun launchImageCropperActivity(activity: Activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(activity)
    }
}