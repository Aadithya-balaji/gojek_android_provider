package com.xjek.provider.views.citylist

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityCityListBinding
import com.xjek.provider.views.adapters.CityListAdapter
import com.xjek.user.data.repositary.remote.model.City

class CityListActivity : BaseActivity<ActivityCityListBinding>() {

    lateinit var mViewDataBinding: ActivityCityListBinding
    override fun getLayoutId(): Int = R.layout.activity_city_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {


        this.mViewDataBinding = mViewDataBinding as ActivityCityListBinding
        this.mViewDataBinding.inputSearch.hint = getString(R.string.search_city)

        val cityList = intent.getSerializableExtra("citylistresponse") as List<City>

        mViewDataBinding.cityListAdapter = CityListAdapter(this, cityList)



        mViewDataBinding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length>2)
                    mViewDataBinding.cityListAdapter!!.filter.filter(s)
            }

        })
    }

}