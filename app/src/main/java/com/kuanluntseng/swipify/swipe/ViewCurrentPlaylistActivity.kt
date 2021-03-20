package com.kuanluntseng.swipify.swipe

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuanluntseng.swipify.R
import com.kuanluntseng.swipify.data.SpotifyService
import com.kuanluntseng.swipify.data.playlist.Playlist
import com.kuanluntseng.swipify.song.SongActivity
import com.kuanluntseng.swipify.utils.Utils
import com.spotify.android.appremote.api.SpotifyAppRemote.isSpotifyInstalled
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ViewCurrentPlaylistActivity : AppCompatActivity(){
    val TAG = "VIEW_CURRENT_PLAYLIST"

    private var playlist: Playlist? = null
    private lateinit var accessToken: String
    private lateinit var playlistId: String
    private var playlistRV: RecyclerView? = null
    private var playlistViewAdapter: ViewCurrentPlaylistAdapter? = null
    private var toggleToast: Toast? = null
    companion object {
        const val EXTRA_ACCESS_TOKEN = "accessToken"
        const val EXTRA_PLAYLIST_ID = "playlistId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
//        if (intent != null) {
//            if (intent.hasExtra(SongActivity.EXTRA_ACCESS_TOKEN)) {
//                this.accessToken =
//                    intent.getSerializableExtra(EXTRA_ACCESS_TOKEN) as String
//            }
//            if (intent.hasExtra(EXTRA_PLAYLIST_ID)){
//                this.playlistId =
//                    intent.getSerializableExtra(EXTRA_PLAYLIST_ID) as String
//            }
//        }
        getPlaylist()
        setContentView(R.layout.activity_view_current_playlist)
        playlistRV = findViewById(R.id.rv_current_playlist)
        playlistRV?.layoutManager = LinearLayoutManager(this)
        playlistRV?.setHasFixedSize(true)
        playlistViewAdapter = ViewCurrentPlaylistAdapter()
        playlistRV?.adapter = playlistViewAdapter
    }

    /**
     * calls the API to get a playlist with this class's member playlistId
     */

    private fun getPlaylist() {
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.spotify_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val spotifyService = retrofit.create(SpotifyService::class.java)
        val res: Call<Playlist> =
            spotifyService.getPlaylistById(
                getString(R.string.token_type) + " " + Utils.token,
                Utils.currentPlaylistId
            )
        res.enqueue(object : Callback<Playlist> {
            override fun onResponse(call: Call<Playlist>, response: Response<Playlist>) {
                if (response.isSuccessful) {
                    val spotifyResult = response.body()
                    Log.d("PLAYLIST_LOG", "onResponse: " + spotifyResult)

                    spotifyResult?.run {
                        playlist = spotifyResult
                        playlistViewAdapter!!.updateData(playlist)
                        title = playlist?.name
                    }
                } else {
                    Log.d(TAG, "onResponse: The request was not successful")
                }
            }

            override fun onFailure(call: Call<Playlist>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_view_current_playlist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_share -> {
                sharePlaylist()
                true
            }
            R.id.action_open_spotify -> {
                viewPlaylistOnSpotify()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sharePlaylist() {
        val shareText: String = "Check out this playlist made on Swipify!\n" + playlist?.externalUrls?.spotify
        val sendIntent: Intent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
        sendIntent.setType("text/plain")

        val chooserIntent: Intent = Intent.createChooser(sendIntent, null)
        startActivity(chooserIntent)
    }

    private fun viewPlaylistOnSpotify() {
        toggleToast?.cancel()
        val isSpotifyInstalled = try {
            packageManager.getPackageInfo("com.spotify.music", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        if (isSpotifyInstalled) {
            val sendIntent = Intent(Intent.ACTION_VIEW)
            sendIntent.data = Uri.parse(playlist?.uri)
            sendIntent.putExtra(
                Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + this.packageName)
            )
            this.startActivity(sendIntent)
        } else {
            Log.d(TAG, "viewPlaylistOnSpotify: Spotify is not installed")
            toggleToast = Toast.makeText(
                this,
                "Spotify is not installed!",
                Toast.LENGTH_LONG
            )
            toggleToast?.show()
        }
    }
}
