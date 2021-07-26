package com.gox.taxiservice.locationpick

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.location.Location
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.gson.Gson
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants.DEFAULT_ZOOM
import com.gox.base.data.PlacesModel
import com.gox.base.utils.LocationCallBack
import com.gox.base.utils.LocationUtils
import com.gox.base.utils.PlacesAutocompleteUtil
import com.gox.base.utils.ViewUtils
import com.gox.taxiservice.R
import com.gox.taxiservice.databinding.ActivityLocationPickBinding
import com.gox.taxiservice.interfaces.CustomClickListener
import kotlinx.android.synthetic.main.activity_location_pick.*
import kotlinx.android.synthetic.main.toolbar_location_pick.*
import permissions.dispatcher.*

@RuntimePermissions
class LocationPickActivity : BaseActivity<ActivityLocationPickBinding>(),
        LocationPickNavigator,
        OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener {

    private var mLocationPickViewModel: LocationPickViewModel? = null
    private var mGoogleMap: GoogleMap? = null
    private var latLng = LatLng(0.0, 0.0)

    private lateinit var mActivityLocationPickBinding: ActivityLocationPickBinding
    private lateinit var mPlacesAutocompleteUtil: PlacesAutocompleteUtil
    private lateinit var prediction: ArrayList<PlacesModel>
    private lateinit var fragmentMap: SupportMapFragment
    private var address: String = ""

    private var countryCode: String = "PS"
    private var canShowLocationList = false
    private var mLocationPickFlag: Int? = null
    private var mLatitude: Double? = null
    private var mLongitude: Double? = null
    private var mAddress: String? = null
    var startLatLong:LatLng? = null

    override fun getLayoutId(): Int = R.layout.activity_location_pick

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mActivityLocationPickBinding = mViewDataBinding as ActivityLocationPickBinding
        mLocationPickViewModel = ViewModelProviders.of(this).get(LocationPickViewModel::class.java)
        mLocationPickViewModel?.navigator = this
        mPlacesAutocompleteUtil = PlacesAutocompleteUtil(applicationContext)
        mActivityLocationPickBinding.viewModel = mLocationPickViewModel
        mActivityLocationPickBinding.executePendingBindings()
        mLocationPickFlag = intent.getIntExtra("LocationPickFlag", 0)
        startLatLong = intent.getParcelableExtra("startlatlong")

        MapsInitializer.initialize(this@LocationPickActivity)
        initialiseMap()

        etLocationPick.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val textVal: String = p0.toString()
                if (textVal.isEmpty()) {
                    rvAutoCompletePlaces.visibility = View.GONE
                } else if (textVal.length > 3)
                    if (canShowLocationList) {
                        prediction = ArrayList()
                        mPlacesAutocompleteUtil.findAutocompletePredictions(textVal, countryCode,
                                object : PlacesAutocompleteUtil.PlaceSuggestion {
                                    override fun onPlaceReceived(mPlacesList: ArrayList<PlacesModel>?) {
                                        rvAutoCompletePlaces.visibility = View.VISIBLE
                                        prediction = mPlacesList!!
                                        println("RRR :: prediction = ${prediction.size}")
                                        val adapter = PlacesAdapter(prediction)
                                        mViewDataBinding.placemodel = adapter
                                        mViewDataBinding.placemodel!!.notifyDataSetChanged()
                                        adapter.notifyDataSetChanged()
                                        adapter.setOnClickListener(mOnAdapterClickListener)
                                    }
                                })
                    }
            }
        })

        btnDone.setOnClickListener {
            address = etLocationPick.text.toString()
            if (address.isEmpty()) ViewUtils.showToast(applicationContext, "Enter Valid Address", false)
            else{
                if (mLocationPickFlag == 0) {
                    setResult(Activity.RESULT_OK, Intent()
                            .putExtra("SelectedLocation", address)
                            .putExtra("SelectedLatLng", latLng))
                    finish()
                }
            }
//            else{
//                ViewUtils.showToast(applicationContext, "Maximum distance 100km", false)
//            }
        }


        ivBack.setOnClickListener { onBackPressed() }
        ivClear.setOnClickListener { etLocationPick.setText("") }

        mLocationPickViewModel = ViewModelProviders.of(this).get(LocationPickViewModel::class.java)

    }

    fun distFrom(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val locationA = Location("point A")

        locationA.latitude = lat1
        locationA.longitude = lng1

        val locationB = Location("point B")

        locationB.latitude = lat2
        locationB.longitude = lng2

        val distance = locationA.distanceTo(locationB)
        Log.d("TAG", "distFrom: $distance ---- $lat1 $lng1 ----- ${Gson().toJson(locationB)}")
        return distance.toDouble()
    }


    private val mOnAdapterClickListener = object : CustomClickListener {
        override fun onListClickListener(position: Int) {
            if (prediction.size > 0) try {
                val mPlace = prediction[position]
                val mLatLng = mPlacesAutocompleteUtil.getPlaceName(mPlace.mPlaceId)
                if (mLatLng.latitude != 0.0 && mLatLng.longitude != 0.0) {
                    canShowLocationList = false
                    mActivityLocationPickBinding.rvAutoCompletePlaces.visibility = View.GONE
                    etLocationPick.setText(prediction[position].mFullAddress)
                    latLng = mLatLng
                    updateMapLocation(LatLng(latLng.latitude, latLng.longitude))
                    hideKeyboard()
                }
                Handler().postDelayed({ canShowLocationList = true }, 2000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initialiseMap() {
        fragmentMap = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        fragmentMap.getMapAsync(this)
    }

    override fun onMapReady(mGoogleMap: GoogleMap?) {

        try {
            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

        this.mGoogleMap = mGoogleMap
        this.mGoogleMap?.setOnCameraIdleListener(this)

        updateLocationUIWithPermissionCheck()
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun updateLocationUI() {
        mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = false
        mGoogleMap!!.uiSettings.isCompassEnabled = false

        LocationUtils.getLastKnownLocation(this, object : LocationCallBack {
            override fun onSuccess(location: Location) {
                updateMapLocation(LatLng(location.latitude, location.longitude))
                countryCode = LocationUtils.getCountryCode(applicationContext, LatLng(location.latitude, location.longitude))
            }

            override fun onFailure(message: String) {
            }

        })
    }

    fun updateMapLocation(mLatLng: LatLng) {
        mGoogleMap?.moveCamera(CameraUpdateFactory
                .newLatLngZoom(mLatLng, DEFAULT_ZOOM))
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onLocationPermissionDenied() {
        ViewUtils.showToast(this, "R.string.location_permission_denied", false)
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onShowRationale(request: PermissionRequest) {
        ViewUtils.showRationaleAlert(this, R.string.default_notification_channel_id, request)
    }

    override fun onCameraIdle() {
        val address =
                LocationUtils.getCurrentAddress(applicationContext, mGoogleMap?.cameraPosition!!.target)
        if (address.size > 0) {
            etLocationPick.setText(address[0].getAddressLine(0))
            mAddress = address[0].getAddressLine(0).toString()
            latLng = mGoogleMap?.cameraPosition!!.target
            mLatitude = mGoogleMap?.cameraPosition!!.target.latitude
            mLongitude = mGoogleMap?.cameraPosition!!.target.longitude
        }
        mActivityLocationPickBinding.rvAutoCompletePlaces.visibility = View.GONE
        canShowLocationList = false

        Handler().postDelayed({ canShowLocationList = true }, 2000)
    }

    override fun onRequestPermissionsResult(code: Int, p: Array<out String>, results: IntArray) {
        super.onRequestPermissionsResult(code, p, results)
        onRequestPermissionsResult(code, results)
    }

    companion object {
        const val SOURCE_REQUEST_CODE = 101
        const val DESTINATION_REQUEST_CODE = 102
        const val EDIT_LOCATION_REQUEST_CODE = 103
    }
}