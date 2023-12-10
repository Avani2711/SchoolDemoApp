package com.example.learning.ui
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.learning.R
import com.example.learning.Student
import com.example.learning.Teacher
import com.example.learning.User

class RecycleNewActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recyclenewactivity)
        val textView:TextView = findViewById(R.id.user_details)
        val user = intent.getSerializableExtra("user") as? User

        if (user is Teacher) {
            textView.text = "Name:${user.name}\nDob:${user.dob}\nIdno:${user.idno.toString()}"
        }

        if(user is Student){
        textView.text = "Name:${user.name}\nMatchTeacherName:${user.teacherNameMatch}"
        }
    }
}

