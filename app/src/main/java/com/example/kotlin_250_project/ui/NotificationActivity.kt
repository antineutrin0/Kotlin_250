package com.example.kotlin_250_project.ui

import NotificationModel
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_250_project.R
import com.example.kotlin_250_project.databinding.ActivityNotificationBinding

import kotlin.collections.filter

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notifications = getDummyNotifications()

        if (notifications.isEmpty()) {
            binding.recyclerNotifications.visibility = View.GONE
            binding.emptyState.visibility = View.VISIBLE
        } else {
            binding.recyclerNotifications.visibility = View.VISIBLE
            binding.emptyState.visibility = View.GONE

            adapter = NotificationAdapter(notifications)
            binding.recyclerNotifications.layoutManager = LinearLayoutManager(this)
            binding.recyclerNotifications.adapter = adapter
        }

        // Tab click functionality
        binding.tabAll.setOnClickListener {
            it.setBackgroundResource(R.drawable.tab_active_background)
            binding.tabUnread.setBackgroundResource(R.drawable.tab_inactive_background)
            // Reload all notifications
            adapter.updateList(notifications)
        }

        binding.tabUnread.setOnClickListener {
            it.setBackgroundResource(R.drawable.tab_active_background)
            binding.tabAll.setBackgroundResource(R.drawable.tab_inactive_background)
            // Filter unread only (example)
            val unread = notifications.filter { !it.isRead }
            adapter.updateList(unread)
        }
    }

    private fun getDummyNotifications(): List<NotificationModel> {
        return listOf(
            NotificationModel("New Feature Released", "Check out the new version 2.1", "2 min ago", false, "ℹ️"),
            NotificationModel("Reminder", "You have a pending task", "10 min ago", true, "🔔"),
            NotificationModel("Security Alert", "Login from new device", "1 hr ago", false, "⚠️"),
            NotificationModel("New Feature Released", "Check out the new version 2.1", "2 min ago", false, "ℹ️"),
            NotificationModel("Reminder", "You have a pending task", "10 min ago", true, "🔔"),
            NotificationModel("Security Alert", "Login from new device", "1 hr ago", false, "⚠️"),
            NotificationModel("New Feature Released", "Check out the new version 2.1", "2 min ago", false, "ℹ️"),
            NotificationModel("Reminder", "You have a pending task", "10 min ago", true, "🔔"),
            NotificationModel("Security Alert", "Login from new device", "1 hr ago", false, "⚠️"),
            NotificationModel("New Feature Released", "Check out the new version 2.1", "2 min ago", false, "ℹ️"),
            NotificationModel("Reminder", "You have a pending task", "10 min ago", true, "🔔"),
            NotificationModel("Security Alert", "Login from new device", "1 hr ago", false, "⚠️")



        )
    }
}
