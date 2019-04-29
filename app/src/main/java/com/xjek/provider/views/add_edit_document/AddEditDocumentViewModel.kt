package com.xjek.provider.views.add_edit_document

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.AddDocumentResponse
import com.xjek.provider.models.ListDocumentResponse
import com.xjek.provider.repository.AppRepository

class AddEditDocumentViewModel : BaseViewModel<DocumentUploadNavigator>() {

    private val appRepository = AppRepository.instance()

    var showLoading = MutableLiveData<Boolean>()
    var documentResponse = MutableLiveData<ListDocumentResponse>()
    var addDocumentResponse = MutableLiveData<AddDocumentResponse>()
    var errorResponse = MutableLiveData<String>()
    var addEditDocumentErrorResponse = MutableLiveData<String>()
    var showEmpty: MutableLiveData<Boolean> = MutableLiveData(false)
    var currentPosition:Int = 0

    var expiryDate = MutableLiveData<String>()

    fun getDocumentList(documentType:String){
        getCompositeDisposable().add(appRepository.getDocumentList(this,documentType))
    }



    fun getDocumentFrontName():String{
        val response = documentResponse.value!!.responseData
        return if (response.isNotEmpty())
            response[currentPosition].name+ " (Front)"
        else
            "Front"
    }


    fun getDocumentBackName():String{
        val response = documentResponse.value!!.responseData
        return if (response.isNotEmpty())
            response[currentPosition].name+ " (Back)"
        else
            "Back"
    }

    fun selectFrontImage(){

    }

    fun selectBackImage(){

    }

    fun onExpiryDateClick() {
        navigator.onDateChanged()
    }
}