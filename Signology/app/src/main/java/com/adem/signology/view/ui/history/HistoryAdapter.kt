package com.adem.signology.view.ui.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adem.signology.R
import com.adem.signology.data.remote.response.HistoryItem

class HistoryAdapter(private val context: Context, private val historyList: List<HistoryItem?>?) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = historyList?.get(position)

                    val intent = Intent(context, DetailHistoryActivity::class.java)
                    intent.putExtra("id", clickedItem?.id ?: -1)
                    intent.putExtra("word", clickedItem?.word ?: "")
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.itemView.findViewById<TextView>(R.id.tv_history_predict).text =
             historyList?.get(position)?.word ?: ""
    }

    override fun getItemCount(): Int {
        return historyList?.size ?: 0
    }
}
