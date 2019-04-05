package com.xjek.provider.views.citylist

import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityCityListBinding
import com.xjek.provider.views.adapters.CityListAdapter
import com.xjek.user.data.repositary.remote.model.City

class CityListActivity : BaseActivity<ActivityCityListBinding>(), CityListNavigator, SearchView.OnQueryTextListener {


    override fun closeActivity() {
        hideKeyboard()
        finish()
    }

    lateinit var mViewDataBinding: ActivityCityListBinding
    override fun getLayoutId(): Int = R.layout.activity_city_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityCityListBinding
        val cityList = intent.getSerializableExtra("citylistresponse") as List<City>
        mViewDataBinding.cityListAdapter = CityListAdapter(this, cityList)
        val cityListViewModel = CityListViewModel()
        cityListViewModel.navigator = this
        mViewDataBinding.citylistmodel = cityListViewModel
        mViewDataBinding.svCity.setOnQueryTextListener(this)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        mViewDataBinding.cityListAdapter!!.filter.filter(query)
        return  true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mViewDataBinding.cityListAdapter!!.filter.filter(newText)
        return true
    }

}