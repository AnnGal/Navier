package an.maguste.android.navier.api

import an.maguste.android.navier.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/*object RetrofitService {
    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(MovieApiHeaderInterceptor())
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Suppress("EXPERIMENTAL_API_USAGE")
    internal val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val movieApi: MovieApi = retrofit.create(MovieApi::class.java)
}*/

object RetrofitHolder {

    @ExperimentalSerializationApi
    @Suppress("EXPERIMENTAL_API_USAGE")
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(createClient())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(createJson().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private fun createClient() = OkHttpClient().newBuilder()
        .addInterceptor(MovieApiHeaderInterceptor())
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private fun createJson() = Json {
        ignoreUnknownKeys = true
    }
}