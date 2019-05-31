package com.gox.partner.views.citylist

import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityCityListBinding
import com.gox.partner.models.City
import com.gox.partner.views.adapters.CityListAdapter

class CityListActivity : BaseActivity<ActivityCityListBinding>(), CityListNavigator, SearchView.OnQueryTextListener {


    override fun closeActivity() {
        ViewUtils.hideSoftInputWindow(this)
        finish()
    }

    lateinit var mViewDataBinding: ActivityCityListBinding
    override fun getLayoutId(): Int = R.layout.activity_city_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityCityListBinding
        val cityList = intent.getSerializableExtra("citylistresponse") as ArrayList<City>
        mViewDataBinding.cityListAdapter = CityListAdapter(this, cityList)
        val cityListViewModel = CityListViewModel()
        cityListViewModel.navigator = this

        mViewDataBinding.citylistmodel = cityListViewModel

        mViewDataBinding.svCity.setOnQueryTextListener(this)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        mViewDataBinding.cityListAdapter!!.filter.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mViewDataBinding.cityListAdapter!!.filter.filter(newText)
        return true
    }

}