package com.xjek.provider.views.countrylist

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityCountryListBinding
import com.xjek.provider.views.adapters.CountryListAdapter
import com.xjek.user.data.repositary.remote.model.CountryListResponse

class CountryListActivity : BaseActivity<ActivityCountryListBinding>(), SearchView.OnQueryTextListener,CountryNavigator {


    lateinit var mViewDataBinding: ActivityCountryListBinding
    override fun getLayoutId(): Int = R.layout.activity_country_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityCountryListBinding
        this.mViewDataBinding.lifecycleOwner=this
        val countrylist = intent.getSerializableExtra("countrylistresponse") as CountryListResponse
        mViewDataBinding.countrylistadapter = CountryListAdapter(this, countrylist.responseData)
        val countryViewModel=CountryViewModel()
        countryViewModel.navigator=this
        mViewDataBinding.countrylistmodel=countryViewModel
        mViewDataBinding.svCountry.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        mViewDataBinding.countrylistadapter!!.filter.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        mViewDataBinding.countrylistadapter!!.filter.filter(newText)
        return true
    }

    override fun closeActivity() {
        ViewUtils.hideSoftInputWindow(this)
        finish()
    }

}