package com.kuanluntseng.swipify.song

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.kuanluntseng.swipify.R
import com.kuanluntseng.swipify.data.SpotifyService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent
import android.database.CursorIndexOutOfBoundsException
import android.util.Log
import android.widget.AbsListView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import com.kuanluntseng.swipify.addplaylist.AddPlaylistAdapter
import com.kuanluntseng.swipify.data.CurrentUserPlaylists
import com.kuanluntseng.swipify.utils.Utils

class SongActivity : FragmentActivity(), SongAdapter.OnSongItemClickListener {
    companion object{
        val EXTRA_ACCESS_TOKEN = "accessToken"
        val EXTRA_USER_ID = "userId"
    }
    lateinit var searchBoxET: EditText;
    lateinit var searchButton: Button;
    lateinit var accessToken: String;
    private var songAdapter: SongAdapter? = null
    private var searchResultRV: RecyclerView? = null
    private lateinit var searchResult: SearchResult
    private var searchResultItems: List<ItemsItem?>? = null

    var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)

        searchResultRV = findViewById(R.id.rv_search_result)
        searchResultRV?.layoutManager = LinearLayoutManager(this)
        searchResultRV?.setHasFixedSize(true)
        songAdapter = SongAdapter(this)
        searchResultRV?.adapter = songAdapter


        var intent = getIntent()
        if (intent != null && intent.hasExtra(EXTRA_ACCESS_TOKEN)){
            this.accessToken = intent.getSerializableExtra(EXTRA_ACCESS_TOKEN) as String
        }

        if(intent != null && intent.hasExtra(EXTRA_USER_ID)){
            this.userId = intent.getSerializableExtra(EXTRA_USER_ID) as String
        }

        searchBoxET = findViewById(R.id.et_search_box)

        searchButton = findViewById(com.kuanluntseng.swipify.R.id.btn_search)
        searchButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View?) {
                //make api class based on input in search field
                getSearchResults()
                //currently just testing layout of dialog

            }

        })
    }

    fun getSearchResults() {
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.spotify_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val spotifyService = retrofit.create(SpotifyService::class.java)

        val results: Call<SearchResult> =
            spotifyService.getSearchResult(getString(R.string.token_type) + " " + accessToken, searchBoxET.text.toString(), "track")
        results.enqueue(object : Callback<SearchResult> {
            override fun onResponse(
                call: Call<SearchResult>,
                response: Response<SearchResult>
            ) {
                if (response.isSuccessful) {
                    val spotifyResult = response.body()
                    Log.d("SONG_LOG", "onResponse: " + spotifyResult)

                    spotifyResult?.run {
                        searchResult = spotifyResult
                        searchResultItems = spotifyResult.tracks?.items
                        songAdapter!!.updateSongData(searchResult)
                    }
                } else {
                    Log.d("SONG_LOG", "onResponse: The request was not successful")
                }

            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onSongItemClick(playlistItemArr: ItemsItem?) {
        //clear old seeds
        Utils.clearSeeds()
        //update one song seed value
        Utils.oneSongSeed = playlistItemArr!!.id!!
        val newFragment = NewPlaylistInfoDialogFragment()
        newFragment.show(supportFragmentManager,"playlist_information")
    }

}