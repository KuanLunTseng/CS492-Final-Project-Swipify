package com.kuanluntseng.swipify.swipe

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kuanluntseng.swipify.data.SpotifyService
import com.kuanluntseng.swipify.data.recommendations.Recommendations
import com.kuanluntseng.swipify.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SwipeRepository {
    val TAG = SwipeRepository::class.java.simpleName

    var currentGenres: String? = ""
    var currentSong: String? = ""

    var recResults: MutableLiveData<Recommendations> = MutableLiveData(null)
    var loadingStatus: MutableLiveData<LoadingStatus> = MutableLiveData(LoadingStatus.SUCCESS)
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val spotifyService = retrofit.create(SpotifyService::class.java)

    fun getRecResults(): LiveData<Recommendations> = recResults
    fun getLoadingStatus(): LiveData<LoadingStatus> = loadingStatus

    fun shouldExecuteGenreSearch(genres: String): Boolean {
        return !currentGenres.isNullOrEmpty()
                || !TextUtils.equals(genres, currentGenres)
                || loadingStatus.value === LoadingStatus.ERROR
    }

    fun shouldExecuteSongSearch(song: String): Boolean {
        return !currentSong.isNullOrEmpty()
                || !TextUtils.equals(song, currentSong)
                || loadingStatus.value === LoadingStatus.ERROR
    }

    fun loadGenreRecResults(genres: String) {
        if (shouldExecuteGenreSearch(genres)) {
            Log.d(TAG, "Running new search for recommendation...")
            currentGenres = genres
            executeSearchGenre(genres)
        } else {
            Log.d(TAG, "Cache!!")
        }
    }

    fun loadSongRecResults(song: String) {
        if (shouldExecuteGenreSearch(song)) {
            Log.d(TAG, "Running new search for recommendation...")
            currentSong = song
            executeSearchSong(song)
        } else {
            Log.d(TAG, "Cache!!")
        }
    }

    fun executeSearchSong(query: String) {
        recResults.value = null
        loadingStatus.value = LoadingStatus.LOADING

        val results: Call<Recommendations> =
            spotifyService.getRecommendationsSong("Bearer " + Utils.token, query)
        results.enqueue(object : Callback<Recommendations> {
            override fun onResponse(
                call: Call<Recommendations>,
                response: Response<Recommendations>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { recResults.value = it }
                    loadingStatus.value = LoadingStatus.SUCCESS
                } else {
                    loadingStatus.value = LoadingStatus.ERROR
                }
            }
            override fun onFailure(call: Call<Recommendations>, t: Throwable) {
                t.printStackTrace()
                loadingStatus.value = LoadingStatus.ERROR
            }
        })
    }

    fun executeSearchGenre(query: String) {
        recResults.value = null
        loadingStatus.value = LoadingStatus.LOADING

        val results: Call<Recommendations> =
            spotifyService.getRecommendationsGenre("Bearer " + Utils.token, query)
        results.enqueue(object : Callback<Recommendations> {
            override fun onResponse(
                call: Call<Recommendations>,
                response: Response<Recommendations>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let { recResults.value = it }
                    loadingStatus.value = LoadingStatus.SUCCESS
                } else {
                    loadingStatus.value = LoadingStatus.ERROR
                }
            }

            override fun onFailure(call: Call<Recommendations>, t: Throwable) {
                t.printStackTrace()
                loadingStatus.value = LoadingStatus.ERROR
            }
        })
    }
}