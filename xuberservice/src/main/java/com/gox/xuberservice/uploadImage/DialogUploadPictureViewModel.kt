package com.gox.xuberservice.uploadImage

import com.gox.base.base.BaseViewModel

class  DialogUploadPictureViewModel:BaseViewModel<DialogUploadPictureNavigator>(){

    fun captureImage(){
        navigator.takePicture()
    }

    fun submitPicture(){
        navigator.submit()
    }
}