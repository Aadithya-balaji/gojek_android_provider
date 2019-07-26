package com.gox.partner.views.manage_bank_details

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.AddBankDetailsModel
import com.gox.partner.models.BankTemplateModel
import com.gox.partner.repository.AppRepository
import org.json.JSONArray
import org.json.JSONObject

class ManageBankDetailsViewModel : BaseViewModel<ManageBankDetailsNavigator>() {

    private val mRepository = AppRepository.instance()

    var showLoading = MutableLiveData<Boolean>()
    var bankResponse = MutableLiveData<BankTemplateModel>()
    var addEditBankResponse = MutableLiveData<AddBankDetailsModel>()
    var errorResponse = MutableLiveData<String>()
    var addEditBankErrorResponse = MutableLiveData<String>()
    var showEmpty: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { false }

    private val adapter: SetupBankTemplateAdapter = SetupBankTemplateAdapter(this)

    fun getBankTemplate() {
        showLoading.value = true
        getCompositeDisposable().add(mRepository.getBankTemplate(object : ApiListener {
            override fun success(successData: Any) {
                showLoading.postValue(false)
                bankResponse.value = successData as BankTemplateModel
            }

            override fun fail(failData: Throwable) {
                showLoading.postValue(false)
                errorResponse.value = getErrorMessage(failData)
            }
        }))
    }

    fun setAdapter() = adapter.notifyDataSetChanged()

    fun preSetValues() {
        bankResponse.value!!.responseData.forEach { data ->
            run {
                data.lableValue = data.bankdetails?.keyvalue ?: ""
            }
        }
    }

    fun getAdapter() = adapter

    fun getItemCount() = if (bankResponse.value != null)
        bankResponse.value!!.responseData.size else 0

    fun getBankLabelName(position: Int): String {
        val response = bankResponse.value!!.responseData
        return if (response.isNotEmpty())
            response[position].label
        else ""
    }

    fun getBankLabelValue(position: Int): String {
        val response = bankResponse.value!!.responseData[position].bankdetails
        return response?.keyvalue ?: ""
    }

    fun getInputTypeIsNumber(position: Int): Boolean {
        val response = bankResponse.value!!.responseData
        return if (response.isNotEmpty()) response[position].type == "INT"
        else false
    }

    fun afterValueChanged(value: CharSequence, position: Int) {
        bankResponse.value!!.responseData[position].lableValue = value.toString()
    }

    fun submitDetails() {
        if (navigator.validateDetails()) {
            showLoading.value = true
            val data = bankResponse.value!!.responseData
            val isEdit: Boolean = data[0].bankdetails != null
            val rootObject = JSONObject()
            val bankFormID = JSONArray()
            val keyValue = JSONArray()
            val id = JSONArray()
            for (i in 0 until data.size) {
                bankFormID.put(i, data[i].id)
                keyValue.put(i, data[i].lableValue)
                if (isEdit)
                    id.put(i, data[i].bankdetails!!.id)
            }

            rootObject.put("bankform_id", bankFormID)
            rootObject.put("keyvalue", keyValue)
            if (isEdit)
                rootObject.put("id", id)

            if (isEdit) getCompositeDisposable().add(mRepository.postEditBankDetails(object : ApiListener {
                override fun success(successData: Any) {
                    addEditBankResponse.value = successData as AddBankDetailsModel
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    showLoading.postValue(false)
                    errorResponse.value = getErrorMessage(failData)
                }
            }, rootObject.toString()))
            else getCompositeDisposable().add(mRepository.postAddBankDetails(object : ApiListener {
                override fun success(successData: Any) {
                    addEditBankResponse.value = successData as AddBankDetailsModel
                    showLoading.postValue(false)
                }

                override fun fail(failData: Throwable) {
                    showLoading.postValue(false)
                    errorResponse.value = getErrorMessage(failData)
                }
            }, rootObject.toString()))
        }
    }
}