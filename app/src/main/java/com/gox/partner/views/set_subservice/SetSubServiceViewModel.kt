package com.gox.partner.views.set_subservice

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.SubServiceCategoriesResponse
import com.gox.partner.repository.AppRepository

class SetSubServiceViewModel : BaseViewModel<SetSubServiceNavigator>() {

    private val mRepository = AppRepository.instance()
    val subServiceCategoriesResponse = MutableLiveData<SubServiceCategoriesResponse>()
    val errorResponse = MutableLiveData<String>()

    fun getSubCategories(id: String) {
        val service = HashMap<String, String>()
        service["service_category_id"] = id
        getCompositeDisposable().add(mRepository.getSubServiceCategories(object : ApiListener {
            override fun success(successData: Any) {
                subServiceCategoriesResponse.value = successData as SubServiceCategoriesResponse
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
            }
        }, service))
    }
}
