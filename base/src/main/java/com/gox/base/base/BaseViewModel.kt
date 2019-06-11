package com.gox.base.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gox.base.data.NetworkError
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException

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

    fun processException(e: Throwable) = when (e) {
        is HttpException -> getErrorMessage(e.response().errorBody()!!)
        is SocketTimeoutException -> NetworkError.TIME_OUT
        is IOException -> NetworkError.IO_EXCEPTION
        else -> NetworkError.SERVER_EXCEPTION
    }

    private fun getErrorMessage(responseBody: ResponseBody): String? {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message
        }
    }

    private fun getErrorMessage(responseBody: Any): String? {
        return try {
            val jsonObject = JSONObject(responseBody.toString())
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.message
        }
    }
}
