package com.example.learning

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learning.ui.NewActivity

class MainActivity : AppCompatActivity() {

    private val obj = School()
    private var incrementStudent = 1
    private var incrementTeacher = 1
    private lateinit var editTextName: EditText
    private lateinit var editTextDob: EditText
    private lateinit var editSearch: EditText
    private lateinit var searchTeacher: EditText
    private lateinit var editTextTeacher: EditText
    private lateinit var searchMatchStudent: EditText
    private var accumulatedData = ""


    fun av() {
        val uiUserList: MutableList<UiUser> = mutableListOf()

        obj.myList.forEach { user ->
            if (user is Teacher) {
                val matchStudentUiList = obj.myList.filterIsInstance<Student>()
                    .filter { user.idno == it.teacherIdMatch }
                val maplist = matchStudentUiList.map { it.name }
                val studentjoin = maplist.joinToString(separator = "\n")
                val teacherUi = TeacherUi(user.name, user.idno, user.dob, studentjoin)
                Log.d("matchstudents", studentjoin)
                Log.d("matchStudentUiListsize", matchStudentUiList.size.toString())
                uiUserList.add(teacherUi)
            } else if (user is Student) {
                val studentUi = StudentUi(
                    user.name,
                    user.idno,
                    user.dob,
                    user.teacherIdMatch,
                    user.teacherNameMatch
                )
                uiUserList.add(studentUi)
            }
        }
        val xyz = uiUserList.toList()
        adapter.submitList(xyz)
    }

