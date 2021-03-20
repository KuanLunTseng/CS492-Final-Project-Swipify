package com.kuanluntseng.swipify.swipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kuanluntseng.swipify.data.recommendations.Recommendations

class SwipeViewModel() : ViewModel() {
    private var swipeRepository: SwipeRepository
    private var recResults: LiveData<Recommendations>
    private var loadingStatus: LiveData<LoadingStatus>

    init {
        swipeRepository = SwipeRepository()
        recResults = swipeRepository.getRecResults()
        loadingStatus = swipeRepository.getLoadingStatus()
    }

    fun loadGenreRecResults(genres: String) = swipeRepository.loadGenreRecResults(genres)
    fun loadSongRecResults(song: String) = swipeRepository.loadSongRecResults(song)
    fun getRecResults(): LiveData<Recommendations> = recResults
    fun getLoadStatus(): LiveData<LoadingStatus> = loadingStatus
}