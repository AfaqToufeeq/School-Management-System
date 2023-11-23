package com.attech.teacher.ui.fragments

import android.R
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.attech.teacher.utils.PickerManager.allBatchesList
import com.attech.teacher.utils.PickerManager.token
import com.attech.teacher.utils.USER_TYPE
import com.attech.teacher.utils.Utils.showToast
import com.attech.teacher.viewmodel.RetrofitViewModel
import com.attech.teacher.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Calendar


class AttendanceFragment : Fragment() {
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
        setupClassSpinner()
        observeViewModel()
        setEventListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeViews() {
        binding.smsText.text = arguments?.getString(MAIN_MENU)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(
            requireActivity(),
            RetrofitViewModelFactory(retrofitRepository)
        )[RetrofitViewModel::class.java]

        attendanceAdapter = AttendanceAdapter { model, position, isChecked ->
            if (student.contains(model.id)) {
                student.remove(model.id)
            } else {
                student.add(model.id)
            }
        }
        binding.recyclerView.adapter = attendanceAdapter

    }

    private fun setupClassSpinner() {
        val batchCodes = allBatchesList!!.map { it.batchcode }
        batchCodes?.let {
            val classAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, it)
            classAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spinnerClass.adapter = classAdapter
        }

        loadCourseSpinner()

        binding.spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedClass = (parent?.getItemAtPosition(position) as? String)!!
                selectedClass?.let { viewModel.getBatchStudents(USER_TYPE, token!!, selectedClass) }
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

    private fun loadCourseSpinner() {
        PickerManager.getTeacherCourses?.let { course->
            val teacherCourses = course.map { it.name }
            teacherCourses?.let {
                val classAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, it)
                classAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                binding.spinnerCourses.adapter = classAdapter
            }
        }
    }


    private fun observeViewModel() {
        viewModel.batchStudents.observe(viewLifecycleOwner) { batches ->
            Log.d("checBat","batches-> ${batches.size}:: ${batches[0]}")
            if (batches.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1500)
                    showLoading(false)
                    binding.recyclerView.visibility = View.GONE
                    showToast(requireActivity(), "No Student is registered in this class")
                }
                Log.d("ViewStudentsFragment", "Batches list is empty $batches")
            } else {
                showLoading(false)
                binding.recyclerView.visibility = View.VISIBLE
                attendanceAdapter.setStudents(batches)
                Log.d("ViewStudentsFragment", "Batches list size: ${batches.size} ")
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
//            setupClassSpinner()
            hideLoader()
        },4000)
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
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
        loadingDialog.showLoadingDialog("loading, Please wait...")
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
        if (response.isSuccessful) {
            showToast(requireActivity(), response.body()!!.msg)
        } else {
            showToast(requireActivity(), "Some Issues: ${response.errorBody()!!}")
        }

    }
}