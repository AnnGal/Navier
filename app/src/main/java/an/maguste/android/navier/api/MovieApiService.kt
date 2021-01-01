package an.maguste.android.navier.api

import an.maguste.android.navier.BuildConfig
import an.maguste.android.navier.data.GenresJson
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.MoviesJson
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_KEY_HEADER = "api_key"

private class MovieApiHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("RetrofitTry", "set header")
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val request = originalRequest.newBuilder()
            .url(originalHttpUrl)
            .addHeader(API_KEY_HEADER, BuildConfig.API_KEY) // just in case
            .build()

        Log.d("RetrofitTry", "set header request= ${request.header(API_KEY_HEADER)}")
        return chain.proceed(request)
    }
}

private val client = OkHttpClient().newBuilder()
    .addInterceptor(MovieApiHeaderInterceptor())
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

private val json = Json {
    ignoreUnknownKeys = true
}

@Suppress("EXPERIMENTAL_API_USAGE")
private val retrofit: Retrofit = Retrofit.Builder()
    .client(client)
    .baseUrl(BuildConfig.BASE_URL)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()

interface MovieApiService {
    @GET("genre/movie/list")
    suspend fun getGenres(@Query(API_KEY_HEADER) key: String = BuildConfig.API_KEY): GenresJson

    @GET("movie/popular")
    suspend fun getMovies(@Query(API_KEY_HEADER) key: String = BuildConfig.API_KEY,
                          @Query("page") page: Int = 1): MoviesJson
    // example: https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&page=1

    @GET("movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") movieId: Int,
                         @Query(API_KEY_HEADER) key: String = BuildConfig.API_KEY): Movie
    // example: https://api.themoviedb.org/3/movie/{movie_id}?api_key=<<api_key>
}

object MovieDbApiService {
    val retrofitService: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }
}
