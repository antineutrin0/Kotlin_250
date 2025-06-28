package com.example.kotlin_250_project.ui

import Note
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class NotesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var notesList: MutableList<Note>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var emptyMessage: TextView
    private lateinit var fabAddNote: FloatingActionButton
    private lateinit var btnback: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_notes)

        // Initialize views
        recyclerView = findViewById(R.id.notesRecyclerView)
        emptyMessage = findViewById(R.id.emptyMessage)
        fabAddNote = findViewById(R.id.fabAddNote)
        btnback=findViewById(R.id.btnBack)
        notesList = mutableListOf()
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddNote.setOnClickListener {
            val intent = Intent(this, CreateNoteActivity::class.java)
            startActivity(intent)
        }

        btnback.setOnClickListener {
            finish()
        }
        loadNotes()
    }

    override fun onResume() {
        super.onResume()
        loadNotes() // Reload notes when coming back from CreateNoteActivity
    }

    private fun loadNotes() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("Users")
            .document(userId)
            .collection("Notes")
            .get()
            .addOnSuccessListener { result ->
                notesList.clear()
                for (document in result) {
                    val note = document.toObject(Note::class.java)
                    note.id = document.id
                    notesList.add(note)
                }

                emptyMessage.visibility = if (notesList.isEmpty()) View.VISIBLE else View.GONE

                val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                adapter = NoteAdapter(notesList,
                    onItemClick = { note ->
                        val formattedDate = note.date?.toDate()?.let {
                            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(it)
                        } ?: "No Date"
                        val intent = Intent(this, NoteDetailActivity::class.java).apply {
                            putExtra("title", note.title)
                            putExtra("description", note.description)
                            putExtra("date", formattedDate)
                        }
                        startActivity(intent)
                    },
                    onEditNote = { note ->
                        val intent = Intent(this, CreateNoteActivity::class.java).apply {
                            putExtra("editMode", true)
                            putExtra("noteId", note.id)
                            putExtra("title", note.title)
                            putExtra("description", note.description)
                        }
                        startActivity(intent)
                    },
                    onDeleteNote = { note, position ->
                        val userId = auth.currentUser?.uid ?: return@NoteAdapter
                        firestore.collection("Users")
                            .document(userId)
                            .collection("Notes")
                            .document(note.id!!)
                            .delete()
                            .addOnSuccessListener {
                                notesList.removeAt(position)
                                adapter.notifyItemRemoved(position)
                                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to delete note", Toast.LENGTH_SHORT).show()
                            }
                    }
                )

                recyclerView.adapter = adapter

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load notes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
