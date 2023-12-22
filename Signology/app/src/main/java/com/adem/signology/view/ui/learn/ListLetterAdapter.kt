package com.adem.signology.view.ui.learn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.adem.signology.R
import com.adem.signology.data.model.letter.Letter

class ListLetterAdapter(private val listLetter: List<Letter>) : RecyclerView.Adapter<ListLetterAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLetter: TextView = itemView.findViewById(R.id.tv_item_letter)
        val cvLetter: CardView = itemView.findViewById(R.id.cvLetter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_letter, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listLetter.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (letter, photo) = listLetter[position]
        holder.tvLetter.text = letter

        holder.cvLetter.setOnClickListener {
            val context = holder.tvLetter.context
            val popup = PopupLetter(context, letter, photo)
            popup.show()
        }
    }

}