package com.attech.teacher.ui.fragments

import android.R
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.teacher.adapters.StudentAdapter
import com.attech.teacher.databinding.FragmentUploadMarksBinding
import com.attech.teacher.models.StudentDetailsResponse
import com.attech.teacher.network.RetrofitClientInstance
import com.attech.teacher.repository.RetrofitRepository
import com.attech.teacher.utils.LoadingDialog
import com.attech.teacher.utils.MAIN_MENU
import com.attech.teacher.utils.PickerManager
import com.attech.teacher.utils.USER_TYPE
import com.attech.teacher.utils.Utils
import com.attech.teacher.viewmodel.RetrofitViewModel
import com.attech.teacher.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class UploadMarksFragment : Fragment() {
    private var loaderShown = false
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var binding: FragmentUploadMarksBinding
    private var argumentTitle: String? = null
    private lateinit var viewModel: RetrofitViewModel
    private var selectedClass: String = ""
    private var selectedCourse: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = requireArguments().getString(MAIN_MENU)
        if (!loaderShown) {
            showLoader()
            loaderShown = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadMarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        setAdapter()
        setupClassSpinner()
        observeViewModel()
        setEventListeners()
        setHandler()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeViews() {
        binding.smsText.text = arguments?.getString(MAIN_MENU)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]

    }

    private fun setAdapter() {
        studentAdapter = StudentAdapter { model, position ->
            navigateToScreen(model, position, selectedCourse, selectedClass)
        }
        binding.recyclerView.adapter = studentAdapter
    }


    private fun navigateToScreen(
        model: StudentDetailsResponse,
        position: Int,
        selectedCourse: String,
        selectedClass: String
    ) {
        val selectedCourseID =
            PickerManager.getTeacherCourses?.firstOrNull {
                it.name == selectedCourse
            }?.id?.toInt() ?: 0

        val selectedClassID =
            PickerManager.allBatchesList?.firstOrNull {
                it.batchcode == selectedClass
            }?.id ?: 0

        val studentId = model.id
        val rollNo = model.rollno
        val name = "${model.firstname} ${model.lastname}"

        val bundle = bundleOf(
            "selectedCourseID" to selectedCourseID,
            "selectedClassID" to selectedClassID,
            "selectedCourseName" to selectedCourse,
            "selectedClassName" to selectedClass,
            "studentId" to studentId,
            "rollNo" to rollNo,
            "name" to name
        )

        findNavController().navigate(com.attech.teacher.R.id.action_uploadMarksFragment_to_uploadDetailedMarks, bundle)

    }

    private fun setupClassSpinner() {
        binding.spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedClass = (parent?.getItemAtPosition(position) as? String)!!
                selectedClass?.let { viewModel.getBatchStudents(USER_TYPE, PickerManager.token!!, selectedClass) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerCourses.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCourse = (parent?.getItemAtPosition(position) as? String)!!
                    selectedClass?.let {}
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun loadCourseSpinner(teacherCourses: List<String>) {
        teacherCourses?.let {
            val classAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, it)
            classAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spinnerCourses.adapter = classAdapter
        }
    }

    private fun loadBatchesSpinner(batches: List<String>) {
        batches?.let {
            val classAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, it)
            classAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spinnerClass.adapter = classAdapter
        }
    }


    private fun observeViewModel() {
        observeBatchStudents()
        observeTeacherCoursesAndBatches()
    }


    private fun observeBatchStudents() {
        viewModel.batchStudents.observe(viewLifecycleOwner) { batches ->
            if (!batches.isNullOrEmpty()) {
                studentAdapter.setStudents(batches)
            }
        }
    }

    private fun observeTeacherCoursesAndBatches() {
        viewModel.teacherClasses.observe(viewLifecycleOwner) { teacherClasses ->
            teacherClasses?.let {
                loadBatchesSpinner(it.batches)
                loadCourseSpinner(it.courses)
            }
        }
    }

    private fun setEventListeners() {
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }


    private fun showLoader() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.showLoadingDialog("loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }

    private fun setHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        },2500)
    }

}