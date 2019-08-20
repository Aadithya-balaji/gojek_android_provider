package com.gox.partner.views.account

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.AccountMenuModel
import com.gox.partner.models.ResponseData
import com.gox.partner.repository.AppRepository

class AccountViewModel : BaseViewModel<AccountNavigator>() {

    private val mRepository = AppRepository.instance()

    private lateinit var adapter: AccountMenuAdapter
    private lateinit var accountMenus: List<AccountMenuModel>
    val successResponse = MutableLiveData<ResponseData>()
    var errorResponse = MutableLiveData<String>()

    fun setAccountMenus(accountMenus: List<AccountMenuModel>) {
        this.accountMenus = accountMenus
    }

    fun getAccountMenus() = accountMenus

    fun getAccountMenu(position: Int) = accountMenus[position]

    fun setAdapter() {
        adapter = AccountMenuAdapter(this)
        adapter.notifyDataSetChanged()
    }

    fun getAdapter() = adapter

    fun onItemClick(position: Int) = navigator.onMenuItemClicked(position)

    fun logoutApp() = getCompositeDisposable().add(mRepository.logoutApp(object : ApiListener {
        override fun success(successData: Any) {
            successResponse.value = successData as ResponseData
        }

        override fun fail(failData: Throwable) {
            errorResponse.value = getErrorMessage(failData)
        }
    }))

}