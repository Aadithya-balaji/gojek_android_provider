package com.gox.partner.utils

import androidx.lifecycle.MutableLiveData
import com.gox.partner.models.Language
import com.gox.partner.views.home.VerificationModel

object Constant {

    var languages: List<Language> = listOf()
    var verificationObservable = MutableLiveData<VerificationModel>()

}