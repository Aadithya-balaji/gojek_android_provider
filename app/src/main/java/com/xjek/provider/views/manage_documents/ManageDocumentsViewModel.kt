package com.xjek.provider.views.manage_documents

import com.xjek.base.base.BaseViewModel

class ManageDocumentsViewModel : BaseViewModel<ManageDocumentsNavigator>() {

    private lateinit var adapter: ManageDocumentsAdapter
//    private lateinit var services: List<ManageServicesModel>

//    fun setServices(services: List<ManageServicesModel>) {
//        this.services = services
//    }

//    fun getServices(): List<ManageServicesModel> {
//        return services
//    }

    fun setAdapter() {
        adapter = ManageDocumentsAdapter(this)
        adapter.notifyDataSetChanged()
    }

    fun getAdapter(): ManageDocumentsAdapter {
        return adapter
    }

//    fun getService(position: Int): ManageServicesModel {
//        return services[position]
//    }

    fun onItemClick(position: Int) {
        navigator.onMenuItemClicked(position)
    }
}