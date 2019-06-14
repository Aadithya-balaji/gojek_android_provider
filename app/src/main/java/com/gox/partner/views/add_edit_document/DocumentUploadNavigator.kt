package com.gox.partner.views.add_edit_document

interface DocumentUploadNavigator {
    fun onDateChanged()
    fun selectFrontImage()
    fun selectBackImage()
    fun submitDocument()
}