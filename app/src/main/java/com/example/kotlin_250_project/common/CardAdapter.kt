package com.example.kotlin_250_project.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.databinding.CardAdapterBinding

class CardAdapter(private val cards: List<String>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(private val binding: CardAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cardTitle: String) {
            binding.textTitle.text = cardTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = CardAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.size
}
