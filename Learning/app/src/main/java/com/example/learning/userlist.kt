package com.example.learning

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class userlist : AppCompatActivity() {

    fun av() {
        Log.d("functioncalltwo", "functioncall")
        val uiUserList: MutableList<UiUser> = mutableListOf()

        studentteacherobject.obj.myList.forEach { user ->
            if (user is Teacher) {
                val matchStudentUiList = studentteacherobject.obj.myList.filterIsInstance<Student>()
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
            val teachers = studentteacherobject.obj.myList.filterIsInstance<Teacher>()
            val students = studentteacherobject.obj.myList.filterIsInstance<Student>()
            if (teachers.size > 1 && students.isEmpty()) {
                val removedTeacher = studentteacherobject.obj.myList.find { user ->
                    (user.idno == uiUser.idno) && user is Teacher
                }
                Log.d("removedteacher", removedTeacher?.name.toString())

                studentteacherobject.obj.myList.remove(removedTeacher)
                av()

            }else if (teachers.size > 1 && students.isNotEmpty()) {
                val removedTeacher = studentteacherobject.obj.myList.find { user ->
                    (user.idno == uiUser.idno) && user is Teacher
                }
                Log.d("removedteacher", removedTeacher?.name.toString())
                if (removedTeacher != null) {
                    val ReassignStudents =
                        studentteacherobject.obj.myList.filterIsInstance<Student>()
                            .filter { student ->
                                student.teacherIdMatch == removedTeacher.idno
                            }
                    Log.d("ReassignStudents", ReassignStudents.size.toString())
                    studentteacherobject.obj.myList.remove(removedTeacher)
                    av()

                    val dialogView =
                        LayoutInflater.from(this).inflate(R.layout.popuplayout, null)
                    val builder = AlertDialog.Builder(this)
                    builder.setView(dialogView)
                    builder.setTitle("Select a Teacher")
                    builder.setIcon(R.drawable.circle)


                    @SuppressLint("SuspiciousIndentation")
                    fun teachersname(): List<String> {
                        val teachers = studentteacherobject.obj.myList.filterIsInstance<Teacher>()
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
                                studentteacherobject.obj.myList.filterIsInstance<Teacher>()[selectedTeacherIndex]
                            for (student in ReassignStudents) {
                                student.teacherIdMatch = selectedTeacher.idno
                                student.teacherNameMatch = selectedTeacher.name
                            }
                            av()
                        }
                    }
                    builder.show()
                }

            }else{
                Toast.makeText(
                    this@userlist,
                    "Can't Remove Last Teacher",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (uiUser is StudentUi) {
            val studentToRemove = studentteacherobject.obj.myList.find { user ->
                Log.d("liststuidno", user.idno.toString())
                Log.d("studentId", uiUser.idno.toString())
                (user.idno == uiUser.idno) && user is Student
            }
            if (studentToRemove != null) {
                studentteacherobject.obj.myList.remove(studentToRemove)
            }
            av()
        }
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.studentteacherrecycle)
        val sortButton: MaterialButton = findViewById(R.id.sort_User)
        val filterButton: MaterialButton = findViewById(R.id.filter_User)


        val recyclerView: RecyclerView = findViewById(R.id.studentTeacherRecycle)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        av()

        val sortedUiUserListOne: MutableList<UiUser> = mutableListOf()
        studentteacherobject.obj.myList.forEach { user ->
            if (user is Teacher) {
                val matchStudentUiList =
                    studentteacherobject.obj.myList.filterIsInstance<Student>()
                        .filter { user.idno == it.teacherIdMatch }
                val maplist = matchStudentUiList.map { it.name }
                val studentjoin = maplist.joinToString(separator = "\n")
                val teacherUi = TeacherUi(user.name, user.idno, user.dob, studentjoin)
                sortedUiUserListOne.add(teacherUi)
            }
        }
        studentteacherobject.obj.myList.forEach { user ->
            if (user is Student) {
                val studentUi = StudentUi(
                    user.name,
                    user.idno,
                    user.dob,
                    user.teacherIdMatch,
                    user.teacherNameMatch
                )
                sortedUiUserListOne.add(studentUi)
            }
        }

        sortButton.setOnClickListener { it ->
            val popupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.sort_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sort_a_to_z -> {
                        adapter.submitList(sortedUiUserListOne.sortedBy { it.name })
                        true
                    }

                    R.id.sort_z_to_a -> {
                        adapter.submitList(sortedUiUserListOne.sortedByDescending { it.name })
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }


        val filteredUiUserTeacherList: MutableList<UiUser> = mutableListOf()
        studentteacherobject.obj.myList.forEach { user ->
            if (user is Teacher) {
                val matchStudentUiList =
                    studentteacherobject.obj.myList.filterIsInstance<Student>()
                        .filter { user.idno == it.teacherIdMatch }
                val maplist = matchStudentUiList.map { it.name }
                val studentjoin = maplist.joinToString(separator = "\n")
                val teacherUi = TeacherUi(user.name, user.idno, user.dob, studentjoin)
                filteredUiUserTeacherList.add(teacherUi)
            }
        }

        val filteredUiUserStudentList: MutableList<UiUser> = mutableListOf()
        studentteacherobject.obj.myList.forEach { user ->
            if (user is Student) {
                val studentUi = StudentUi(
                    user.name,
                    user.idno,
                    user.dob,
                    user.teacherIdMatch,
                    user.teacherNameMatch
                )
                filteredUiUserStudentList.add(studentUi)
            }
        }


        filterButton.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.inflate(R.menu.filter_menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.filter_student -> {
                        val xyz = filteredUiUserStudentList.toList()
                        adapter.submitList(xyz)
                        true
                    }
                    R.id.filter_teacher -> {
                        val xyz = filteredUiUserTeacherList.toList()
                        adapter.submitList(xyz)
                        true
                    }
                    R.id.filter_User -> {
                        val allUiUser = filteredUiUserStudentList + filteredUiUserTeacherList
                        val xyz = allUiUser.toList()
                        adapter.submitList(xyz)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }
}

