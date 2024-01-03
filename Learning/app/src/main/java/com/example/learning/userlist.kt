package com.example.learning

 import android.annotation.SuppressLint
 import android.os.Bundle
 import android.widget.PopupMenu
 import android.widget.TextView
 import androidx.appcompat.app.AppCompatActivity
 import com.google.android.material.button.MaterialButton

class userlist : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userlist)
        val textView: TextView = findViewById(R.id.user_list)
        val sortButton: MaterialButton = findViewById(R.id.sort_User)
        val filterButton: MaterialButton = findViewById(R.id.filter_User)

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

        sortButton.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.sort_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sort_a_to_z -> {
                        val sortedUsers = userInfoList.sorted()
                        textView.text = sortedUsers.joinToString(separator = "\n")
                        true
                    }
                    R.id.sort_z_to_a -> {
                        val sortedUsers = userInfoList.sortedDescending()
                        textView.text = sortedUsers.joinToString(separator = "\n")
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }


        filterButton.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.filter_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.filter_student -> {
                        val studentInfoList = studentteacherobject.obj.myList.filterIsInstance<Student>()
                        val studentInfoText = studentInfoList.joinToString("\n") {
                            "${it.name}, ${it.idno}, ${it.dob}, ${it.teacherIdMatch}, ${it.teacherNameMatch}"
                        }
                        textView.text = studentInfoText
                        true
                    }
                    R.id.filter_teacher -> {
                        val teacherInfoList = studentteacherobject.obj.myList.filterIsInstance<Teacher>()
                        val teacherInfoText = teacherInfoList.joinToString("\n") { teacher ->
                            val matchStudentUiList =
                                studentteacherobject.obj.myList.filterIsInstance<Student>()
                                    .filter {student->
                                        teacher.idno == student.teacherIdMatch }
                            val maplist = matchStudentUiList.map { it.name }
                            val studentjoin = maplist.joinToString(separator = "\n")
                           "${teacher.name}, ${teacher.idno}, ${teacher.dob}, $studentjoin"
                        }
                        textView.text = teacherInfoText
                        true
                    }
                    R.id.filter_User -> {
                        textView.text = userInfoList.joinToString(separator = "\n")
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
}
