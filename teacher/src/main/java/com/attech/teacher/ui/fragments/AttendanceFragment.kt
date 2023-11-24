package com.attech.teacher.ui.fragments

import android.R
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.teacher.adapters.AttendanceAdapter
import com.attech.teacher.databinding.FragmentAttendanceBinding
import com.attech.teacher.models.AttendanceModel
import com.attech.teacher.models.AttendanceResponse
import com.attech.teacher.network.RetrofitClientInstance
import com.attech.teacher.repository.RetrofitRepository
import com.attech.teacher.utils.LoadingDialog
import com.attech.teacher.utils.MAIN_MENU
import com.attech.teacher.utils.PickerManager
import com.attech.teacher.utils.PickerManager.token
import com.attech.teacher.utils.USER_TYPE
import com.attech.teacher.utils.Utils.showToast
import com.attech.teacher.viewmodel.RetrofitViewModel
import com.attech.teacher.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Calendar

class AttendanceFragment : Fragment() {
    private var teacherCourses: List<String> = emptyList()
    private var loaderShown = false
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var binding: FragmentAttendanceBinding
    private var argumentTitle: String? = null
    private lateinit var attendanceAdapter: AttendanceAdapter
    private lateinit var viewModel: RetrofitViewModel
    private var selectedDate: String = ""
    private var selectedClass: String = ""
    private var selectedCourse: String = ""
    private var student: ArrayList<Int> = ArrayList()

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
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        setAdapter()
        spinnerEvents()
        observeViewModel()
        setEventListeners()
        setHandler()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeViews() {
        binding.smsText.text = arguments?.getString(MAIN_MENU)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]
    }

    private fun setAdapter() {
        attendanceAdapter = AttendanceAdapter { model, _, _ ->
            if (student.contains(model.id)) student.remove(model.id)
            else student.add(model.id)

            if (binding.btnSubmit.visibility == View.GONE)
                binding.btnSubmit.visibility = View.VISIBLE
        }
        binding.recyclerView.adapter = attendanceAdapter
    }

    private fun spinnerEvents() {
        binding.spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedClass = (parent?.getItemAtPosition(position) as? String)!!
                selectedClass?.let {
                    viewModel.getBatchStudents(USER_TYPE, token!!, selectedClass)
                }
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
                loadingDialog.dismissLoadingDialog()
                attendanceAdapter.setStudents(batches)
            }
        }
    }

    private fun observeTeacherCoursesAndBatches() {
        viewModel.teacherClasses.observe(viewLifecycleOwner) { teacherClasses ->
            teacherClasses?.let {
                loadBatchesSpinner(it.batches)
                loadCourseSpinner(it.courses)
                teacherCourses = it.courses
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.selectedDate.text = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun setEventListeners() {
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
        binding.datePickerButton.setOnClickListener { showDatePicker() }
        binding.btnSubmit.setOnClickListener { submission() }
    }


    private fun showLoader() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.showLoadingDialog("Loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }

    private fun submission() {
        val selectedCourseID =
            PickerManager.getTeacherCourses?.firstOrNull {
                it.name == selectedCourse
            }?.id?.toInt() ?: 0

        if (selectedDate.isEmpty()) {
            showToast(requireActivity(), "Please select a date")
            return
        }

        if (student.isNullOrEmpty()) {
            showToast(requireActivity(), "You can't proceed without any changes")
            return
        }

        lifecycleScope.launch {
            student.forEach {
                val response = withContext(Dispatchers.IO) {
                    viewModel.markAttendance(
                        attendanceModel = AttendanceModel(
                            USER_TYPE,
                            token!!,
                            selectedClass,
                            selectedCourseID.toString(),
                            it,
                            selectedDate
                        )
                    )
                }

                logResponse(response)
            }
        }
    }

    private fun logResponse(response: Response<AttendanceResponse>) {
        if (response.isSuccessful) showToast(requireActivity(), response.body()!!.msg)
        else
            showToast(requireActivity(), "Some Issues: ${response.errorBody()!!}")
    }

    private fun setHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        },2500)
    }
}