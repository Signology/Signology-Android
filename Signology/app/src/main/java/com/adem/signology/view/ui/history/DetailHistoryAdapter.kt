package com.adem.signology.view.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adem.signology.R
import com.adem.signology.data.remote.response.ImageHistoriesItem
import com.bumptech.glide.Glide

class DetailHistoryAdapter(private val context: Context, private val imageList: List<ImageHistoriesItem?>?) : RecyclerView.Adapter<DetailHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_detail_history)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_detail_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = imageList?.get(position)?.image
        Glide.with(context)
            .load(imageUrl)
            .centerInside()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList?.size ?: 0
    }
}
