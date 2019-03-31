package com.xjek.provider.views.document

import com.xjek.base.base.BaseViewModel

class DocumentViewModel:BaseViewModel<DocumentNavigator>(){

    //Upload Images
    fun uploadImages(){
        navigator.uploadImages()
    }

    fun uploadPDF(){
        navigator.uploadPDF()
    }
}
