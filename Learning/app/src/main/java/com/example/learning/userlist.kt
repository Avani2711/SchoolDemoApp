package com.example.learning

 import android.annotation.SuppressLint
 import android.os.Bundle
 import android.widget.TextView
 import androidx.appcompat.app.AppCompatActivity

class userlist : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userlist)
        val textView: TextView = findViewById(R.id.user_list)

        val userInfoList = mutableListOf<String>()

        studentteacherobject.obj.myList.forEach { user ->
            if (user is Teacher) {
                val matchStudentUiList =
                    studentteacherobject.obj.myList.filterIsInstance<Student>()
                        .filter { user.idno == it.teacherIdMatch }
                val maplist = matchStudentUiList.map { it.name }
                val studentjoin = maplist.joinToString(separator = "\n")
                val teacherInfo = "${user.name}, ${user.idno}, ${user.dob}, ${studentjoin}"
                userInfoList.add(teacherInfo)
            }
        }
        studentteacherobject.obj.myList.forEach { user ->
                if (user is Student) {
                      val studentInfo =  "${user.name},${user.idno},${user.dob},${user.teacherIdMatch},${user.teacherNameMatch}"
                    userInfoList.add(studentInfo)
                }
        }
        textView.text = userInfoList.joinToString(separator = "\n")
    }
}
