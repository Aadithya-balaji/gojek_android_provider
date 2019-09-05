package com.gox.partner.views.countrypicker

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.partner.R
import com.gox.partner.databinding.ActivityCountryCodeBinding
import com.gox.partner.models.CountryModel
import com.gox.partner.utils.Country
import com.gox.partner.views.adapters.CountryAdapter

class CountryCodeActivity : BaseActivity<ActivityCountryCodeBinding>(),
        SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, CountryCodeNavigator {

    private lateinit var mBinding: ActivityCountryCodeBinding
    private lateinit var ivBack: ImageView
    private lateinit var llPlaces: ListView
    private lateinit var svCountry: SearchView
    private lateinit var listCountry: List<CountryModel>

    private var countryAdapter: CountryAdapter? = null

    override fun getLayoutId() = R.layout.activity_country_code

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mBinding = mViewDataBinding as ActivityCountryCodeBinding
        val mViewModel = CountryCodeViewModel()
        mViewModel.navigator = this
        mBinding.placesModel = mViewModel
        ivBack = findViewById(R.id.iv_back)
        svCountry = findViewById(R.id.sv_country)
        llPlaces = this.findViewById(R.id.ll_country)


        listCountry = Country.getAllCountries()
        countryAdapter = CountryAdapter(this@CountryCodeActivity, listCountry)
        llPlaces.adapter = countryAdapter
        svCountry.setOnQueryTextListener(this)

        llPlaces.onItemClickListener = this

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        countryAdapter!!.filter.filter(query.toString())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        countryAdapter!!.filter.filter(newText.toString())
        return true
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val countryModel = parent!!.adapter.getItem(position) as CountryModel

        val resultIntent = Intent()
        resultIntent.putExtra("countryName", countryModel.name)
        resultIntent.putExtra("countryCode", countryModel.dialCode)
        resultIntent.putExtra("countryFlag", countryModel.flag)
        resultIntent.putExtra("countryIsoCode", countryModel.code)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun closeActivity() = finish()
}