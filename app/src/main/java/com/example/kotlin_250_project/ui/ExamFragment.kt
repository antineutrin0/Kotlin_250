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
        // Get selected subject from Spinner
        val selectedSubject = binding.spinnerSubject.selectedItem.toString()

        // Get selected marks from RadioGroup
        val selectedRadioId = binding.rgMarks.checkedRadioButtonId

        if (selectedRadioId == -1) {
            Toast.makeText(requireContext(), "Please select the marks.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedMarksRadio = requireView().findViewById<RadioButton>(selectedRadioId)
        val selectedMarks = selectedMarksRadio.text.toString().replace("[^0-9]".toRegex(), "").toInt()

        // Optional: validate Spinner selection (if your first item is like "Select a subject")
        if (selectedSubject == "Select a subject") {
            Toast.makeText(requireContext(), "Please select a subject.", Toast.LENGTH_SHORT).show()
            return
        }

        // Now you have both values
        Toast.makeText(requireContext(), "Subject: $selectedSubject, Marks: $selectedMarks", Toast.LENGTH_LONG).show()

        // TODO: Navigate to QuizFragment or pass data
            startActivity(Intent(requireContext(), QuizActivity::class.java))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
