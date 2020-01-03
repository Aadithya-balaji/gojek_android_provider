package com.gox.base.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonSyntaxException
import com.gox.base.BuildConfig
import com.gox.base.data.NetworkError
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.session.SessionManager
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel<N> : ViewModel() {

    private var compositeDisposable = CompositeDisposable()
    private lateinit var mNavigator: WeakReference<N>
    private var liveErrorResponse = MutableLiveData<String>()

    var navigator: N
        get() = mNavigator.get()!!
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    fun getCompositeDisposable() = compositeDisposable

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getErrorObservable() = liveErrorResponse

    fun getErrorMessage(e: Throwable): String {
        return when (e) {
            is JsonSyntaxException -> if (BuildConfig.DEBUG) e.message.toString()
            else NetworkError.DATA_EXCEPTION
            is HttpException -> {
                if (e.code() == 401 && !PreferencesHelper
                                .get(PreferencesKey.ACCESS_TOKEN, "")
                                .equals(""))
                    SessionManager.clearSession()
                getError(e.response().errorBody()!!)
            }
            is SocketTimeoutException -> NetworkError.TIME_OUT
            is IOException -> NetworkError.IO_EXCEPTION
            is UnknownHostException ->NetworkError.UNKNOWN_HOST_EXCEPTION
            is ConnectException ->NetworkError.UNKNOWN_HOST_EXCEPTION
            else -> NetworkError.SERVER_EXCEPTION
        }
    }

    private fun getError(responseBody: ResponseBody): String {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message!!
        }
    }
}
