package com.attech.sms.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.sms.R
import com.attech.sms.adapters.AttendanceAdapter
import com.attech.sms.callbacks.OnItemClick
import com.attech.sms.databinding.FragmentAttendanceBinding
import com.attech.sms.models.GetAttendanceModelResponse
import com.attech.sms.models.GetCourse
import com.attech.sms.network.RetrofitClientInstance
import com.attech.sms.repository.RetrofitRepository
import com.attech.sms.utils.LoadingDialog
import com.attech.sms.utils.MAIN_MENU
import com.attech.sms.utils.PickerManager.token
import com.attech.sms.utils.USER_TYPE
import com.attech.sms.viewmodel.RetrofitViewModel
import com.attech.sms.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class AttendanceFragment : Fragment(), OnItemClick {
    private var binding: FragmentAttendanceBinding? = null
    private var selectedDate: String = ""
    private var title: String = ""
    private var loaderShown = false
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var viewModel: RetrofitViewModel
    private lateinit var attendanceAdapter: AttendanceAdapter
    private var filteredData: List<GetAttendanceModelResponse> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(MAIN_MENU) ?: ""
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
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        setAdapter()
        fetchCoursesData()
        buttonClicks()
        setObservers()
        setHandler()

    }

    private fun initializeViews() {
        binding!!.smsText.text = title
        binding!!.parentLL.visibility = View.VISIBLE
        binding!!.recyclerViewAttendance.layoutManager = LinearLayoutManager(requireActivity())

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]
    }

    private fun setAdapter() {
        attendanceAdapter = AttendanceAdapter(viewModel)
        binding!!.recyclerViewAttendance.adapter = attendanceAdapter
    }

    private fun fetchCoursesData() {
        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                viewModel.getCourses(GetCourse(
                    type = USER_TYPE,
                    token = token!!
                ))
            }
            Log.d("checkCoursesResponse", "Courses: ${response.body()}")
        }


        /*        val courseName = viewModel.getCourses(GetCourse(
                    type = USER_TYPE,
                    token = token!!,
                    id = 1
                ))*/


        /*        val batchCodes = PickerManager.allBatchesList!!.map { it.batchcode }
                lifecycleScope.launch {
                    batchCodes.forEach { batch->
                      viewModel.getBatchStudents(USER_TYPE, PickerManager.token!!, batch)
                    }
                }*/
    }

    private fun setObservers() {
        setStudentClassAndCoursesObserver()
        setAttendanceObserver()
    }

    private fun setStudentClassAndCoursesObserver() {
        viewModel.studentClassAndCoursesResponse.observe(viewLifecycleOwner) {
            binding!!.classBatch.text =it.batchcode
        }
    }

    private fun setAttendanceObserver() {
        viewModel.getAttendance.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                attendanceAdapter.setAttendanceList(it)
                filteredData = it
            }
        }
    }

    private fun buttonClicks() {
        binding!!.datePickerButton.setOnClickListener { showDatePicker() }
        binding!!.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = String.format(
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1, // Month is 0-based
                    selectedDay
                )
                binding!!.selectedDate.text = selectedDate
                filterRecyclerView(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun filterRecyclerView(selectedDate: String) {
        attendanceAdapter.filterByDate(selectedDate)
        val filteredData = getFilteredData(selectedDate)
        if (filteredData.isEmpty()) {
            Toast.makeText(requireContext(), "No records found for $selectedDate", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFilteredData(selectedDate: String): List<GetAttendanceModelResponse> {
        return filteredData.filter { it.date == selectedDate }
    }


    override fun clickListener(position: Int, value: String) {
        openFragment(value)
    }

    private fun openFragment(title: String) {
        val bundle = Bundle().apply {
            putString(MAIN_MENU, title)
        }

        findNavController().apply {
            when (title) {
                "Marks" -> navigate(R.id.action_dashboardFragment_to_testMarksFragment, bundle)
            }
        }
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
        },5000)
    }
}