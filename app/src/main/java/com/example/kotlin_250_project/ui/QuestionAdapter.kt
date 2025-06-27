package com.example.kotlin_250_project.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R

class QuestionAdapter(
    val questions: MutableList<Question>,
    val bookmarkedIds: MutableList<String>,
    val onBookmarkToggle: (Question, Boolean) -> Unit,
    val showCorrectAnswer: Boolean = false,
    val isOptionsEnabled: Boolean = true
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    val selectedAnswers = mutableMapOf<String, String>()

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvQuestionNumber: TextView = itemView.findViewById(R.id.tv_question_number)
        private val tvQuestionText: TextView = itemView.findViewById(R.id.tv_question_text)
        private val rgAnswerOptions: RadioGroup = itemView.findViewById(R.id.rg_answer_options)
        private val rbOptionA: RadioButton = itemView.findViewById(R.id.rb_option_a)
        private val rbOptionB: RadioButton = itemView.findViewById(R.id.rb_option_b)
        private val rbOptionC: RadioButton = itemView.findViewById(R.id.rb_option_c)
        private val rbOptionD: RadioButton = itemView.findViewById(R.id.rb_option_d)
        private val bookmarkBtn: ImageButton = itemView.findViewById(R.id.btn_flag_question)

        fun bind(question: Question, position: Int) {
            tvQuestionNumber.text = "Q${position + 1}."
            tvQuestionText.text = question.question

            val options = question.options
            val radioButtons = listOf(rbOptionA, rbOptionB, rbOptionC, rbOptionD)

            rgAnswerOptions.setOnCheckedChangeListener(null)
            rgAnswerOptions.clearCheck()

            // Reset and bind options
            radioButtons.forEachIndexed { index, button ->
                if (index < options.size) {
                    val label = ('A' + index).toString()
                    button.text = "$label) ${options[index]}"
                    button.visibility = View.VISIBLE
                    button.isEnabled = isOptionsEnabled
                    button.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    button.visibility = View.GONE
                }
            }

            // Restore previous selection
            val selected = selectedAnswers[question.id]
            options.forEachIndexed { index, opt ->
                if (opt == selected) {
                    radioButtons[index].isChecked = true
                }
            }

            if (isOptionsEnabled) {
                rgAnswerOptions.setOnCheckedChangeListener { _, checkedId ->
                    val answer = when (checkedId) {
                        R.id.rb_option_a -> options.getOrNull(0)
                        R.id.rb_option_b -> options.getOrNull(1)
                        R.id.rb_option_c -> options.getOrNull(2)
                        R.id.rb_option_d -> options.getOrNull(3)
                        else -> null
                    }
                    answer?.let {
                        selectedAnswers[question.id] = it
                    }
                }
            }

            val isBookmarked = bookmarkedIds.contains(question.id)
            bookmarkBtn.setColorFilter(
                if (isBookmarked) Color.parseColor("#FF5722") else Color.GRAY
            )
            bookmarkBtn.setOnClickListener {
                val nowBookmarked = !isBookmarked
                if (nowBookmarked) bookmarkedIds.add(question.id) else bookmarkedIds.remove(question.id)
                onBookmarkToggle(question, nowBookmarked)
                bookmarkBtn.setColorFilter(
                    if (nowBookmarked) Color.parseColor("#FF5722") else Color.GRAY
                )
            }

            // Show correct answer only in review mode
            if (showCorrectAnswer) {
                highlightCorrectAnswer(question, radioButtons)
            }
        }

        private fun highlightCorrectAnswer(question: Question, radioButtons: List<RadioButton>) {
            val correctIndex = question.options.indexOf(question.correctAnswer)
            val selectedIndex = question.options.indexOf(selectedAnswers[question.id])

            radioButtons.forEachIndexed { index, button ->
                if (index == correctIndex) {
                    button.setBackgroundColor(Color.parseColor("#C8E6C9")) // Green
                }
                if (selectedIndex == index && selectedIndex != correctIndex) {
                    button.setBackgroundColor(Color.parseColor("#FFCDD2")) // Red
                }
            }

            // Also show a TextView with correct answer if needed (optional)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_card, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position], position)
    }

    override fun getItemCount(): Int = questions.size
}
