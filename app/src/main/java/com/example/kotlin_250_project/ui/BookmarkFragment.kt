package com.example.kotlin_250_project.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_250_project.R
import com.example.kotlin_250_project.databinding.FragmentBookmarkBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private lateinit var questionAdapter: QuestionAdapter
    private var bookmarkedIds: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)

        binding.btnBackBookmarks.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, HomeFragment()) // Use your container ID here
                .addToBackStack(null)
                .commit()
        }


        fetchBookmarkedQuestions()

        return binding.root
    }

    private fun fetchBookmarkedQuestions() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("Users").document(userId).get()
            .addOnSuccessListener { document ->
                bookmarkedIds = (document.get("bookmarks") as? List<String>)?.toMutableList()
                    ?: mutableListOf()

                if (bookmarkedIds.isEmpty()) {
                    showEmptyView()
                    return@addOnSuccessListener
                }

                val allQuestions = loadQuestionsFromAssets()
                val bookmarkedQuestions = allQuestions.filter { it.id in bookmarkedIds }

                if (bookmarkedQuestions.isEmpty()) {
                    showEmptyView()
                } else {
                    showQuestions(bookmarkedQuestions, bookmarkedIds)
                }
            }
            .addOnFailureListener { showEmptyView() }
    }

    private fun loadQuestionsFromAssets(): List<Question> {
        val inputStream = requireContext().assets.open("questions.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Question>>() {}.type
        val questions: List<Question> = Gson().fromJson(reader, type)
        reader.close()
        return questions
    }

    private fun showEmptyView() {
        binding.emptyAchievementsLayout.visibility = View.VISIBLE
        binding.recyclerViewBookmarks.visibility = View.GONE
    }

    private fun showQuestions(questions: List<Question>, bookmarkedIds: List<String>) {
        binding.emptyAchievementsLayout.visibility = View.GONE
        binding.recyclerViewBookmarks.visibility = View.VISIBLE

        if (!::questionAdapter.isInitialized) {
            questionAdapter = QuestionAdapter(
                questions = questions.toMutableList(),
                bookmarkedIds = bookmarkedIds.toMutableList(),
                onBookmarkToggle = { question, isBookmarked ->
                    updateBookmark(question.id, isBookmarked)
                },
                showCorrectAnswer = true
            )
            binding.recyclerViewBookmarks.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerViewBookmarks.adapter = questionAdapter
        } else {
            questionAdapter.apply {
                this.questions.clear()
                this.questions.addAll(questions)
                this.bookmarkedIds.clear()
                this.bookmarkedIds.addAll(bookmarkedIds)
                notifyDataSetChanged()
            }
        }
    }

    private fun updateBookmark(questionId: String, add: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        val userRef = firestore.collection("Users").document(userId)

        val update = if (add) {
            mapOf("bookmarks" to FieldValue.arrayUnion(questionId))
        } else {
            mapOf("bookmarks" to FieldValue.arrayRemove(questionId))
        }

        userRef.update(update)
            .addOnSuccessListener {
                // After update, refresh bookmarks
                fetchBookmarkedQuestions()
            }
            .addOnFailureListener {
                // Optionally handle failure
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
