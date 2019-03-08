package com.appoets.gojek.provider.views.document

import com.appoets.basemodule.base.BaseViewModel

class DocumentViewModel:BaseViewModel<DocumentNavigator>(){

    //Upload Images
    fun uploadImages(){
        navigator.uploadImages()
    }

    fun uploadPDF(){
        navigator.uploadPDF()
    }
}
