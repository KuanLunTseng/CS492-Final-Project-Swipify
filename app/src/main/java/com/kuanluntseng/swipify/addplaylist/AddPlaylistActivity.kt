package com.kuanluntseng.swipify.addplaylist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuanluntseng.swipify.R
import com.kuanluntseng.swipify.data.CurrentUserPlaylists
import com.kuanluntseng.swipify.data.Items
import com.kuanluntseng.swipify.data.SpotifyService
import com.kuanluntseng.swipify.genre.GenreActivity
import com.kuanluntseng.swipify.song.SongActivity
import com.kuanluntseng.swipify.swipe.SwipeActivity
import com.kuanluntseng.swipify.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddPlaylistActivity: AppCompatActivity(), AddPlaylistAdapter.OnPlaylistItemClickListener {
    val TAG = "PLAYLIST_LOG"
    private var playlistAdapter: AddPlaylistAdapter? = null
    private var currentUserPlaylistsRV: RecyclerView? = null
    private lateinit var currentUserPlaylists: CurrentUserPlaylists
    private lateinit var currentUserPlaylistsItems: List<Items>
    companion object {
        val EXTRA_ACCESS_TOKEN = "accessToken"
    }

    lateinit var accessToken: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setTitle("Select a playlist")
        Log.d("PLAYLIST_VIEW", "onCreate: this function is called");
        var intent = getIntent()
        if (intent != null && intent.hasExtra(SongActivity.EXTRA_ACCESS_TOKEN)) {
            this.accessToken =
                intent.getSerializableExtra(SongActivity.EXTRA_ACCESS_TOKEN) as String
        }
        getCurrentUserPlaylists();
        setContentView(R.layout.activity_add_playlist)
        currentUserPlaylistsRV = findViewById(R.id.rv_current_user_playlist)
        currentUserPlaylistsRV?.layoutManager = LinearLayoutManager(this)
        currentUserPlaylistsRV?.setHasFixedSize(true)
        playlistAdapter = AddPlaylistAdapter(this)
        currentUserPlaylistsRV?.adapter = playlistAdapter

    }
    fun getCurrentUserPlaylists() {
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.spotify_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val spotifyService = retrofit.create(SpotifyService::class.java)

        val results: Call<CurrentUserPlaylists> =
            spotifyService.getCurrentUserPlaylists(getString(R.string.token_type) + " " + accessToken)
        results.enqueue(object : Callback<CurrentUserPlaylists> {
            override fun onResponse(
                call: Call<CurrentUserPlaylists>,
                response: Response<CurrentUserPlaylists>
            ) {
                if (response.isSuccessful) {
                    val spotifyResult = response.body()
                    Log.d("PLAYLIST_LOG", "onResponse: " + spotifyResult)

                    spotifyResult?.run {
                        for (i in 0..spotifyResult.items.size - 1) {
                            Log.d("PLAYLIST_LOG", "Item[$i]:" + spotifyResult.items[i].name)
                        }
                        currentUserPlaylists = spotifyResult
                        currentUserPlaylistsItems = spotifyResult.items
                        playlistAdapter!!.updatePlaylistData(currentUserPlaylists)
                    }
                } else {
                    Log.d(TAG, "onResponse: The request was not successful")
                }

            }

            override fun onFailure(call: Call<CurrentUserPlaylists>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onPlaylistItemClick(playlistItemArr: Items?) {
        val intent = Intent(this, GenreActivity::class.java)
        intent.putExtra(GenreActivity.Companion.EXTRA_PLAYLIST_ID, playlistItemArr?.id)
        Utils.currentPlaylistId = playlistItemArr?.id!!
        startActivity(intent)
    }
}