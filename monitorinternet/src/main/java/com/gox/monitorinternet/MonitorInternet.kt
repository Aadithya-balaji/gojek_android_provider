package com.gox.monitorinternet

import android.content.Context
import android.content.IntentFilter
import java.lang.ref.WeakReference
import java.util.*

class MonitorInternet private constructor(context: Context) : NetworkChangeReceiver.NetworkChangeListener {

    private val mContextWeakReference: WeakReference<Context>
    private val mInternetConnectivityListener: MutableList<WeakReference<InternetConnectivityListener>>?
    private var mCheckConnectivityCallback: TaskFinished<Boolean>? = null
    private var mNetworkChangeReceiver: NetworkChangeReceiver? = null

    private var mIsNetworkChangeRegistered = false
    var currentNetworkStatus = false
        private set
    //      this variable is to track if initial connectivity status has been calculated or not
    private var isInitialConnectivityStatusKnow = false

    init {
        val appContext = context.applicationContext
        mContextWeakReference = WeakReference(appContext)
        mInternetConnectivityListener = ArrayList()
    }

    /**
     * Add InternetConnectivityListener only if it's not added. It keeps a weak reference to the listener.
     * So user should have a strong reference to that listener otherwise that will be garbage collected
     */
    fun addInternetConnectivityListener(listener: InternetConnectivityListener?) {
        if (listener == null) return
        mInternetConnectivityListener!!.add(WeakReference(listener))
        if (mInternetConnectivityListener.size == 1) {
            registerNetworkChangeReceiver()
            isInitialConnectivityStatusKnow = false
            return
        }
        publishInternetAvailabilityStatus(currentNetworkStatus)
    }

    /**
     * remove the weak reference to the listener
     */
    fun removeInternetConnectivityChangeListener(internetConnectivityListener: InternetConnectivityListener?) {
        if (internetConnectivityListener == null) return
        if (mInternetConnectivityListener == null) return

        val iterator = mInternetConnectivityListener.iterator()
        while (iterator.hasNext()) {

            //      if weak reference is null then remove it from iterator
            val reference = iterator.next()
            if (reference == null) {
                iterator.remove()
                continue
            }

            //      if listener referenced by this weak reference is garbage collected then remove it from iterator
            val listener = reference.get()
            if (listener == null) {
                reference.clear()
                iterator.remove()
                continue
            }

            //      if listener to be removed is found then remove it
            if (listener === internetConnectivityListener) {
                reference.clear()
                iterator.remove()
                break
            }
        }

        //      if all listeners are removed then unregister NetworkChangeReceiver
        if (mInternetConnectivityListener.size == 0)
            unregisterNetworkChangeReceiver()
    }

    fun removeAllInternetConnectivityChangeListeners() {
        if (mInternetConnectivityListener == null) return

        val iterator = mInternetConnectivityListener.iterator()
        while (iterator.hasNext()) {
            val reference = iterator.next()
            reference.clear()
            iterator.remove()
        }
        unregisterNetworkChangeReceiver()
    }

    /**
     * registers a NetworkChangeReceiver if not registered already
     */
    private fun registerNetworkChangeReceiver() {
        val context = mContextWeakReference.get()
        if (context != null && !mIsNetworkChangeRegistered) {
            mNetworkChangeReceiver = NetworkChangeReceiver()
            mNetworkChangeReceiver!!.setNetworkChangeListener(this)
            context.registerReceiver(mNetworkChangeReceiver, IntentFilter(CONNECTIVITY_CHANGE_INTENT_ACTION))
            mIsNetworkChangeRegistered = true
        }
    }

    /**
     * unregisters the already registered NetworkChangeReceiver
     */
    private fun unregisterNetworkChangeReceiver() {
        val context = mContextWeakReference.get()
        if (context != null && mNetworkChangeReceiver != null && mIsNetworkChangeRegistered) {
            try {
                context.unregisterReceiver(mNetworkChangeReceiver)
            } catch (exception: IllegalArgumentException) {
                //consume this exception
            }

            mNetworkChangeReceiver!!.removeNetworkChangeListener()
        }
        mNetworkChangeReceiver = null
        mIsNetworkChangeRegistered = false
        mCheckConnectivityCallback = null
    }

    override fun onNetworkChange(isNetworkAvailable: Boolean) {
        if (isNetworkAvailable) {
            mCheckConnectivityCallback = object : TaskFinished<Boolean> {
                override fun onTaskFinished(data: Boolean) {
                    mCheckConnectivityCallback = null
                    if (!isInitialConnectivityStatusKnow || currentNetworkStatus != data) {
                        publishInternetAvailabilityStatus(data)
                        isInitialConnectivityStatusKnow = true
                    }
                }
            }
            CheckInternetTask(mCheckConnectivityCallback!!).execute()
        } else if (!isInitialConnectivityStatusKnow || currentNetworkStatus) {
            publishInternetAvailabilityStatus(false)
            isInitialConnectivityStatusKnow = true
        }
    }

    private fun publishInternetAvailabilityStatus(isInternetAvailable: Boolean) {
        currentNetworkStatus = isInternetAvailable
        if (mInternetConnectivityListener == null) return

        val iterator = mInternetConnectivityListener.iterator()

        while (iterator.hasNext()) {
            val reference = iterator.next()

            if (reference == null) {
                iterator.remove()
                continue
            }

            val listener = reference.get()
            if (listener == null) {
                iterator.remove()
                continue
            }

            listener.onInternetConnectivityChanged(isInternetAvailable)
        }

        if (mInternetConnectivityListener.size == 0)
            unregisterNetworkChangeReceiver()
    }

    companion object {

        private val LOCK = Any()
        private val CONNECTIVITY_CHANGE_INTENT_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
        @Volatile
        private var sInstance: MonitorInternet? = null

        /**
         * Call this function in application class to do initial setup. it returns singleton instance.
         * @param context need to register for Connectivity broadcast
         * @return instance of InternetConnectivityHelper
         */
        fun init(context: Context?): MonitorInternet? {
            if (context == null) throw NullPointerException("context can not be null")

            if (sInstance == null) synchronized(LOCK) {
                if (sInstance == null) sInstance = MonitorInternet(context)
            }
            return sInstance
        }

        val instance: MonitorInternet?
            get() {
                if (sInstance == null)
                    throw IllegalStateException("call init(Context) in your application class before calling getInstance()")
                return sInstance
            }
    }
}