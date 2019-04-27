package com.xjek.xuberservice.serviceStatus

import com.xjek.base.base.BaseViewModel

class  ServiceBottomSheetViewModel:BaseViewModel<ServiceBottomSheetNavigator>(){
     fun  openCameraDialog(){
         navigator.opentPictureDialog()
     }
}