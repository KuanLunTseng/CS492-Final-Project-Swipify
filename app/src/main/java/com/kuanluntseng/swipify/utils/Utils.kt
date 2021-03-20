package com.kuanluntseng.swipify.utils

import com.kuanluntseng.swipify.data.SpotifyService
import com.kuanluntseng.swipify.data.devices.Devices
import com.kuanluntseng.swipify.data.playback.Playback
import com.kuanluntseng.swipify.data.recommendations.Recommendations
import com.kuanluntseng.swipify.genre.GenreItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Utils {
    val CLIENT_ID = "2baa0fc179d94869b28342364bd5ef98"
    val REDIRECT_URI = "com.kuanluntseng.swipify://callback"

    lateinit var userId: String
    lateinit var currentPlaylistId: String
    lateinit var token: String
    lateinit var oneSongSeed: String
    lateinit var genreSeeds : ArrayList<GenreItem>
    lateinit var recommendations: Recommendations
    lateinit var deviceId: Devices
    lateinit var currentPlayback: Playback
    var likedSongs = mutableListOf<String>()
    var selectedGenres = ArrayList<String>()

    fun isAuthed() = token.isNotEmpty()

    fun isOneSongSeedInit() = ::oneSongSeed.isInitialized

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.spotify.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val spotifyService = retrofit.create(SpotifyService::class.java)

    fun clearSeeds() {
        this.oneSongSeed = ""
        this.selectedGenres = arrayListOf<String>()
    }
}