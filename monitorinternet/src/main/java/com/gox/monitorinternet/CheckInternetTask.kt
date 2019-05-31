package com.gox.monitorinternet

import android.os.AsyncTask

import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

//      this async task tries to create a socket connection with google.com. If succeeds then return true otherwise false
internal class CheckInternetTask(callback: TaskFinished<Boolean>) : AsyncTask<Void, Void, Boolean>() {

    private val mCallbackWeakReference: WeakReference<TaskFinished<Boolean>> = WeakReference(callback)

    override fun doInBackground(vararg params: Void): Boolean? {
        try {
            //      parse url. if url is not parsed properly then return
            val url: URL
            try {
                url = URL("https://clients3.google.com/generate_204")
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return false
            }

            //      open connection. If fails return false
            val urlConnection: HttpURLConnection
            try {
                urlConnection = url.openConnection() as HttpURLConnection
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }

            urlConnection.setRequestProperty("User-Agent", "Android")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.connectTimeout = 1500
            urlConnection.readTimeout = 1500
            urlConnection.connect()
            return urlConnection.responseCode == 204 && urlConnection.contentLength == 0
        } catch (e: IOException) {
            return false
        }

    }

    override fun onPostExecute(isInternetAvailable: Boolean?) {
        val callback = mCallbackWeakReference.get()
        callback?.onTaskFinished(isInternetAvailable!!)
    }
}
