package com.xjek.xuberservice.uploadImage

import com.xjek.base.base.BaseViewModel

class  DialogUploadPictureViewModel:BaseViewModel<DialogUploadPictureNavigator>(){

    fun captureImage(){
        navigator.takePicture()
    }

    fun submitPicture(){
        navigator.submit()
    }
}