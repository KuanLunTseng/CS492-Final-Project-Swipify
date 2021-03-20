package com.kuanluntseng.swipify.genre

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kuanluntseng.swipify.R
import com.kuanluntseng.swipify.song.NewPlaylistInfoDialogFragment
import com.kuanluntseng.swipify.song.SongActivity
import com.kuanluntseng.swipify.swipe.SwipeActivity
import com.kuanluntseng.swipify.utils.Utils

class GenreActivity : AppCompatActivity(), GenreAdapter.OnItemClickListener {
    lateinit var genreAdapter: GenreAdapter
    lateinit var genreRecyclerView: RecyclerView
    private var toggleToast: Toast? = null
    private var playlistId: String? = null

    companion object {
        val EXTRA_PLAYLIST_ID = "playlistId"
    }
//    val retrofit = Retrofit.Builder()
//        .baseUrl(getString(R.string.spotify_base_url))
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//    val spotifyService = retrofit.create(SpotifyService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)
        title = "Select a Genre"
        Utils.selectedGenres.clear()
        var genreItems: ArrayList<GenreItem> = Utils.genreSeeds

        this.genreAdapter = GenreAdapter(this)
        this.genreRecyclerView = findViewById(R.id.genre_rv)
        this.genreRecyclerView.adapter = genreAdapter
        this.genreRecyclerView.layoutManager = LinearLayoutManager(this)
        this.genreRecyclerView.setHasFixedSize(true)

        if (intent != null && intent.hasExtra(EXTRA_PLAYLIST_ID)) {
            this.playlistId =
                intent.getSerializableExtra(EXTRA_PLAYLIST_ID) as String
        }

        //functionality for floating action button to confirm genre selections
        val fab: View = findViewById(R.id.floating_action_button)
        fab.visibility = View.INVISIBLE
        fab.setOnClickListener {
            if (this.playlistId != null) {
                Utils.currentPlaylistId = this.playlistId!!
                val intent = Intent(this, SwipeActivity::class.java)
                startActivity(intent)
            } else {
                val newFragment = NewPlaylistInfoDialogFragment()
                newFragment.show(supportFragmentManager, "playlist_information")
            }
        }
    }

    override fun onClick(genreItem: GenreItem) {
//        var genreItems: List<GenreItem> = genreAdapter.genreItems
//        val selectedItem: GenreItem = genreAdapter.genreList[position]

//        if (::toggleToast.isInitialized) {
//            toggleToast.cancel()
//            toggleToast = Toast.makeText(this, "${genreItem.name} clicked", Toast.LENGTH_SHORT)
//            toggleToast.show()
//        } else {
//
//        }
        toggleToast?.cancel()
        when (Utils.selectedGenres.size < 5 || genreItem.selected) {
            true -> {
                genreItem.selected = !genreItem.selected
                when (genreItem.selected) {
                    true -> {
                        Utils.selectedGenres.add(genreItem.name)
                        findViewById<FloatingActionButton>(R.id.floating_action_button).visibility =
                            View.VISIBLE
                        toggleToast = Toast.makeText(
                            this,
                            "Genre ${genreItem.name} added!",
                            Toast.LENGTH_SHORT
                        )
                    }
                    false -> {
                        Utils.selectedGenres.remove(genreItem.name)
                        if (Utils.selectedGenres.isNullOrEmpty()) {
                            findViewById<FloatingActionButton>(R.id.floating_action_button).visibility =
                                View.INVISIBLE
                        }
                        toggleToast = Toast.makeText(
                            this,
                            "Genre ${genreItem.name} removed!",
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
            else -> {
                toggleToast =
                    Toast.makeText(this, "Only support up to 5 genre types", Toast.LENGTH_SHORT)
            }
        }
        toggleToast?.show()
//        for (item: GenreItem in genreItems) {
//            // Set the item's select property to true it is is the one selected
//                // If not set it to false
//            item.selected = item == selectedItem
//        }
        genreAdapter.notifyDataSetChanged()
    }

//    when (currentItem.selected) {
//        true->holder.view.setTextColor(Color.WHITE)
//        else-> holder.view.setTextColor(Color.BLACK)
//    }

    fun requestGenreList() {

    }

    override fun onStop() {
        super.onStop()
        for (item: GenreItem in this.genreAdapter.genreItems) {
            if (item.selected) {
                item.selected = false
            }
        }
    }
}