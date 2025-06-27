package com.example.kotlin_250_project.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kotlin_250_project.databinding.FragmentExamBinding

class ExamFragment : Fragment() {

    private var _binding: FragmentExamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExamBinding.inflate(inflater, container, false)

        binding.btnStartExam.setOnClickListener {
            handleStartExam()
        }

        return binding.root
    }

    private fun handleStartExam() {
        val selectedSubject = binding.spinnerSubject.selectedItem.toString()

        val selectedRadioId = binding.rgMarks.checkedRadioButtonId
        if (selectedRadioId == -1) {
            Toast.makeText(requireContext(), "Please select the marks.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedMarksRadio = binding.root.findViewById<RadioButton>(selectedRadioId)
        val selectedMarksText = selectedMarksRadio.text.toString()
        val selectedMarks = selectedMarksText.replace("[^0-9]".toRegex(), "").toIntOrNull()

        if (selectedMarks == null) {
            Toast.makeText(requireContext(), "Invalid marks selection.", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedSubject == "Select a subject") {
            Toast.makeText(requireContext(), "Please select a subject.", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(requireContext(), QuizActivity::class.java).apply {
            putExtra("subject", selectedSubject)
            putExtra("marks", selectedMarks)
        }

        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
