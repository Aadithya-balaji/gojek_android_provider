package com.xjek.provider.views.manage_documents

import com.xjek.base.base.BaseViewModel
import com.xjek.provider.repository.AppRepository

class ManageDocumentsViewModel : BaseViewModel<ManageDocumentsNavigator>() {

    private val appRepository = AppRepository.instance()

    fun showAllDocuments() = navigator.showAllDocuments()
    fun showTransportDocuments() = navigator.showTransportDocuments()
    fun showDelieveryDocuments() = navigator.showDelieveryDocuments()
    fun showServicesDocuments() = navigator.showServicesDocuments()

}