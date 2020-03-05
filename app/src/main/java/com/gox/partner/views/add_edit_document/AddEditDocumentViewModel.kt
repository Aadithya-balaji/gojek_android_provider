package com.gox.partner.views.add_edit_document

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.base.utils.Utils
import com.gox.partner.models.AddDocumentResponse
import com.gox.partner.models.ListDocumentResponse
import com.gox.partner.repository.AppRepository
import com.gox.partner.utils.Enums
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.sql.Timestamp

class AddEditDocumentViewModel : BaseViewModel<DocumentUploadNavigator>() {

    private val mRepository = AppRepository.instance()
    private lateinit var data: List<ListDocumentResponse.ResponseData>

    var showLoading = MutableLiveData<Boolean>()
    var documentResponse = MutableLiveData<ListDocumentResponse>()
    var addDocumentResponse = MutableLiveData<AddDocumentResponse>()
    var errorResponse = MutableLiveData<String>()
    var showEmpty: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { false }
    var currentPosition: Int = 0

    var documentFrontFileName = MutableLiveData<String>()
    var documentBackFileName = MutableLiveData<String>()
    var documentFrontName = MutableLiveData<String>()
    var documentBackName = MutableLiveData<String>()
    var documentFrontImageURL = MutableLiveData<String>()
    var documentBackImageURL = MutableLiveData<String>()
    var pageIndicator = MutableLiveData<String>()
    var documentFrontImageFile = MutableLiveData<File>()
    var documentBackImageFile = MutableLiveData<File>()
    var expiryDate = MutableLiveData<String>()

    var showBackSide = MutableLiveData<Boolean>()
    var showExpiry = MutableLiveData<Boolean>()
    var showFrontView = MutableLiveData<Boolean>().apply { false }
    var showBackView = MutableLiveData<Boolean>().apply { false }

    var isPDF = MutableLiveData<Boolean>()

    fun getDocumentList(documentType: String) {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.getDocumentList(object : ApiListener {
            override fun success(successData: Any) {
                documentResponse.value = successData as ListDocumentResponse
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, documentType))
    }

    fun setData(data: ListDocumentResponse.ResponseData) {
        val documentList =ArrayList<ListDocumentResponse.ResponseData>()
        documentList.add(data)
        this.data = documentList
        if (this.data.isNotEmpty()) {
            updateDetails()
            showEmpty.value = false
        } else showEmpty.value = true
    }

    fun getData() = this.data

    private fun updateDetails() {
        documentFrontName.value = data[currentPosition].name + " (Front)"
        documentBackName.value = data[currentPosition].name + " (Back)"
        showBackSide.value = data[currentPosition].is_backside != null && data[currentPosition].is_backside!! == "1"
        showExpiry.value = data[currentPosition].is_expire == 1
        if (data[currentPosition].provider_document != null) {
            showFrontView.value = true
            expiryDate.value = Utils.parseDateToYYYYMMdd(data[currentPosition].provider_document?.expires_at)
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
            documentFrontImageFile.value = null
            documentBackImageFile.value = null
        }
        documentFrontFileName.value = ""
        documentBackFileName.value = ""

        pageIndicator.value = "${currentPosition + 1}/${data.size}"
    }

    fun getFileType(): String {
        var fileType = Enums.IMAGE_TYPE
        if (data.isNotEmpty() && data[currentPosition].file_type.equals("pdf", true)) fileType = Enums.PDF_TYPE
        return fileType
    }

    fun selectFrontImage() = navigator.selectFrontImage()

    fun selectBackImage() = navigator.selectBackImage()

    fun onExpiryDateClick() = navigator.onDateChanged()

    fun submitDocument() = navigator.submitDocument()

    fun incrementPosition() {
        currentPosition += 1
        updateDetails()
    }

    fun updateDocument() {
        showLoading.value = true

        val hashMap: HashMap<String, RequestBody> = HashMap()
        if (!expiryDate.value.isNullOrEmpty()) hashMap["expires_at"] =
                RequestBody.create(MediaType.parse("text/plain"), expiryDate.value!!)
        hashMap["document_id"] = RequestBody.create(MediaType.parse("text/plain"), data[currentPosition].id.toString())

        var frontImageRequestBody: RequestBody? = null
        if (documentFrontImageFile.value != null) {
            frontImageRequestBody = RequestBody.create(
                    MediaType.parse("*/*"),
                    documentFrontImageFile.value!!)
        }

        var backImageFile: RequestBody? = null

        if (documentBackImageFile.value != null) {
            backImageFile = RequestBody.create(
                    MediaType.parse("*/*"),
                    documentBackImageFile.value!!)
        }

        var fileFrontImageBody: MultipartBody.Part? = null
        if (frontImageRequestBody != null) fileFrontImageBody = MultipartBody.Part.createFormData("file[0]",
                "${Timestamp(System.currentTimeMillis())}_front", frontImageRequestBody)

        var fileBackImageBody: MultipartBody.Part? = null
        if (backImageFile != null) fileBackImageBody = MultipartBody.Part.createFormData("file[1]",
                "${Timestamp(System.currentTimeMillis())}_back", backImageFile)

        getCompositeDisposable().add(mRepository.postDocument(object : ApiListener {
            override fun success(successData: Any) {
                addDocumentResponse.value = successData as AddDocumentResponse
                showLoading.postValue(false)
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
                showLoading.postValue(false)
            }
        }, hashMap, fileFrontImageBody, fileBackImageBody))
    }
}