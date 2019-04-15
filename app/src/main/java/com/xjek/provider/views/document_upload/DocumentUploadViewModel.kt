package com.xjek.provider.views.document_upload

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.DocumentUploadDataModel

class DocumentUploadViewModel : BaseViewModel<DocumentUploadNavigator>() {

    private lateinit var documentData: DocumentUploadDataModel
    var expiryDate = MutableLiveData<String>()

    fun getDocumentData(): DocumentUploadDataModel {
        return documentData
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun setDocumentData(documentUploadDataModel: DocumentUploadDataModel) {
        documentData = documentUploadDataModel
    }

//    fun setExpiryDateObservable(expiryDate: MutableLiveData<String>) {
//        this.expiryDate = expiryDate
//    }
//
//    fun getExpiryDateObservable(): MutableLiveData<String> {
//        return expiryDate
//    }

    fun onExpiryDateClick(view: View) {
        navigator.onDateChanged()
    }
}