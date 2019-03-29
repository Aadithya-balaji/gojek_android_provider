package com.appoets.gojek.provider.views.countrypicker

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityCountryListBinding
import com.appoets.gojek.provider.model.CountryModel
import com.appoets.gojek.provider.utils.Country
import com.appoets.gojek.provider.views.adapters.CountryAdapter
import com.appoets.gojek.provider.views.adapters.PlacesAdapter
import com.appoets.gojek.taxiservice.views.views.countrypicker.CountrtCodeNavigator

class CountryCodeActivity : BaseActivity<ActivityCountryListBinding>(),
        SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, CountrtCodeNavigator {

    private lateinit var ivBack: ImageView
    private lateinit var llPlaces: ListView
    private lateinit var placesAdapter: PlacesAdapter
    private lateinit var placeList: ArrayList<CountryModel>
    private lateinit var svCountry: SearchView
    private lateinit var listCountry: List<CountryModel>
    private var countryAdapter: CountryAdapter? = null
    private var countryName: String? = ""
    private var countryCode: String? = ""
    private var countryFlag: Int? = -1
    private lateinit var activityCountryListBinding: ActivityCountryListBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_country_list
    }

    override fun initView(mViewDataBinding: ViewDataBinding) {
        this.activityCountryListBinding = mViewDataBinding as ActivityCountryListBinding
        val countryCodeViewModel = CountryCodeViewModel()
        countryCodeViewModel.navigator = this
        activityCountryListBinding.placesModel = countryCodeViewModel
        ivBack = findViewById(R.id.iv_back) as ImageView
        svCountry = findViewById(R.id.sv_country)
        llPlaces = findViewById(R.id.ll_country) as ListView


        listCountry = Country.getAllCountries()
        countryAdapter = CountryAdapter(this@CountryCodeActivity, listCountry)
        llPlaces.adapter = countryAdapter
        svCountry.setOnQueryTextListener(this)

        llPlaces.setOnItemClickListener(this)

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

    override fun closeActivity() {
        finish()
    }
}
