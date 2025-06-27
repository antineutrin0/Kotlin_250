package com.example.kotlin_250_project.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.databinding.QuestionCardBinding

class WrongAnswersAdapter(
    private val questions: List<Question>,
    private val userAnswers: Map<String, String>,
    private val showCorrectAnswer: Boolean = true
) : RecyclerView.Adapter<WrongAnswersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: QuestionCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = QuestionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questions[position]
        val userSelected = userAnswers[question.id]
        val correctAnswer = question.correctAnswer

        val binding = holder.binding
        binding.tvQuestionNumber.text = "Q${position + 1}."
        binding.tvQuestionText.text = question.question

        val optionViews = listOf(
            binding.rbOptionA,
            binding.rbOptionB,
            binding.rbOptionC,
            binding.rbOptionD
        )

        binding.rgAnswerOptions.clearCheck()
        binding.tvCorrectAnswer.visibility = View.GONE // default hidden

        optionViews.forEachIndexed { index, radioButton ->
            if (index < question.options.size) {
                val optionCode = ('A' + index).toString()
                val optionText = question.options[index]
                radioButton.visibility = View.VISIBLE
                radioButton.text = "$optionCode) $optionText"
                radioButton.isEnabled = false
                radioButton.setBackgroundColor(Color.TRANSPARENT)

                // ✅ Highlight correct answer
                if (optionCode == correctAnswer) {
                    radioButton.setBackgroundColor(Color.parseColor("#C8E6C9")) // green
                    radioButton.isChecked = true
                }

                // ❌ Highlight user's wrong selection
                if (optionCode == userSelected && userSelected != correctAnswer) {
                    radioButton.setBackgroundColor(Color.parseColor("#FFCDD2")) // red
                }
            } else {
                radioButton.visibility = View.GONE
            }
        }

        // Show correct and selected answer in TextView
        if (showCorrectAnswer) {
            val correct = correctAnswer
            val selected = userSelected ?: "Not Answered"
            binding.tvCorrectAnswer.apply {
                text = "✅ Correct Answer: $correct |  Your Answer: $selected"
                visibility = View.VISIBLE
            }
        }
    }
}
