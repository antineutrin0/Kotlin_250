//package com.example.kotlin_250_project.adapters
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.kotlin_250_project.databinding.CardItemBinding
//import com.example.kotlin_250_project.models.CardModel
//
//class ViewPagerAdapter(private val cardList: List<CardModel>) :
//    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
//
//    inner class ViewHolder(val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val card = cardList[position]
//        holder.binding.cardTitle.text = card.title
//        holder.binding.cardDescription.text = card.description
//        holder.binding.cardButton.text = card.buttonText
//    }
//
//    override fun getItemCount(): Int = cardList.size
//}
