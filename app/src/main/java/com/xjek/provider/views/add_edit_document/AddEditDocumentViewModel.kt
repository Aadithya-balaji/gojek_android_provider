package com.xjek.provider.views.add_edit_document

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.AddDocumentResponse
import com.xjek.provider.models.ListDocumentResponse
import com.xjek.provider.repository.AppRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AddEditDocumentViewModel : BaseViewModel<DocumentUploadNavigator>() {

    private val appRepository = AppRepository.instance()
    private lateinit var data: List<ListDocumentResponse.ResponseData>

    var showLoading = MutableLiveData<Boolean>()
    var documentResponse = MutableLiveData<ListDocumentResponse>()
    var addDocumentResponse = MutableLiveData<AddDocumentResponse>()
    var errorResponse = MutableLiveData<String>()
    var addEditDocumentErrorResponse = MutableLiveData<String>()
    var showEmpty: MutableLiveData<Boolean> = MutableLiveData(false)
    var currentPosition: Int = 0

    var documentFrontName = MutableLiveData<String>()
    var documentBackName = MutableLiveData<String>()
    var documentFrontImageURL = MutableLiveData<String>()
    var documentBackImageURL = MutableLiveData<String>()
    var documentFrontImageFile = MutableLiveData<File>()
    var documentBackImageFile = MutableLiveData<File>()
    var expiryDate = MutableLiveData<String>()

    var showBackSide = MutableLiveData<Boolean>()
    var showExpiry = MutableLiveData<Boolean>()
    var showFrontView = MutableLiveData<Boolean>()
    var showBackView = MutableLiveData<Boolean>()

    fun getDocumentList(documentType: String) {
        showLoading.value = true
        getCompositeDisposable().add(appRepository.getDocumentList(this, documentType))
    }


    fun setData(data: List<ListDocumentResponse.ResponseData>) {
        this.data = data
        if (data.isNotEmpty()) {
            updateDetails()
            showEmpty.value = true
        } else
            showEmpty.value = true
    }

    private fun updateDetails() {
        documentFrontName.value = data[currentPosition].name + " (Front)"
        documentBackName.value = data[currentPosition].name + " (Back)"
        showBackSide.value = data[currentPosition].is_backside != null && data[currentPosition].is_backside!! == "1"
        showExpiry.value = data[currentPosition].is_expire == "1"
        if (data[currentPosition].provider_document != null) {
            showFrontView.value = true
            expiryDate.value = data[currentPosition].provider_document?.expires_at
            documentFrontImageURL.value = data[currentPosition].provider_document!!.url[0].url
            if (data[currentPosition].provider_document!!.url.size > 1) {
                documentBackImageURL.value = data[currentPosition].provider_document!!.url[1].url
                showBackView.value = true
            }
        } else {
            showFrontView.value = false
            showBackView.value = false
            documentFrontImageURL.value = ""
            documentBackImageURL.value = ""
        }
    }


    fun selectFrontImage() {
        navigator.selectFrontImage()
    }

    fun selectBackImage() {
        navigator.selectBackImage()
    }

    fun onExpiryDateClick() {
        navigator.onDateChanged()
    }

    fun submitDocument() {
        navigator.submitDocument()
    }

    fun incrementPosition() {
        if (data.size > (currentPosition+1)) {
            currentPosition += 1
            updateDetails()
        }
    }

    fun updateDocument() {
        showLoading.value = true


        val hashMap: HashMap<String, RequestBody> = HashMap()
        if (!expiryDate.value.isNullOrEmpty()) {
            hashMap["expires_at"] = RequestBody.create(MediaType.parse("text/plain"), expiryDate.value!!)
        }
        hashMap["document_id"] = RequestBody.create(MediaType.parse("text/plain"), data[currentPosition].id.toString())

        var frontImageRequestBody: RequestBody? = null
        if (documentFrontImageFile.value != null) {
            frontImageRequestBody = RequestBody.create(
                    MediaType.parse("*/*"),
                    documentFrontImageFile.value)
        }

        var backImageFile: RequestBody? = null

        if (documentBackImageFile.value != null) {
            backImageFile = RequestBody.create(
                    MediaType.parse("*/*"),
                    documentBackImageFile.value)
        }

        var fileFrontImageBody: MultipartBody.Part? = null
        if (frontImageRequestBody != null) {
            fileFrontImageBody = MultipartBody.Part.createFormData("file[0]", data[currentPosition].name + "_front", frontImageRequestBody)
        }

        var fileBackImageBody: MultipartBody.Part? = null
        if (backImageFile != null) {
            fileBackImageBody = MultipartBody.Part.createFormData("file[1]", data[currentPosition].name + "_back", backImageFile)
        }

        getCompositeDisposable().add(appRepository.postDocument(this, hashMap, fileFrontImageBody, fileBackImageBody))
    }

}