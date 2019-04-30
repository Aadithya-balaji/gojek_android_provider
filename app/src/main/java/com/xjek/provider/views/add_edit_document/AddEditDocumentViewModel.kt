package com.xjek.provider.views.add_edit_document

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.AddDocumentResponse
import com.xjek.provider.models.ListDocumentResponse
import com.xjek.provider.repository.AppRepository


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
    var documentFrontImageURL = MutableLiveData<String>()
    var documentBackName = MutableLiveData<String>()
    var documentBackImageURL = MutableLiveData<String>()
    var expiryDate = MutableLiveData<String>()

    var showBack = MutableLiveData<Boolean>()
    var showExpiry = MutableLiveData<Boolean>()

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
        showBack.value = data[currentPosition].is_backside!=null && data[currentPosition].is_backside!! == "1"
        showExpiry.value = data[currentPosition].is_expire == "1"
        if (data[currentPosition].provider_document != null) {
            expiryDate.value = data[currentPosition].provider_document?.expires_at
            documentFrontImageURL.value = data[currentPosition].provider_document!!.url[0].url
            if (data[currentPosition].provider_document!!.url.size > 1)
                documentBackImageURL.value = data[currentPosition].provider_document!!.url[1].url
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

    fun submitDocument(){
        navigator.submitDocument()
    }

    @BindingAdapter("bind:imageUrl")
    fun loadImage(view: ImageView, imageUrl: String) {
        Glide.with(view.context).load(imageUrl)
                .into(view)
    }
}