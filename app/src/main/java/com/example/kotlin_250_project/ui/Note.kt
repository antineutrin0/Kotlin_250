import com.google.firebase.Timestamp

data class Note(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var date: Timestamp? = null
)
