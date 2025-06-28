package com.example.kotlin_250_project.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R
import java.text.SimpleDateFormat
import java.util.*

class AchievementAdapter(private val list: MutableList<Achievement>) :
    RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder>() {

    class AchievementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.achievementDescription)
        val date: TextView = itemView.findViewById(R.id.achievementDate)
        val badge: TextView = itemView.findViewById(R.id.achievementBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achievement, parent, false)
        return AchievementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val item = list[position]
        holder.description.text = item.message
        holder.date.text = formatDate(item.timestamp)
        holder.badge.text = "🏆"
    }

    override fun getItemCount(): Int = list.size

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
