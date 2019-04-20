package com.xjek.provider.views.manage_documents

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.DocumentTypeResponseModel
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository

class ManageDocumentsViewModel : BaseViewModel<ManageDocumentsNavigator>() {

    private val appRepository = AppRepository.instance()
    private val documentTypeLiveData = MutableLiveData<DocumentTypeResponseModel>()

    private val adapter = ManageDocumentsAdapter(this)

    fun setAdapter() {
        adapter.notifyDataSetChanged()
    }

    fun getAdapter() = adapter

    fun getDocumentType(position: Int): String {
        return documentTypeLiveData.value!!.responseData[position].name
    }

    fun getDocumentTypes() {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        val params = HashMap<String, String>()
        params[WebApiConstants.ListDocuments.TYPE] = "Transport"
        getCompositeDisposable().add(appRepository.getDocumentTypes(this, token, params))
    }

    fun getDocumentTypeObservable() = documentTypeLiveData

    fun onItemClick(position: Int) {
        navigator.onMenuItemClicked(position)
    }
}