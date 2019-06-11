package com.gox.base.di

import android.preference.PreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.gox.base.BuildConfig
import com.gox.base.base.BaseApplication
import com.gox.base.data.PreferencesKey
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class WebServiceModule {

    companion object {
        const val CONNECTION_TIMEOUT = 60L
        const val READ_TIMEOUT = 60L
        const val WRITE_TIMEOUT = 60L
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .client(getHttpClient())
                .build()
    }

    private fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addNetworkInterceptor(getRequestHeader())
                .addInterceptor(getLoggingInterceptor())
                .addNetworkInterceptor(StethoInterceptor())
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()
    }

    private fun getLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private fun getRequestHeader(): Interceptor {
        return Interceptor {
            val original = it.request()
            val request = original.newBuilder()
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("Authorization",
                            "Bearer "+ PreferenceManager.getDefaultSharedPreferences(BaseApplication.getBaseApplicationContext).getString(PreferencesKey.ACCESS_TOKEN,""))
                    .method(original.method(), original.body())
                    .build()

            it.proceed(request)
        }
    }
}