package com.gox.partner.views.manage_documents

import com.gox.base.base.BaseViewModel

class ManageDocumentsViewModel : BaseViewModel<ManageDocumentsNavigator>() {
    fun showAllDocuments() = navigator.showAllDocuments()
    fun showTransportDocuments() = navigator.showTransportDocuments()
    fun showDeliveryDocuments() = navigator.showDeliveryDocuments()
    fun showServicesDocuments() = navigator.showServicesDocuments()
}