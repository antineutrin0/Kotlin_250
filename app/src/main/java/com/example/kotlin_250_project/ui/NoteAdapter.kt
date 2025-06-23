package com.example.kotlin_250_project.ui

import Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onItemClick: (Note) -> Unit,
    private val onEditNote: (Note) -> Unit,
    private val onDeleteNote: (Note, Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvNoteTitle)
        val date: TextView = itemView.findViewById(R.id.tvNoteDate)
        val options: ImageView = itemView.findViewById(R.id.ivMoreOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        holder.date.text = note.date?.toDate()?.let { sdf.format(it) } ?: "No Date"

        holder.itemView.setOnClickListener {
            onItemClick(note)
        }

        holder.options.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.note_item_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        onEditNote(note)
                        true
                    }
                    R.id.menu_delete -> {
                        onDeleteNote(note, position)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount() = notes.size
}
