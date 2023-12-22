package com.adem.signology.view.ui.premium

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adem.signology.R
import com.adem.signology.data.model.premium.PremiumModel

class PremiumAdapter(private val premiumData: List<PremiumModel>) : RecyclerView.Adapter<PremiumAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cvContentPremium)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val contentRecyclerView: RecyclerView = itemView.findViewById(R.id.contentRecyclerView)
        val buttonBuy: Button = itemView.findViewById(R.id.btnBuy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_premium, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val premiumItem = premiumData[position]

        holder.titleTextView.text = premiumItem.title
        holder.priceTextView.text = premiumItem.price

        val contentAdapter = ContentAdapter(premiumItem.content)
        holder.contentRecyclerView.adapter = contentAdapter
        holder.contentRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)

        val cardColor = determineCardColor(holder, position)
        holder.cardView.setCardBackgroundColor(cardColor)

        holder.buttonBuy.setOnClickListener {
            onButtonBuyClickListener?.onButtonBuyClick(premiumItem.title)
        }
    }

    interface OnButtonBuyClickListener {
        fun onButtonBuyClick(title: String)
    }

    private var onButtonBuyClickListener: OnButtonBuyClickListener? = null

    fun setOnButtonBuyClickListener(listener: OnButtonBuyClickListener) {
        onButtonBuyClickListener = listener
    }

    override fun getItemCount(): Int {
        return premiumData.size
    }

    private fun determineCardColor(holder: ViewHolder, position: Int): Int {
        return if (position == 1) {
            ContextCompat.getColor(holder.itemView.context, R.color.blue2)
        } else if (position == 2) {
            ContextCompat.getColor(holder.itemView.context, R.color.red)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.purple)
        }
    }
}
