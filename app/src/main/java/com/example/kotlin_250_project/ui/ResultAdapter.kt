import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_250_project.R
import java.text.SimpleDateFormat
import java.util.*

class ResultAdapter(
    private val results: List<ExamResult>
) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectName: TextView = itemView.findViewById(R.id.subjectName)
        val examDate: TextView = itemView.findViewById(R.id.examDate) // Add this in your XML!
        val obtainedMarks: TextView = itemView.findViewById(R.id.obtainedMarks)
        val grade: TextView = itemView.findViewById(R.id.grade)
        val resultStatus: TextView = itemView.findViewById(R.id.resultStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.result_item, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]

        holder.subjectName.text = "Subject: ${result.subject}"

        // Format timestamp to date string
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val dateString = if (result.timestamp != 0L) sdf.format(Date(result.timestamp)) else "Date N/A"
        holder.examDate.text = "Date: $dateString"

        holder.obtainedMarks.text = "${result.obtainedMarks} / ${result.totalMarks}"

        val percentage = if (result.totalMarks != 0) {
            (result.obtainedMarks.toDouble() / result.totalMarks) * 100
        } else 0.0

        val gradeText = when {
            percentage >= 90 -> "A+"
            percentage >= 80 -> "A"
            percentage >= 70 -> "B"
            percentage >= 60 -> "C"
            percentage >= 50 -> "D"
            else -> "F"
        }
        holder.grade.text = gradeText

        val statusText = if (percentage >= 50) "PASS" else "FAIL"
        holder.resultStatus.text = statusText
        holder.resultStatus.setTextColor(
            if (statusText == "PASS") 0xFF4CAF50.toInt() else 0xFFF44336.toInt()
        )
    }

    override fun getItemCount(): Int = results.size
}
