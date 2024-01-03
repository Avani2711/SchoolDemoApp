package com.example.learning

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.learning.studentteacherobject.obj

class MainActivity : AppCompatActivity() {

    //var obj = School()
    private var incrementStudent = 1
    private var incrementTeacher = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newscreen)

        val addStudent: Button = findViewById(R.id.add_student)
        val addTeacher: Button = findViewById(R.id.add_teacher)
        val sortStudents: Button = findViewById(R.id.sort_student)
        val matchName: Button = findViewById(R.id.match_name)
        val searchStudents: Button = findViewById(R.id.search_student)
        val usersList: Button = findViewById(R.id.all_users)


        usersList.setOnClickListener {
            val intent = Intent(this, userlist::class.java)
            startActivity(intent)
        }

        fun addStudentToList(
            name: String,
            dob: String,
            teacherIdMatch: Int,
            teacherNameMatch: String
        ): String {
            val student = Student(name, 0, dob, teacherIdMatch, teacherNameMatch)
            student.idno = incrementStudent
            incrementStudent++
            studentteacherobject.obj.addValue(student)
            Log.d("tstudentidno", student.idno.toString())
            Log.d("teacherIdMatch", student.teacherIdMatch.toString())
            Log.d("teacherNameMatch", student.teacherNameMatch.toString())
            Log.d("mylistsize", obj.myList.size.toString())

            val studentListNewActivity = StudentListNewActivity()
            studentListNewActivity.av()

            // av()
            return "$name, $dob ,$teacherIdMatch, $teacherNameMatch"
        }


        fun addTeacherToList(
            name: String,
            idno: Int,
            dob: String,
            students: String
        ): String {
            val teacher = Teacher(name, 0, dob)
            teacher.idno = incrementTeacher
            incrementTeacher++
            obj.addValue(teacher)
            Log.d("teacheridno", teacher.idno.toString())
            Log.d("mylistsize", obj.myList.size.toString())

            val teacherlistnewactivity = teacherlistnewactivity()
            teacherlistnewactivity.av()
            Log.d("reach to mylist", "hey reached")
            return "$name, $idno, $dob,$students"
        }

        addStudent.setOnClickListener {

            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.editstudentlayout, null)
            val editTextName = dialogView.findViewById<EditText>(R.id.name)
            val editTextDob = dialogView.findViewById<EditText>(R.id.dob)
            val selectTeacher = dialogView.findViewById<TextView>(R.id.select_teacher)

            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)
            builder.setTitle("Add Student")

            builder.setPositiveButton("SAVE") { dialog, which ->
                val name = editTextName.text.toString()
                val dob = editTextDob.text.toString()
                //val selectTeacher = selectTeacher.text.toString()

                if (name.isNotBlank() && dob.isNotBlank()) {

                    if (selectTeacher != null) {

                        val dialogViewOne =
                            LayoutInflater.from(this).inflate(R.layout.searchteacherpoplayout, null)

                        val builderOne = AlertDialog.Builder(this)
                        builderOne.setView(dialogViewOne)
                        builderOne.setTitle("Select Teacher")
                        builderOne.setIcon(R.drawable.radio)

                        @SuppressLint("SuspiciousIndentation")
                        fun teachersname(): List<String> {
                            val teachers = obj.myList.filterIsInstance<Teacher>()
                            return teachers.map { it.name }
                        }

                        val teachersnames = teachersname()
                        val checkedItem = intArrayOf(-1)
                        builderOne.setSingleChoiceItems(
                            teachersnames.toTypedArray(),
                            checkedItem[0]
                        ) { _, which ->
                            checkedItem[0] = which
                            val selectedTeacherName = teachersnames[which]
                            Log.d("SelectedTeacherNameOne", selectedTeacherName)
                        }

                        builderOne.setPositiveButton("SAVE") { _, which ->
                            val selectedTeacherIndex = checkedItem[0]
                            if (selectedTeacherIndex != -1) {
                                val selectedTeacher =
                                    obj.myList.filterIsInstance<Teacher>()[selectedTeacherIndex]

                                val value = addStudentToList(
                                    name,
                                    dob,
                                    teacherIdMatch = selectedTeacher.idno,
                                    teacherNameMatch = selectedTeacher.name
                                )
                                val intent = Intent(this, teacherlistnewactivity::class.java)
                                startActivity(intent)
                            }
                        }
                        builderOne.show()
                    }
                } else {
                    val duplicateStudent =
                        obj.myList.find {
                            it.name.equals(
                                name,
                                ignoreCase = true
                            ) && it.dob == dob
                        }
                    if (duplicateStudent != null) {
                        Toast.makeText(
                            this@MainActivity,
                            "Student already exists!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            builder.show()
        }



        addTeacher.setOnClickListener {

            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.editteacherlayout, null)
            val editTextName = dialogView.findViewById<EditText>(R.id.name)
            val editTextDob = dialogView.findViewById<EditText>(R.id.dob)

            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)
            builder.setTitle("Add Teacher")


            builder.setPositiveButton("SAVE") { dialog, which ->
                val name = editTextName.text.toString()
                val dob = editTextDob.text.toString()

                if (name.isNotBlank() && dob.isNotBlank()) {
                    val duplicateTeacher = obj.myList.find {
                        it.name.equals(name, ignoreCase = true) && it.dob == dob
                    }
                    if (duplicateTeacher != null) {

                        Toast.makeText(
                            this@MainActivity,
                            "Teacher already exists!",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        val value = addTeacherToList(
                            name,
                            idno = incrementTeacher,
                            dob,
                            students = ""
                        )
                    }
                }
                val intent = Intent(this, teacherlistnewactivity::class.java)
                startActivity(intent)
            }
            builder.show()

        }


        matchName.setOnClickListener {
        }

        sortStudents.setOnClickListener {
            val printStudent = obj.printStudent()
        }

//
//            editSearch.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    before: Int,
//                    count: Int
//                ) {
//                }
//
//                override fun afterTextChanged(s: Editable?) {
//                    val stringBuilder = StringBuilder()
//
//                    val newList = obj.myList.filter { user ->
//                        val result = (user.name.contains(
//                            s.toString(),
//                            ignoreCase = true
//                        ) || (user.dob == s.toString()))
//                        result
//                    }
//
//                    if (newList.isNotEmpty()) {
//                        newList.forEach { user ->
//                            val abc = user.name
//                            stringBuilder.append("${abc}\n")
//                        }
//                    } else {
//                        stringBuilder.append("NO MATCH FOUND")
//                    }
//                    screen.text = stringBuilder.toString()
//                }
//            })

        searchStudents.setOnClickListener {

            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.searchmatchstudentlayout, null)
            val searchTeacher = dialogView.findViewById<EditText>(R.id.searchMatchStudent)
            val searchMatchStudent = dialogView.findViewById<EditText>(R.id.searchTeacher)

            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView)
            builder.setTitle("Search Match User")


            builder.setPositiveButton("SAVE") { dialog, which ->
                val nameOne = searchTeacher.text.toString()
                val nameTwo = searchMatchStudent.text.toString()

                if (nameOne.isNotBlank() || nameTwo.isNotBlank()) {
                    val matchTeacher = obj.myList.find {
                        it.name.equals(
                            nameOne,
                            ignoreCase = false
                        ) && it is Teacher
                    }

                    if (matchTeacher != null) {
                        val matchStudents = obj.myList.filter {
                            if (it is Student) {
                                //Log.d("checkimg name", it.name)
                                Log.d("checkimg ", it.teacherIdMatch.toString())
                                Log.d(
                                    "checkimg name",
                                    matchTeacher.idno.toString()
                                )
                                return@filter it.teacherIdMatch == matchTeacher.idno
                            }
                            return@filter false
                        }

                        if (matchStudents.isNotEmpty()) {
                            val matchStudent = matchStudents.find {
                                it.name.equals(nameTwo, ignoreCase = false)
                            }

                            if (matchStudent != null) {
                                //accumulatedData = matchStudent.name
                                Toast.makeText(
                                    this@MainActivity,
                                    "Student already added",
                                    Toast.LENGTH_SHORT
                                ).show()

                                Log.d("cone", matchStudent.name.toString())
                                //screen.text = accumulatedData
                            } else {
                                // screen.text = "NO MATCH Student FOUND"
                                Toast.makeText(
                                    this@MainActivity,
                                    "NO MATCH Student FOUND!",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }

                    } else {
                        //screen.text = "NO MATCH Teacher FOUND"
                        Toast.makeText(
                            this@MainActivity,
                            "NO MATCH Teacher FOUND!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // screen.text = "Plz Enter The Field"
                    Toast.makeText(
                        this@MainActivity,
                        "Plz Enter The Field!",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
            builder.show()
        }
    }
}



