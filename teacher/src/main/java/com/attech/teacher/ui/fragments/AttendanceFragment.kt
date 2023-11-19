package com.attech.teacher.ui.fragments

import android.R
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.teacher.adapters.AttendanceAdapter
import com.attech.teacher.databinding.FragmentAttendanceBinding
import com.attech.teacher.repository.TeacherRepository
import com.attech.teacher.utils.MAIN_MENU
import com.attech.teacher.viewmodel.TeacherViewModel
import com.attech.teacher.viewmodelfactory.TeacherViewModelFactory


class AttendanceFragment : Fragment() {
    private lateinit var binding: FragmentAttendanceBinding
    private var argumentTitle: String? = null
    private lateinit var attendanceAdapter: AttendanceAdapter
    private val viewModel: TeacherViewModel by viewModels { TeacherViewModelFactory(TeacherRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = requireArguments().getString(MAIN_MENU)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        observeViewModel()
        setEventListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeViews() {
        binding.smsText.text = arguments?.getString(MAIN_MENU)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        attendanceAdapter = AttendanceAdapter { updatedStudent ->
            viewModel.handleAttendanceUpdate(updatedStudent)
        }

        binding.recyclerView.adapter = attendanceAdapter

        setupClassSpinner()
        setupDatePicker()
    }

    private fun setupClassSpinner() {
        binding.spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedClass = parent?.getItemAtPosition(position) as? String
                selectedClass?.let { viewModel.onClassSelected(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupDatePicker() {
        binding.datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            viewModel.onDateSelected(year, monthOfYear, dayOfMonth)
        }
    }
    private fun observeViewModel() {
        viewModel.classes.observe(viewLifecycleOwner) { classes ->
            classes?.let {
                val classAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, it)
                classAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                binding.spinnerClass.adapter = classAdapter
            }
        }

        viewModel.attendance.observe(viewLifecycleOwner) { students ->
            students?.let {
                attendanceAdapter.setStudents(it)
            }
        }

    }

    private fun setEventListeners() {
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
        binding.btnSubmit.setOnClickListener { viewModel.submitAttendance() }
    }
}