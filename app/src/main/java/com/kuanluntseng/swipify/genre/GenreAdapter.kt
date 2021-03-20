package com.kuanluntseng.swipify.genre

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kuanluntseng.swipify.R
import com.kuanluntseng.swipify.utils.Utils
import org.w3c.dom.Text

class GenreAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<GenreAdapter.GenreItemViewHolder>() {

    var genreItems: List<GenreItem> = Utils.genreSeeds

    inner class GenreItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewR: RelativeLayout = itemView.findViewById(R.id.genre_name)
        val view: TextView = viewR.getChildAt(1) as TextView

//        init {
//            itemView.setOnClickListener(this)
//        }

//        override fun onClick(v: View?) {
//            val position: Int = adapterPosition
//            if (position != RecyclerView.NO_POSITION) {
//                // Make sure the position is valid
//                clickListener.onItemClick(position)
//            }
//        }
    }

    interface OnItemClickListener {
        fun onClick(genreItem: GenreItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.genre_list_item, parent, false)
        val viewHolder = GenreItemViewHolder(view)
        view.setOnClickListener {
            onItemClickListener.onClick(genreItems[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: GenreItemViewHolder, position: Int) {
        val currentItem = genreItems[position]
        holder.view.text = currentItem.name
        when (currentItem.selected) {
            true -> {
                holder.viewR.setBackgroundColor(Color.LTGRAY)
                holder.view.setTextColor(Color.BLACK)
            }
            else -> {
                holder.viewR.setBackgroundColor(Color.DKGRAY)
                holder.view.setTextColor(Color.WHITE)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.genreItems.size
    }

}