package com.kuanluntseng.swipify.swipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuanluntseng.swipify.R
import com.kuanluntseng.swipify.data.playlist.ItemsItem
import com.kuanluntseng.swipify.data.playlist.Playlist

class ViewCurrentPlaylistAdapter(): RecyclerView.Adapter<ViewCurrentPlaylistAdapter.ViewCurrentPlaylistItemViewHolder>() {
    private var playlist: Playlist? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewCurrentPlaylistAdapter.ViewCurrentPlaylistItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(
            R.layout.current_playlist_item,
            parent,
            false
        )
        return ViewCurrentPlaylistItemViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ViewCurrentPlaylistAdapter.ViewCurrentPlaylistItemViewHolder,
        position: Int
    ) {
        holder.bind(playlist?.tracks?.items?.get(position))
    }

    override fun getItemCount(): Int {
        return if (playlist == null || playlist?.tracks?.items == null) {
            0
        } else {
            playlist!!.tracks!!.items!!.size
        }
    }

    fun updateData(playlist: Playlist?) {
        this.playlist = playlist
        notifyDataSetChanged()
    }

    inner class ViewCurrentPlaylistItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var songNameTV: TextView? = null
        private var artistNameTV: TextView? = null
        private lateinit var songImage: ImageView

        fun bind(song: ItemsItem?){
            val ctx = itemView.context
            songNameTV?.setText(song?.track?.name)
            artistNameTV?.setText(song?.track?.artists?.get(0)?.name)
            if (song?.track?.album?.images?.get(0)?.url != null) {
                Glide.with(ctx)
                    .load(song?.track?.album?.images?.get(0)?.url)
                    .into(songImage)
            }
        }

        init {
            songNameTV = itemView.findViewById(R.id.song_name)
            artistNameTV = itemView.findViewById(R.id.song_artist_name)
            songImage = itemView.findViewById(R.id.song_image)
        }

    }
}