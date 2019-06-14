package com.gox.xuberservice.uploadImage

import com.gox.base.base.BaseViewModel

class UploadPictureDialogViewModel : BaseViewModel<UploadPictureDialogNavigator>() {

    fun captureImage() = navigator.takePicture()

    fun submitPicture() = navigator.submit()
}