package com.example.kotlin_250_project.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_250_project.R
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etNotes: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageButton

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private var noteId: String? = null // Used in edit mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_note)

        etTitle = findViewById(R.id.etTitle)
        etNotes = findViewById(R.id.etNotes)
        btnSave = findViewById(R.id.btnSave)
        btnBack = findViewById(R.id.btnBack)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Check if we're editing an existing note
        noteId = intent.getStringExtra("noteId")
        val passedTitle = intent.getStringExtra("title")
        val passedDescription = intent.getStringExtra("description")

        if (noteId != null) {
            etTitle.setText(passedTitle)
            etNotes.setText(passedDescription)
        }

        btnSave.setOnClickListener {
            if (noteId != null) {
                updateNote()
            } else {
                createNote()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun updateNote() {
        val userId = auth.currentUser?.uid ?: return
        val updatedTitle = etTitle.text.toString().trim()
        val updatedDescription = etNotes.text.toString().trim()

        if (updatedTitle.isEmpty()) {
            etTitle.error = "Title required"
            return
        }

        if (updatedDescription.isEmpty()) {
            etNotes.error = "Note content required"
            return
        }

        val updatedData = mapOf(
            "title" to updatedTitle,
            "description" to updatedDescription,
            "date" to Timestamp.now()
        )

        firestore.collection("Users")
            .document(userId)
            .collection("Notes")
            .document(noteId!!)
            .update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update note: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createNote() {
        val title = etTitle.text.toString().trim()
        val description = etNotes.text.toString().trim()
        val userId = auth.currentUser?.uid ?: return

        if (title.isEmpty()) {
            etTitle.error = "Title required"
            return
        }

        if (description.isEmpty()) {
            etNotes.error = "Note content required"
            return
        }

        val note = hashMapOf(
            "title" to title,
            "description" to description,
            "date" to Timestamp.now()
        )

        firestore.collection("Users")
            .document(userId)
            .collection("Notes")
            .add(note)
            .addOnSuccessListener {
                Toast.makeText(this, "Note created", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save note: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
