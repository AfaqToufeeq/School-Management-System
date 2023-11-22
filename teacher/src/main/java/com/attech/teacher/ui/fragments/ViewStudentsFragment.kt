package com.attech.teacher.ui.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.teacher.adapters.StudentAdapter
import com.attech.teacher.databinding.FragmentViewStudentsBinding
import com.attech.teacher.repository.TeacherRepository
import com.attech.teacher.utils.MAIN_MENU
import com.attech.teacher.utils.PickerManager
import com.attech.teacher.viewmodel.TeacherViewModel
import com.attech.teacher.viewmodelfactory.TeacherViewModelFactory

class ViewStudentsFragment : Fragment() {

    private lateinit var binding: FragmentViewStudentsBinding
    private lateinit var studentAdapter: StudentAdapter
    private val viewModel: TeacherViewModel by viewModels { TeacherViewModelFactory(TeacherRepository()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        observeViewModel()
        setEventListeners()
    }

    private fun initializeViews() {
        binding.smsText.text = arguments?.getString(MAIN_MENU)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        studentAdapter = StudentAdapter()
        binding.recyclerView.adapter = studentAdapter

        setupClassSpinner()
    }

    private fun setupClassSpinner() {
        binding.spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedClass = parent?.getItemAtPosition(position) as? String
                selectedClass?.let { }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun observeViewModel() {
        val batchCodes = PickerManager.allBatchesList!!.map { it.batchcode }
        batchCodes?.let {
            val classAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, it)
            classAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spinnerClass.adapter = classAdapter
        }

        viewModel.attendance.observe(viewLifecycleOwner) { students ->
            students?.let {
                studentAdapter.setStudents(it)
            }
        }
    }

    private fun setEventListeners() {
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }
}
