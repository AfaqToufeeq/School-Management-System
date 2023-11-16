package com.app.admin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.admin.adapters.StudentAdapter
import com.app.admin.databinding.FragmentStudentListBinding
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.PickerManager.token
import com.app.admin.utils.USER_TYPE
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory

class StudentListFragment : Fragment() {
    private lateinit var binding: FragmentStudentListBinding
    private var argumentTitle: String? = null
    private val studentAdapter = StudentAdapter()
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setObserver()
        events()
    }

    private fun init() {
        binding.apply {
            toolbar.smsText.text = argumentTitle
            studentsRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
            viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
            studentsRecyclerView.adapter = studentAdapter
        }
    }

    private fun events() {
        binding.apply {
            toolbar.leftIcon.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setObserver() {
        with(viewModel) {
            fetchStudents(USER_TYPE, token!!)
            students.observe(viewLifecycleOwner) { students ->
                studentAdapter.submitList(students)
            }
        }
    }
}