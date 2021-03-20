package com.kuanluntseng.swipify.data

import com.kuanluntseng.swipify.data.devices.Devices
import com.kuanluntseng.swipify.data.genreseeds.GenreSeeds
import com.kuanluntseng.swipify.data.newplaylist.NewPlaylist
import com.kuanluntseng.swipify.data.playback.Playback
import com.kuanluntseng.swipify.data.playlist.Playlist
import com.kuanluntseng.swipify.data.recommendations.Recommendations
import com.kuanluntseng.swipify.data.userprofile.UserProfile
import com.kuanluntseng.swipify.song.SearchResult
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface SpotifyService {

    @GET("v1/me")
    fun getUserProfile(
        @Header("Authorization") accessToken: String
    ): Call<UserProfile>

    @GET("v1/me/playlists?limit=50&offset=0")
    fun getCurrentUserPlaylists(
        @Header("Authorization") accessToken: String
    ): Call<CurrentUserPlaylists>

    @GET("v1/recommendations/available-genre-seeds")
    fun getGenreSeeds(
        @Header("Authorization") accessToken: String
    ): Call<GenreSeeds>

    @GET("v1/recommendations?limit=10")
    fun getRecommendationsGenre(
        @Header("Authorization") accessToken: String,
        @Query("seed_genres") query: String
    ): Call<Recommendations>

    @GET("v1/recommendations?limit=10")
    fun getRecommendationsSong(
        @Header("Authorization") accessToken: String,
        @Query("seed_tracks") query: String
    ): Call<Recommendations>

    @GET("v1/playlists/{playlist_id}")
    fun getPlaylistById(
        @Header("Authorization") accessToken: String,
        @Path("playlist_id") playlistId: String
    ): Call<Playlist>

    @GET("v1/me/player/devices")
    fun getDeviceId(
        @Header("Authorization") accessToken: String
    ): Call<Devices>

    @POST("v1/me/player/queue")
    fun queueSong(
        @Header("Authorization") accessToken: String,
        @Query("device_id") deviceId: String,
        @Query("uri") uri: String
    ): Call<Any>

    @GET("v1/search?limit=50")
    fun getSearchResult(
        @Header("Authorization") accessToken: String,
        @Query("q") query: String,
        @Query("type") type: String
    ): Call<SearchResult>

    @POST("v1/users/{user_id}/playlists")
    fun createNewPlaylist(
        @Header("Authorization") accessToken: String,
        @Path("user_id") user_id: String,
        @Body requestBody: RequestBody
    ): Call<NewPlaylist>

    @GET("v1/me/player")
    fun getCurrentPlayback(
        @Header("Authorization") accessToken: String
    ): Call<Playback>

    @POST("v1/playlists/{playlist_id}/tracks")
    fun addItemsToPlaylist(
        @Header("Authorization") accessToken: String,
        @Path("playlist_id") playlistId: String,
        @Query("uris") uris: String
    ): Call<Any>
}