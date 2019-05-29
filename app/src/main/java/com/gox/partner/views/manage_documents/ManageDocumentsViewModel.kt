package com.gox.partner.views.manage_documents

import com.gox.base.base.BaseViewModel
import com.gox.partner.repository.AppRepository

class ManageDocumentsViewModel : BaseViewModel<ManageDocumentsNavigator>() {

    private val appRepository = AppRepository.instance()

    fun showAllDocuments() = navigator.showAllDocuments()
    fun showTransportDocuments() = navigator.showTransportDocuments()
    fun showDelieveryDocuments() = navigator.showDelieveryDocuments()
    fun showServicesDocuments() = navigator.showServicesDocuments()

}