    val adapter = AddToScheduleTimeSlotAdapter { uiUser ->

        if (uiUser is TeacherUi) {
            val removedTeacher = obj.myList.find { user ->
                (user.idno == uiUser.idno) && user is Teacher
            }
            Log.d("removedteacher", removedTeacher?.name.toString())
            if (removedTeacher != null) {
                val ReassignStudents =
                    obj.myList.filterIsInstance<Student>().filter { student ->
                        student.teacherIdMatch == removedTeacher.idno
                    }
                Log.d("ReassignStudents", ReassignStudents.size.toString())
                obj.myList.remove(removedTeacher)
                av()
                val dialogView =
                    LayoutInflater.from(this).inflate(R.layout.popuplayout, null)
               // val enterId = dialogView.findViewById<EditText>(R.id.enterId)

                val builder = AlertDialog.Builder(this)
                builder.setView(dialogView)
                builder.setTitle("Select a Teacher")
                builder.setIcon(R.drawable.circle)


                fun teachersname() : List<String> {
                    val teachers = obj.myList.filterIsInstance<Teacher>()
                        return teachers.map { it.name }
                }

                val teachersnames = teachersname()
                val checkedItem = intArrayOf(-1)
                builder.setSingleChoiceItems(teachersnames.toTypedArray(), checkedItem[0]) { dialog, which ->
                    checkedItem[0] = which
                    val selectedTeacherName = teachersnames[which]
                    Log.d("SelectedTeacherName", selectedTeacherName)
                }

                builder.setPositiveButton("OK") { dialog, which ->
                    val selectedTeacherIndex = checkedItem[0]
                    if (selectedTeacherIndex != -1) {
                        val selectedTeacher = obj.myList.filterIsInstance<Teacher>()[selectedTeacherIndex]
                        for (student in ReassignStudents) {
                            student.teacherIdMatch = selectedTeacher.idno
                            student.teacherNameMatch = selectedTeacher.name
                        }
                        av()
                    }
                }
                builder.show()
            }

        } else if (uiUser is StudentUi) {
            val studentToRemove = obj.myList.find { user ->
                Log.d("liststuidno", user.idno.toString())
                Log.d("studentId", uiUser.idno.toString())
                (user.idno == uiUser.idno) && user is Student
            }
            if (studentToRemove != null) {
                obj.myList.remove(studentToRemove)
            }
            av()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newscreen)


        val save: Button = findViewById(R.id.save)
        val saveOne: Button = findViewById(R.id.saveOne)
        val screen: TextView = findViewById(R.id.screen)
        val addStudent: Button = findViewById(R.id.add_student)
        val addTeacher: Button = findViewById(R.id.add_teacher)
        val sortStudents: Button = findViewById(R.id.sort_student)
        val matchName: Button = findViewById(R.id.match_name)
        val searchStudents: Button = findViewById(R.id.search_students)
        val recyclerView: RecyclerView = findViewById(R.id.recycle)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        editSearch = findViewById(R.id.editSearch)
        searchTeacher = findViewById(R.id.searchTeacher)
        editTextName = findViewById(R.id.name)
        editTextDob = findViewById(R.id.dob)
        editTextTeacher = findViewById(R.id.teacher_search)
        searchMatchStudent = findViewById(R.id.searchMatchStudent)


        editSearch.visibility = EditText.GONE
        editTextTeacher.visibility = EditText.GONE
        matchName.visibility = Button.GONE
        searchTeacher.visibility = EditText.GONE
        searchMatchStudent.visibility = EditText.GONE
        save.visibility = EditText.GONE
        editTextName.visibility = EditText.GONE
        editTextDob.visibility = EditText.GONE
        val buttonClick = findViewById<Button>(R.id.click_me)
        buttonClick.visibility = Button.GONE



        buttonClick.setOnClickListener {
            val intent = Intent(this, NewActivity::class.java)
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
                obj.addValue(student)
                Log.d("tstudentidno", student.idno.toString())
                Log.d("teacherIdMatch", student.teacherIdMatch.toString())
                Log.d("teacherNameMatch", student.teacherNameMatch.toString())
                Log.d("mylistsize", obj.myList.size.toString())

                av()
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
                av()
                //Log.d("uiteachersize", uiUserList.size.toString())
                //Log.d("matchstudent", students.toString())
                return "$name, $idno, $dob,$students"
            }

            addStudent.setOnClickListener {
                editTextName.visibility = EditText.VISIBLE
                editTextDob.visibility = EditText.VISIBLE
                editTextTeacher.visibility = EditText.VISIBLE
                editSearch.visibility = EditText.GONE
                save.visibility = Button.VISIBLE
                saveOne.visibility = Button.GONE
                searchTeacher.visibility = EditText.GONE
                searchMatchStudent.visibility = EditText.GONE

            }

            save.setOnClickListener {
                val name = editTextName.text.toString()
                val dob = editTextDob.text.toString()
                val searchTeacher = editTextTeacher.text.toString()

                if (name.isNotBlank() && dob.isNotBlank()) {
                    val duplicateStudent =
                        obj.myList.find { it.name.equals(name, ignoreCase = true) && it.dob == dob }
                    if (duplicateStudent != null) {
                        accumulatedData += "IT ALREADY EXISTS\n"
                        screen.text = accumulatedData
                    } else if (searchTeacher.isNotBlank()) {
                        val matchTeacher = obj.myList.find {
                            val result =
                                it.name.equals(searchTeacher, ignoreCase = true) && it is Teacher
                            result
                        }
                        if (matchTeacher != null) {
                            val value = addStudentToList(
                                name,
                                dob,
                                teacherIdMatch = matchTeacher.idno,
                                teacherNameMatch = matchTeacher.name
                            )
                            accumulatedData += "$value\n"
                            screen.text = accumulatedData
                            Log.d("match", matchTeacher.idno.toString())
                        }
                    }
                    editTextName.text.clear()
                    editTextDob.text.clear()
                    editTextTeacher.text.clear()
                    editTextName.visibility = EditText.GONE
                    editTextDob.visibility = EditText.GONE
                    editTextTeacher.visibility = EditText.GONE
                } else {
                    accumulatedData += "NO MATCH FOUND\n"
                    screen.text = accumulatedData
                }
                accumulatedData += "PLZ FIND TEACHER\n"
                screen.text = accumulatedData
            }

            addTeacher.setOnClickListener {
                editTextName.visibility = EditText.VISIBLE
                editTextDob.visibility = EditText.VISIBLE
                editTextTeacher.visibility = EditText.GONE
                editSearch.visibility = EditText.GONE
                saveOne.visibility = Button.VISIBLE
                save.visibility = Button.GONE
                searchTeacher.visibility = EditText.GONE
                searchMatchStudent.visibility = EditText.GONE
            }

            saveOne.setOnClickListener {
                val name = editTextName.text.toString()
                val dob = editTextDob.text.toString()

                if (name.isNotBlank() && dob.isNotBlank()) {
                    val duplicateTeacher = obj.myList.find {
                        it.name.equals(name, ignoreCase = true) && it.dob == dob
                    }
                    if (duplicateTeacher != null) {
                        // screen.text = "IT ALREADY EXISTS"
                        accumulatedData += "IT ALREADY EXISTS\n"
                        screen.text = accumulatedData
                    } else {
                        val value = addTeacherToList(
                            name,
                            idno = incrementTeacher,
                            dob,
                            students = ""
                        )
                        accumulatedData += "$value\n"
                        screen.text = accumulatedData
                    }
                }

                editTextName.text.clear()
                editTextDob.text.clear()
                editTextName.visibility = EditText.GONE
                editTextDob.visibility = EditText.GONE
            }

            matchName.setOnClickListener {
                editSearch.visibility = EditText.VISIBLE
                editTextName.visibility = EditText.GONE
                editTextDob.visibility = EditText.GONE
                editTextTeacher.visibility = EditText.GONE
                searchMatchStudent.visibility = EditText.GONE
            }

            sortStudents.setOnClickListener {
                val printStudent = obj.printStudent()
                screen.text = printStudent
                editSearch.visibility = EditText.GONE
                editTextName.visibility = EditText.GONE
                editTextDob.visibility = EditText.GONE
                editTextTeacher.visibility = EditText.GONE
                searchTeacher.visibility = EditText.GONE
                searchMatchStudent.visibility = EditText.GONE
            }


            editSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val stringBuilder = StringBuilder()

                    val newList = obj.myList.filter { user ->
                        val result = (user.name.contains(
                            s.toString(),
                            ignoreCase = true
                        ) || (user.dob == s.toString()))
                        result
                    }

                    if (newList.isNotEmpty()) {
                        newList.forEach { user ->
                            val abc = user.name
                            stringBuilder.append("${abc}\n")
                        }
                    } else {
                        stringBuilder.append("NO MATCH FOUND")
                    }
                    screen.text = stringBuilder.toString()
                }
            })

            searchStudents.setOnClickListener {
                editSearch.visibility = EditText.GONE
                editTextName.visibility = EditText.GONE
                editTextDob.visibility = EditText.GONE
                editTextTeacher.visibility = EditText.GONE
                searchTeacher.visibility = EditText.VISIBLE
                searchMatchStudent.visibility = EditText.VISIBLE

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
                                accumulatedData = matchStudent.name
                                Log.d("cone", matchStudent.name.toString())
                                screen.text = accumulatedData
                            } else {
                                screen.text = "NO MATCH Student FOUND"
                            }
                        }

                    } else {
                        screen.text = "NO MATCH Teacher FOUND"
                    }
                } else {
                    screen.text = "Plz Enter The Field"
                }
            }
        }
    }



