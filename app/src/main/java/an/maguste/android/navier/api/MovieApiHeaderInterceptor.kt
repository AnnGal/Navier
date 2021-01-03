package an.maguste.android.navier.api

import an.maguste.android.navier.BuildConfig
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class MovieApiHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("RetrofitTry", "set header")
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val request = originalRequest.newBuilder()
            .url(originalHttpUrl)
            .addHeader(Companion.API_KEY_HEADER, BuildConfig.API_KEY) // just in case
            .build()

        Log.d("RetrofitTry", "set header request= ${request.header(Companion.API_KEY_HEADER)}")
        return chain.proceed(request)
    }

    companion object {
        private const val API_KEY_HEADER = "api_key"
    }
}