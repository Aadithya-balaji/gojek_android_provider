package com.xjek.provider.models

class DocumentUploadDataModel {
    var documentId: Int = -1
    lateinit var frontPageTitle: String
    var isBackPageRequired: Boolean = false
    var backPageTitle: String? = null
    var frontPageUrl: String? = null
    var backPageUrl: String? = null
    var isExpiryDateRequired: Int = -1
}