package com.app.admin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.admin.databinding.ItemTeachersBinding
import com.app.admin.models.Teacher


class ViewTeachersAdapter (
    private val teacherList: List<Teacher>,
    private val callback : (Teacher?) -> Unit
) :
    RecyclerView.Adapter<ViewTeachersAdapter.TeacherViewHolder>() {

    inner class TeacherViewHolder(private val binding: ItemTeachersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(teacher: Teacher) {
            binding.apply {
                teacherNameTextView.text = teacher.name
                teacherEmailTextView.text = teacher.email
                teacherSubjectTextView.text = teacher.subject
                teacherClassTextView.text = teacher.classToTeach

                removeTeacherIcon.setOnClickListener {
                    callback.invoke(teacher)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val binding = ItemTeachersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeacherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacher = teacherList[position]
        holder.bind(teacher)
    }

    override fun getItemCount(): Int {
        return teacherList.size
    }
}
