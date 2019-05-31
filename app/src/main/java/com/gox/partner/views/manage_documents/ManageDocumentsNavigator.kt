package com.gox.partner.views.manage_documents

interface ManageDocumentsNavigator {
    fun showAllDocuments()
    fun showTransportDocuments()
    fun showDelieveryDocuments()
    fun showServicesDocuments()
    fun showError(error: String)
}