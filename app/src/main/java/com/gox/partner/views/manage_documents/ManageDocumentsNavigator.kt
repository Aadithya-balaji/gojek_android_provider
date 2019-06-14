package com.gox.partner.views.manage_documents

interface ManageDocumentsNavigator {
    fun showAllDocuments()
    fun showTransportDocuments()
    fun showDeliveryDocuments()
    fun showServicesDocuments()
    fun showError(error: String)
}