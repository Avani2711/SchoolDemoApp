package com.example.learning


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class AddToScheduleTimeSlotAdapter( var removeList:(UiUser) -> Unit) :
    ListAdapter<UiUser, AddToScheduleTimeSlotAdapter.Superholder>(DiffCallBack()) {
    companion object {
        const val THE_FIRST_VIEW = 1
        const val THE_SECOND_VIEW = 2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Superholder {
        val context = parent.context
        if (viewType == THE_FIRST_VIEW) {
            return MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.recycleview, parent, false), this
            )
        }
        return MyViewHolderOne(
            LayoutInflater.from(context).inflate(R.layout.teacherrecycleview, parent, false), this
        )
    }


    override fun getItemViewType(position: Int): Int {
        val user = getItem(position)
        return if (user is StudentUi) THE_FIRST_VIEW else THE_SECOND_VIEW
    }

    override fun onBindViewHolder(holder: Superholder, position: Int) {
        holder.fillData(getItem(position))
        Log.d("cdnjc", "hyyhhn calle thswyu ")
    }

        abstract class Superholder(view: View, adapter: AddToScheduleTimeSlotAdapter) : RecyclerView.ViewHolder(view) {

            abstract fun fillData(user: UiUser)

//            init {
//                view.setOnClickListener {
//                    val context = view.context
//                    val user = adapter.getItem(bindingAdapterPosition)
//                    if (user != null) {
//                        val intent = Intent(context, RecycleNewActivity::class.java)
//                        intent.putExtra("user", User)
//                        context.startActivity(intent)
//                    }
//                }
//            }
        }

        class MyViewHolder(private val view: View, adapter: AddToScheduleTimeSlotAdapter) : Superholder(view, adapter) {
            private val studentTextView: TextView = view.findViewById(R.id.textView)
            private val button:Button = view.findViewById(R.id.button)
            override fun fillData(user: UiUser) {
                if (user is StudentUi) {
                    val data =
                        "${user.name} , ${user.dob} , ${user.idno.toString()}, ${user.teacherIdMatch.toString()},${user.teacherNameMatch}"
                    studentTextView.text = data
                }
            }
            init {
                button.setOnClickListener {
                    val userPosition = adapter.getItem(bindingAdapterPosition)
                    adapter.removeList(userPosition)
                }
            }

////            init {
//                replace.setOnClickListener {
//                    val userPosition = adapter.getItem(bindingAdapterPosition)
//                    adapter.replaceList(userPosition)
//                }
//            }
        }

        class MyViewHolderOne(private val view: View, adapter: AddToScheduleTimeSlotAdapter) : Superholder(view, adapter) {
            private var teacherTextView: TextView = view.findViewById(R.id.textViewOne)
            private val buttonOne:Button = view.findViewById(R.id.buttonOne)
            override fun fillData(user: UiUser) {
                Log.d("teacheruser", "hyyhhn calle thswyu $user ,")
                if (user is TeacherUi) {
                    val data = "${user.name} , ${user.dob} , ${user.idno}, ${user.students}"
                    teacherTextView.text = data
                }
            }
            init {
                buttonOne.setOnClickListener {
                    val userPosition = adapter.getItem(bindingAdapterPosition)
                    adapter.removeList(userPosition)
                }
            }

//            init {
//                replaceone.setOnClickListener {
//                    val userPosition = adapter.getItem(bindingAdapterPosition)
//                    adapter.replaceList(userPosition)
//                }
//            }


        }

        class DiffCallBack : DiffUtil.ItemCallback<UiUser>() {
            override fun areItemsTheSame(oldItem: UiUser, newItem: UiUser): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: UiUser, newItem: UiUser): Boolean {
                return oldItem == newItem
            }
        }
    }

