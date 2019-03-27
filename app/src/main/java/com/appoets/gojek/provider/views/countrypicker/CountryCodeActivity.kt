package com.appoets.gojek.provider.views.countrypicker

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.appoets.basemodule.base.BaseActivity
import com.appoets.basemodule.base.BaseBottomSheet
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityCountryListBinding
import com.appoets.gojek.provider.model.CountryModel
import com.appoets.gojek.provider.model.ciity.PlaceResponseModel
import com.appoets.gojek.provider.utils.Country
import com.appoets.gojek.provider.views.adapters.CountryAdapter
import com.appoets.gojek.provider.views.adapters.PlacesAdapter

class CountryCodeActivity : BaseActivity<ActivityCountryListBinding>(), SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {


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


    override fun getLayoutId(): Int {
        return R.layout.activity_country_list
    }


    override fun initView(mViewDataBinding: ViewDataBinding?) {
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
        if (countryModel != null) {
            countryName = countryModel.name
            countryCode = countryModel.dialCode
            countryFlag = countryModel.flag

        }
    }

}
