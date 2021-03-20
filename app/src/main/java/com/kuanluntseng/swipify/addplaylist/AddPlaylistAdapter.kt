package com.kuanluntseng.swipify.addplaylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuanluntseng.swipify.R
import com.kuanluntseng.swipify.data.CurrentUserPlaylists
import com.kuanluntseng.swipify.data.Items
import com.kuanluntseng.swipify.data.SpotifyService


class AddPlaylistAdapter(private val onPlaylistItemClickListener: OnPlaylistItemClickListener) :
    RecyclerView.Adapter<AddPlaylistAdapter.PlaylistItemViewHolder>() {
    private var currentUserPlaylists: CurrentUserPlaylists? = null
    private var playlistItemArr: List<Items?>? = null
    private val playlistId: String? = null

    interface OnPlaylistItemClickListener {
        fun onPlaylistItemClick(playlistItemArr: Items?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.current_user_playlists_item, parent, false)
        return PlaylistItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        holder.bind(currentUserPlaylists?.items?.get(position))
    }

    fun updatePlaylistData(currentUserPlaylists: CurrentUserPlaylists?) {
        this.currentUserPlaylists = currentUserPlaylists
        this.playlistItemArr = currentUserPlaylists?.items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (currentUserPlaylists == null || currentUserPlaylists?.items == null) {
            0
        } else {
            currentUserPlaylists!!.items!!.size
        }
    }

    inner class PlaylistItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var playlistNameTV: TextView? = null
        private lateinit var playlistImage: ImageView

        fun bind(playlistItem: Items?) {
            val ctx = itemView.context
            playlistNameTV?.setText(playlistItem?.name)

            if (playlistItem?.images!!.size > 0) {
                if (playlistItem?.images?.get(0)?.url != null) {
                    Glide.with(ctx)
                        .load(playlistItem?.images?.get(0)?.url)
                        .into(playlistImage)
                }
            } else {
                Glide.with(ctx)
                    .load(R.drawable.ic_spotify_icon_rgb_green)
                    .into(playlistImage)
            }


        }

        init {
            playlistNameTV = itemView.findViewById(R.id.playlist_name)
            playlistImage = itemView.findViewById(R.id.playlist_image)
            itemView.setOnClickListener {
                onPlaylistItemClickListener.onPlaylistItemClick(currentUserPlaylists!!.items[adapterPosition])
            }
        }

    }

}