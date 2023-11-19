package com.app.admin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.admin.adapters.ViewTeachersAdapter
import com.app.admin.databinding.FragmentViewTeachersBinding
import com.app.admin.models.Teacher
import com.app.admin.utils.MAIN_MENU

class ViewTeachersFragment : Fragment() {

    private lateinit var binding: FragmentViewTeachersBinding
    private lateinit var teacherList: List<Teacher>
    private lateinit var teacherAdapter: ViewTeachersAdapter
    private var argumentTitle: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = requireArguments().getString(MAIN_MENU)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewTeachersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        events()
    }

    private fun init() {
        binding.apply {
            binding.smsText.text = argumentTitle
            teacherRecyclerView.layoutManager = LinearLayoutManager(context)

            teacherList = listOf(
                Teacher("John Doe", "john@example.com", "Math", "Grade 9"),
                Teacher("Jane Smith", "jane@example.com", "Science", "Grade 7"),
                Teacher("Michael Johnson", "michael@example.com", "History", "Grade 10"),
                Teacher("Emily Davis", "emily@example.com", "English", "Grade 8"),
                Teacher("Robert Wilson", "robert@example.com", "Physics", "Grade 11")
            )
            teacherAdapter = ViewTeachersAdapter(teacherList) { teacher ->
                teacherList = teacherList.filter { it != teacher }
                teacherAdapter.notifyDataSetChanged()
            }
            teacherRecyclerView.adapter = teacherAdapter
        }
    }

    private fun events() {
        binding.apply {
            leftIcon.setOnClickListener { findNavController().popBackStack() }
        }
    }
}
