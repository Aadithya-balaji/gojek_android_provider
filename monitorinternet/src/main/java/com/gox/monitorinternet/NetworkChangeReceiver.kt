package com.gox.monitorinternet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

import java.lang.ref.WeakReference

internal class NetworkChangeReceiver : BroadcastReceiver() {

    private var mNetworkChangeListenerWeakReference: WeakReference<NetworkChangeListener>? = null

    override fun onReceive(context: Context, intent: Intent) {
        val networkChangeListener = mNetworkChangeListenerWeakReference!!.get()
        networkChangeListener?.onNetworkChange(isNetworkConnected(context))
    }

    fun setNetworkChangeListener(networkChangeListener: NetworkChangeListener) {
        mNetworkChangeListenerWeakReference = WeakReference(networkChangeListener)
    }

    fun removeNetworkChangeListener() {
        if (mNetworkChangeListenerWeakReference != null)
            mNetworkChangeListenerWeakReference!!.clear()
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo

        //      should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isAvailable && netInfo.isConnected
    }

    //      Interface to send opt to listeners
    internal interface NetworkChangeListener {
        fun onNetworkChange(isNetworkAvailable: Boolean)
    }
}
