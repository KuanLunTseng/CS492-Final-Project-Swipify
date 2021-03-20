package com.kuanluntseng.swipify.song

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.kuanluntseng.swipify.R



class SongAdapter (private val onSongItemClickListener: OnSongItemClickListener ) : RecyclerView.Adapter<SongAdapter.SongItemViewHolder>(){
    private var searchResult: SearchResult? = null
    private var playlistItemArr: List<ItemsItem?>? = null

    interface OnSongItemClickListener{
        fun onSongItemClick(playlistItemArr: ItemsItem?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.search_result_item, parent, false)
        return SongItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:SongItemViewHolder, position: Int) {
        holder.bind(searchResult?.tracks?.items?.get(position))
    }

    fun updateSongData(searchResult: SearchResult?){
        this.searchResult = searchResult
        this.playlistItemArr = searchResult?.tracks?.items
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return if (searchResult == null || searchResult?.tracks?.items == null){
            0
        } else{
            searchResult!!.tracks!!.items!!.size
        }
    }
    inner class SongItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var songNameTV: TextView? = null
        private var songImage: ImageView

        fun bind(playlistItem: ItemsItem?){
            val ctx = itemView.context
            songNameTV?.setText(playlistItem?.name)

            if (playlistItem?.album?.images?.get(0)?.url != null) {
                Glide.with(ctx)
                    .load(playlistItem.album.images.get(0)!!.url)
                    .into(songImage)
            }

        }

        init {
            songNameTV = itemView.findViewById(R.id.song_name)
            songImage = itemView.findViewById(R.id.song_image)
            itemView.setOnClickListener{
                onSongItemClickListener.onSongItemClick(searchResult!!.tracks!!.items!![adapterPosition])
            }
        }

    }

}