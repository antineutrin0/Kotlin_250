package com.example.kotlin_250_project.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R

class QuestionAdapter(
    private val questions: List<Question>
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    // To store user selected answers
    val selectedAnswers = mutableMapOf<String, String>()  // questionId -> selectedOption

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestionNumber: TextView = itemView.findViewById(R.id.tv_question_number)
        val tvQuestionText: TextView = itemView.findViewById(R.id.tv_question_text)
        val rgAnswerOptions: RadioGroup = itemView.findViewById(R.id.rg_answer_options)

        private val rbOptionA: RadioButton = itemView.findViewById(R.id.rb_option_a)
        private val rbOptionB: RadioButton = itemView.findViewById(R.id.rb_option_b)
        private val rbOptionC: RadioButton = itemView.findViewById(R.id.rb_option_c)
        private val rbOptionD: RadioButton = itemView.findViewById(R.id.rb_option_d)

        fun bind(question: Question, position: Int) {
            tvQuestionNumber.text = "Q${position + 1}."
            tvQuestionText.text = question.question

            // Set options text (assuming exactly 4)
            val options = question.options
            rbOptionA.text = "A) ${options[0]}"
            rbOptionB.text = "B) ${options[1]}"
            rbOptionC.text = "C) ${options[2]}"
            rbOptionD.text = "D) ${options[3]}"

            // Clear previous selection to prevent recycling issues
            rgAnswerOptions.setOnCheckedChangeListener(null)
            rgAnswerOptions.clearCheck()

            // Restore selection if exists
            val selected = selectedAnswers[question.id]
            when (selected) {
                options[0] -> rbOptionA.isChecked = true
                options[1] -> rbOptionB.isChecked = true
                options[2] -> rbOptionC.isChecked = true
                options[3] -> rbOptionD.isChecked = true
            }

            // Listen to selection changes
            rgAnswerOptions.setOnCheckedChangeListener { _, checkedId ->
                val answer = when (checkedId) {
                    R.id.rb_option_a -> options[0]
                    R.id.rb_option_b -> options[1]
                    R.id.rb_option_c -> options[2]
                    R.id.rb_option_d -> options[3]
                    else -> null
                }
                if (answer != null) {
                    selectedAnswers[question.id] = answer
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_card, parent, false)
        return QuestionViewHolder(view)
    }

    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(questions[position], position)
    }
}
