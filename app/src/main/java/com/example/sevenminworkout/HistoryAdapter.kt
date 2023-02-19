package com.example.sevenminworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sevenminworkout.databinding.ItemHistoryRowBinding

// ubacujemo param item kao listu <String> zato sto je samo jedan element u data classHistoryEntity(
//@PrimaryKey
//val date: String

class HistoryAdapter(private val items: ArrayList<String>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    inner class ViewHolder(binding: ItemHistoryRowBinding): RecyclerView.ViewHolder(binding.root) {
        val llHistoryItemMain = binding.llHistoryItemMain
        val tvItem = binding.tvItem
        val tvPosition = binding.tvPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // pribavljamo svaki pojedinacni item(unos) na odredjenoj poziciji
        val date: String = items.get(position)
        // stavljamo +1 zato sto brojanje pocinje od 0
        holder.tvPosition.text = (position + 1).toString()
        holder.tvItem.text = date

        // pravimo da svaki drugi red bude svijetlo sivi radi lakse preglednosti
        if (position % 2 == 0){
            holder.llHistoryItemMain.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context,
                R.color.colorLightGray))
        } else {
            holder.llHistoryItemMain.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}