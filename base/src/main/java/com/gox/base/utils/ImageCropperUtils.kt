package com.gox.base.utils

import android.app.Activity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

object ImageCropperUtils {
    fun launchImageCropperActivity(activity: Activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(activity)
    }
}