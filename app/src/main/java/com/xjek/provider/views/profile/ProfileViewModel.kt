package com.xjek.provider.views.profile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.CommonResponse
import com.xjek.provider.models.ProfileResponse
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.repository.AppRepository
import com.xjek.xjek.ui.profile.ProfileNavigator
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


class ProfileViewModel : BaseViewModel<ProfileNavigator>() {
    private lateinit var subscription: Disposable
    private val appRepository = AppRepository.instance()
    var profileResponse = MutableLiveData<ProfileResponse>()
    var errorResponse = MutableLiveData<Throwable>()
    var mFirstName = MutableLiveData<String>()
    var mLastName = MutableLiveData<String>()
    var mMobileNumber = MutableLiveData<String>()
    var mEmail = MutableLiveData<String>()
    var mCity = MutableLiveData<String>()
    var mCountry = MutableLiveData<String>()
    var mCountryCode = MutableLiveData<String>()
    var mProfileImage = MutableLiveData<String>()
    val mOpenGallerFlag = MutableLiveData<String>()
    var mGender = MutableLiveData<String>()
    val updateProfileResposne = MutableLiveData<CommonResponse>()
    val filePath = MutableLiveData<MultipartBody.Part>()

    companion object {
        @JvmStatic
        @BindingAdapter("android:profileImage")
        fun setImageViewResource(imageView: ImageView, imageUrl: String) {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .into(imageView);

        }
    }

    fun getProfileRespose() = profileResponse

    fun getUpdateProfileLiveData() = updateProfileResposne

    fun updateProfile() {
        val params = HashMap<String, RequestBody>()
        params.put(WebApiConstants.FIRST_NAME, RequestBody.create(MediaType.parse("text/plain"), mFirstName.value.toString()))
        params.put(WebApiConstants.LAST_NAME, RequestBody.create(MediaType.parse("text/plain"), mLastName.value.toString()))
        params.put(WebApiConstants.COUNTRY_CODE, RequestBody.create(MediaType.parse("text/plain"), mCountryCode.value.toString()))
        params.put(WebApiConstants.PHONE_NUMBER, RequestBody.create(MediaType.parse("text/plain"), mMobileNumber.value.toString()))
        params.put(WebApiConstants.GENDER, RequestBody.create(MediaType.parse("text/plain"), mGender.value.toString()))
        params.put(WebApiConstants.COUNTRY_ID, RequestBody.create(MediaType.parse("text/plain"), mCountry.value.toString()))
        params.put(WebApiConstants.CITY_ID, RequestBody.create(MediaType.parse("text/plain"), mCity.value.toString()))
        getCompositeDisposable().add(appRepository.updateProfile(this, params, filePath.value))
    }

    fun getProfile() {
        getCompositeDisposable().add(appRepository.getProfile(this, "Bearer" + " "
                + readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
    }

    fun getImage() {
        navigator.pickImage()
    }


}