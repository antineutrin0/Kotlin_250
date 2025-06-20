package com.example.kotlin_250_project.ui
import Note
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R
import com.example.kotlin_250_project.ui.NoteAdapter
import com.example.kotlin_250_project.ui.NoteDetailActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter
    private lateinit var notesList: MutableList<Note>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var emptyMessage: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_notes)

        recyclerView = findViewById(R.id.notesRecyclerView)
        emptyMessage = findViewById(R.id.emptyMessage)
        notesList = mutableListOf()

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadNotes()
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
                Toast.makeText(this, "Failed to load notes ${result.documents}", Toast.LENGTH_SHORT).show()
                print(result)
                for (document in result) {
                    val note = document.toObject(Note::class.java)
                    note.id = document.id // get Firestore document ID
                    notesList.add(note)
                }
                emptyMessage.visibility = if (notesList.isEmpty()) View.VISIBLE else View.GONE

                adapter = NoteAdapter(notesList) { note ->
                    val intent = Intent(this, NoteDetailActivity::class.java).apply {
                        putExtra("title", note.title)
                        putExtra("description", note.description)
                        putExtra("date", note.date)
                    }
                    startActivity(intent)
                }

                recyclerView.adapter = adapter
            }
            .addOnFailureListener {
                print("error")
                Toast.makeText(this, "Failed to load notes", Toast.LENGTH_SHORT).show()
            }
    }
}
