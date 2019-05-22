package com.xjek.provider.views.countrypicker

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.gojek.taxiservice.views.views.countrypicker.CountrtCodeNavigator
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityCountryCodeBinding
import com.xjek.provider.models.CountryModel
import com.xjek.provider.views.adapters.CountryAdapter
import com.xjek.provider.views.adapters.PlacesAdapter

class CountryCodeActivity : BaseActivity<ActivityCountryCodeBinding>(),
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
    private lateinit var activityCountryListBinding: ActivityCountryCodeBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_country_code
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.activityCountryListBinding = mViewDataBinding as ActivityCountryCodeBinding
        val countryCodeViewModel = CountryCodeViewModel()
        countryCodeViewModel.navigator = this
        activityCountryListBinding.placesModel = countryCodeViewModel
        ivBack = findViewById(R.id.iv_back)
        svCountry = findViewById(R.id.sv_country)
        llPlaces = findViewById(R.id.ll_country)


        listCountry = com.xjek.provider.utils.Country.getAllCountries()
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

    override fun closeActivity() {
        finish()
    }
}