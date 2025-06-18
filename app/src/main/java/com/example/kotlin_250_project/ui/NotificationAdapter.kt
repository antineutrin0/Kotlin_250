package com.example.kotlin_250_project.ui

import NotificationModel
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R

class NotificationAdapter(private var items: List<NotificationModel>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.notification_title)
        val messageText: TextView = itemView.findViewById(R.id.notification_message)
        val timeText: TextView = itemView.findViewById(R.id.notification_time)
        val readMoreBtn: TextView = itemView.findViewById(R.id.read_more_btn)
        val iconText: TextView = itemView.findViewById(R.id.notification_icon)
        val unreadBadge: View = itemView.findViewById(R.id.unread_badge)
        val leftBorder: View = itemView.findViewById(R.id.left_border)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = items[position]

        holder.titleText.text = item.title
        holder.messageText.text = item.message
        holder.timeText.text = item.time
        holder.iconText.text = item.icon

        if (item.isRead) {
            holder.unreadBadge.visibility = View.GONE
            holder.leftBorder.setBackgroundColor(holder.itemView.context.getColor(R.color.notification_read_border))
        } else {
            holder.unreadBadge.visibility = View.VISIBLE
            holder.leftBorder.setBackgroundColor(holder.itemView.context.getColor(R.color.notification_info))
        }

        holder.readMoreBtn.setOnClickListener {
            // Expand message logic or navigate
        }
    }

    fun updateList(newItems: List<NotificationModel>) {
        items = newItems
        notifyDataSetChanged()
    }
}
