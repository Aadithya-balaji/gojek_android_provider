package com.gox.partner.views.account

import com.gox.base.base.BaseViewModel
import com.gox.partner.models.AccountMenuModel

class AccountViewModel : BaseViewModel<AccountNavigator>() {

    private lateinit var adapter: AccountMenuAdapter
    private lateinit var accountMenus: List<AccountMenuModel>

    fun setAccountMenus(accountMenus: List<AccountMenuModel>) {
        this.accountMenus = accountMenus
    }

    fun getAccountMenus(): List<AccountMenuModel> {
        return accountMenus
    }

    fun getAccountMenu(position: Int): AccountMenuModel {
        return accountMenus[position]
    }

    fun setAdapter() {
        adapter = AccountMenuAdapter(this)
        adapter.notifyDataSetChanged()
    }

    fun getAdapter(): AccountMenuAdapter {
        return adapter
    }

    fun onItemClick(position: Int) {
        navigator.onMenuItemClicked(position)
    }
}
