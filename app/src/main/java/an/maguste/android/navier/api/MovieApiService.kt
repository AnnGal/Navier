package an.maguste.android.navier.api

import an.maguste.android.navier.BuildConfig
import an.maguste.android.navier.data.Movie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("genre/movie/list")
    suspend fun getGenres(@Query("api_key") key: String = BuildConfig.API_KEY): GenresDto

    @GET("movie/popular")
    suspend fun getMovies(@Query("api_key") key: String = BuildConfig.API_KEY,
                          @Query("page") page: Int = 1): MoviesDto
    // example: https://api.themoviedb.org/3/movie/popular?api_key=<<api_key>>&page=1

    @GET("movie/{movie_id}/credits")
    suspend fun getActors(@Path("movie_id") movieId: Int,
                         @Query("api_key") key: String = BuildConfig.API_KEY): ActorsListDto
    // example: https://api.themoviedb.org/3/movie/{movie_id}/credits?api_key=<<api_key>>&language=en-US
}

object MovieApiService {
    val retrofitService: MovieApi by lazy {
        retrofit.create(MovieApi::class.java)
    }
}
