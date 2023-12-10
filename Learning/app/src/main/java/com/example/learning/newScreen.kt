package com.example.learning

import java.io.Serializable
interface UiUser {
    val name: String
    var dob: String
    var idno: Int
}
class StudentUi (override val name:String,override var idno:Int,override var dob:String,var teacherIdMatch:Int, var teacherNameMatch:String): UiUser {
}
class TeacherUi (override val name:String,override var idno:Int,override var dob:String,val students:String): UiUser {
}

interface User : Serializable {
    val name: String
    var dob: String
    var idno: Int
}
class Student (override val name:String,override var idno:Int,override var dob:String,var teacherIdMatch:Int, var teacherNameMatch:String): User {

//    override fun idno(){
//        println("my idno is $idno")
//    }

}

class Teacher (override val name:String,override var idno:Int,override var dob:String): User {

//    override fun idno(){
//        println("my idno is $idno")
//    }
}

class Principle (override val name:String,override var idno:Int,override var dob:String): User {
//
//    override fun idno(){
//        println("my idno is $idno")
//    }
}

class Peon(override val name: String,override var idno: Int, override var dob: String): User {

//
//    override fun idno(){
//        println("my idno is $idno")
//    }
}




class School {
    val myList: MutableList<User> = mutableListOf()

    fun addValue(user: User) {
        myList.add(user)
    }

    fun printNamee() {
        myList.forEach { element -> (element.name)
        }
    }

//    fun printStudent() {
//        myList.forEach { element ->
//
//            println("name = ${element.name}")
//        }
//    }

        fun countStudent(): Int {
            return myList.count { it is Student }
        }

        fun countTeacher(): Int {
            return myList.count { it is Teacher }
        }

        fun printStudent(): String {
            return myList.sortedBy { it.name }.joinToString(separator = "\n") {"${it.name}, ${it.dob}"}
        }

        fun printName(user: User) {
            println("${user.name},${user.dob}")
        }
    }




//    fun printStudent(): String {
//        val stringBuilder = StringBuilder()
//
//        myList.forEach { element ->
//            stringBuilder.append("name = ${element.name} and id = ${element.idno()}\n")
//        }
//
//        return stringBuilder.toString()
//    }




//
