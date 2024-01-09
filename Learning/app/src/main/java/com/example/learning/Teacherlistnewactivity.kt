package com.example.learning

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learning.studentteacherobject.obj

class teacherlistnewactivity : AppCompatActivity(){

  //  private val obj = School()

      @SuppressLint("SuspiciousIndentation")
      fun av() {
          Log.d("functioncalltwo", "functioncall")
        val uiUserList: MutableList<UiUser> = mutableListOf()

          studentteacherobject.obj.myList.forEach { user ->
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
            val teachers = obj.myList.filterIsInstance<Teacher>()
            if (teachers.size > 1) {
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


                    @SuppressLint("SuspiciousIndentation")
                    fun teachersname(): List<String> {
                        val teachers = obj.myList.filterIsInstance<Teacher>()
                        return teachers.map { it.name }
                    }

                    val teachersnames = teachersname()
                    val checkedItem = intArrayOf(-1)
                    builder.setSingleChoiceItems(
                        teachersnames.toTypedArray(),
                        checkedItem[0]
                    ) { dialog, which ->
                        checkedItem[0] = which
                        val selectedTeacherName = teachersnames[which]
                        Log.d("SelectedTeacherName", selectedTeacherName)
                    }


                    builder.setPositiveButton("SAVE") { dialog, which ->
                        val selectedTeacherIndex = checkedItem[0]
                        if (selectedTeacherIndex != -1) {
                            val selectedTeacher =
                                obj.myList.filterIsInstance<Teacher>()[selectedTeacherIndex]
                            for (student in ReassignStudents) {
                                student.teacherIdMatch = selectedTeacher.idno
                                student.teacherNameMatch = selectedTeacher.name
                            }
                            av()
                        }
                        }
                        builder.show()
                    }
                } else {
                    Toast.makeText(
                        this@teacherlistnewactivity,
                        "Can't Remove Last Teacher",
                        Toast.LENGTH_SHORT
                    ).show()
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
            setContentView(R.layout.studentteacherrecycle)
//             val sortButton: MaterialButton = findViewById(R.id.sort_User)
//             val filterButton: MaterialButton = findViewById(R.id.filter_User)
//             sortButton.visibility = View.GONE
//             filterButton.visibility = View.GONE
            val childLayout: ConstraintLayout = findViewById(R.id.child_layout)
            childLayout.visibility = View.GONE
            val recyclerView: RecyclerView = findViewById(R.id.studentTeacherRecycle)

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
            av()
        }
     }
