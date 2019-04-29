package com.xjek.provider.views.manage_documents

interface ManageDocumentsNavigator {
    fun showAllDocuments()
    fun showTransportDocuments()
    fun showDelieveryDocuments()
    fun showServicesDocuments()
    fun showError(error: String)
}