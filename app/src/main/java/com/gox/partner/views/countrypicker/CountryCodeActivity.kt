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
import com.gox.partner.views.adapters.CountryAdapter

class CountryCodeActivity : BaseActivity<ActivityCountryCodeBinding>(),
        SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, CountryCodeNavigator {

    private lateinit var mBinding: ActivityCountryCodeBinding
    private lateinit var ivBack: ImageView
    private lateinit var llPlaces: ListView
    private lateinit var svCountry: SearchView
    private lateinit var listCountry: List<CountryModel>

    private var countryAdapter: CountryAdapter? = null
    private var countryName: String? = ""
    private var countryCode: String? = ""
    private var countryFlag: Int? = -1

    override fun getLayoutId() = R.layout.activity_country_code

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mBinding = mViewDataBinding as ActivityCountryCodeBinding
        val mViewModel = CountryCodeViewModel()
        mViewModel.navigator = this
        mBinding.placesModel = mViewModel
        ivBack = findViewById(R.id.iv_back)
        svCountry = findViewById(R.id.sv_country)
        llPlaces = findViewById(R.id.ll_country)


        listCountry = com.gox.partner.utils.Country.getAllCountries()
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
        countryName = countryModel.name
        countryCode = countryModel.dialCode
        countryFlag = countryModel.flag

        val resultIntent = Intent()
        resultIntent.putExtra("countryName", countryName)
        resultIntent.putExtra("countryCode", countryCode)
        resultIntent.putExtra("countryFlag", countryFlag!!)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    override fun closeActivity() = finish()
}