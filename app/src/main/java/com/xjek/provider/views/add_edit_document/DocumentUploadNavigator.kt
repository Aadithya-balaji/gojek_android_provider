package com.xjek.provider.views.add_edit_document

interface DocumentUploadNavigator {

    fun onDateChanged()
    fun selectFrontImage()
    fun selectBackImage()
    fun showFrontImage()
    fun showBackImage()
    fun submitDocument()
}