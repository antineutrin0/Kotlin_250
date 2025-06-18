package com.example.kotlin_250_project.ui

import ResultModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.databinding.ResultItemBinding

class ResultAdapter(private val resultList: List<ResultModel>) :
    RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(val binding: ResultItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = resultList[position]
        holder.binding.subjectName.text = "Subject : ${result.subject}"
        holder.binding.obtainedMarks.text = result.marks
        holder.binding.grade.text = result.grade
        holder.binding.resultStatus.text = result.result
    }

    override fun getItemCount(): Int = resultList.size
}
