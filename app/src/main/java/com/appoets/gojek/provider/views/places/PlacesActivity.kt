package com.appoets.gojek.provider.views.places

import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivitySearchPlaceBinding
import com.appoets.gojek.provider.model.ciity.PlaceResponseModel
import com.appoets.gojek.provider.views.adapters.PlacesAdapter
import com.appoets.gojek.traximodule.views.views.model.CountryResponseModel
import java.net.ResponseCache

class  PlacesActivity :BaseActivity<ActivitySearchPlaceBinding>(),SearchView.OnQueryTextListener{


    private  lateinit var  placeSearchTextView:SearchView
    private  lateinit var  ivBack:ImageView
    private  lateinit var  llPlaces: ListView
    private  lateinit var  placesAdapter:PlacesAdapter
    private  lateinit var  placeList:ArrayList<PlaceResponseModel>



    override fun getLayoutId(): Int {
        return  R.layout.activity_search_place;
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        ivBack=findViewById(R.id.iv_back)
        placeSearchTextView=findViewById(R.id.sv_place)
        llPlaces=findViewById(R.id.ll_places) as ListView
        placeList=ArrayList<PlaceResponseModel>()

           val placeone= PlaceResponseModel()
           placeone.countryName="India"
            placeList.add(placeone)

        val placetwo= PlaceResponseModel()
        placetwo.countryName="Kenya"
        placeList.add(placetwo)


        placesAdapter= PlacesAdapter(this,true,false,false,placeList)
        llPlaces.adapter=placesAdapter
        placeSearchTextView.setOnQueryTextListener(this)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
         placesAdapter.filter.filter(query.toString())
        return  true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return  false
    }

}
