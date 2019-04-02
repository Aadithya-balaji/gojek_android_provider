package com.xjek.provider.views.countrylist

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityCountryListBinding
import com.xjek.provider.views.adapters.CountryListAdapter
import com.xjek.user.data.repositary.remote.model.CountryListResponse

class CountryListActivity : BaseActivity<ActivityCountryListBinding>() {

    lateinit var mViewDataBinding: ActivityCountryListBinding
    override fun getLayoutId(): Int = R.layout.activity_country_list

    override fun initView(mViewDataBinding: ViewDataBinding?) {


        this.mViewDataBinding = mViewDataBinding as ActivityCountryListBinding
        this.mViewDataBinding.lifecycleOwner=this

        val countrylist = intent.getSerializableExtra("countrylistresponse") as CountryListResponse
        mViewDataBinding.countrylistadapter = CountryListAdapter(this, countrylist.responseData)



        mViewDataBinding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (s!!.length>2)
                    mViewDataBinding.countrylistadapter!!.filter.filter(s)
            }

        })
    }


}