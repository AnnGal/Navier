package an.maguste.android.navier.api

import an.maguste.android.navier.data.*
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


private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_KEY_HEADER = "api_key"
private const val apiKey = "c589a9f6e9e9d8b8b1a3612f1b750053"

private class MovieApiHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("RetrofitTry", "set header")
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val request = originalRequest.newBuilder()
            .url(originalHttpUrl)
            .addHeader(API_KEY_HEADER, apiKey)
            .build()

        return chain.proceed(request)
    }
}


private val client = OkHttpClient().newBuilder()
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .addInterceptor(MovieApiHeaderInterceptor())
    .build()

private val json = Json {
    ignoreUnknownKeys = true
}

@Suppress("EXPERIMENTAL_API_USAGE")
private val retrofit: Retrofit = Retrofit.Builder()
    .client(client)
    .baseUrl(BASE_URL)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .build()


interface MovieApiService {
    @GET("genre/movie/list?api_key=c589a9f6e9e9d8b8b1a3612f1b750053")
    suspend fun getGenresList(): GenresJson
}

object MovieDbApiService {
    val retrofitService: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }
}

/*
  private class CatsApiHeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url

            val request = originalRequest.newBuilder()
                .url(originalHttpUrl)
                .addHeader(API_KEY_HEADER, apiKey)
                .build()

            return chain.proceed(request)
        }
    }

    private object RetrofitModule {
        private val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(CatsApiHeaderInterceptor())
            .build()

        private val json = Json {
            ignoreUnknownKeys = true
        }

        // TODO_ 01: Instantiate the OkHttpClient val by using ".newBuilder()"
        // TODO_ 02: Add "HttpLoggingInterceptor" to your Okhttp client
        // TODO_ 03: Add Logging Level - ".setLevel(HttpLoggingInterceptor.Level.BODY):

        @Suppress("EXPERIMENTAL_API_USAGE")
        private val retrofit: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        val catsApi: CatsApi = retrofit.create(CatsApi::class.java)
    }

* */

/*

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val HEADER = "http.agent: GameTrackerApp"
private const val API_KEY_HEADER = "c589a9f6e9e9d8b8b1a3612f1b750053"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface GamesApiService {
    @GET("games")
    suspend fun getGamesList(): ResponseJsonGamesList

    @Headers(HEADER)
    @GET("games")
    suspend fun getGamesHotListSorts(@Query("dates") datesRange: String, @Query("ordering") ordering: String): ResponseJsonGamesList
    // example: https://api.rawg.io/api/games?dates=2020-06-01,2020-09-15&ordering=-added

    @Headers(HEADER)
    @GET("games")
    suspend fun getGamesListSearch(@Query("page_size") pageSize: Int, @Query("search") gameName: String): ResponseJsonGamesList
    // example https://api.rawg.io/api/games?page_size=5&search=dishonored

    @Headers(HEADER)
    @GET("games/{currentGame}")
    suspend fun getSpecificGame(@Path("currentGame") gameAlias: String): GameDetail
    // example: https://api.rawg.io/api/games/cyberpunk-2077
}

object GamesApi {
    val retrofitService: GamesApiService by lazy {
        retrofit.create(GamesApiService::class.java)
    }
}
* */