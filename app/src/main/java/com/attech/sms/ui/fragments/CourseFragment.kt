package com.attech.sms.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.sms.R
import com.attech.sms.adapters.CourseDetailsAdapter
import com.attech.sms.databinding.FragmentAttendanceBinding
import com.attech.sms.models.CourseData
import com.attech.sms.network.RetrofitClientInstance
import com.attech.sms.repository.RetrofitRepository
import com.attech.sms.utils.LoadingDialog
import com.attech.sms.utils.MAIN_MENU
import com.attech.sms.viewmodel.RetrofitViewModel
import com.attech.sms.viewmodelfactory.RetrofitViewModelFactory


class CourseFragment : Fragment() {
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var viewModel: RetrofitViewModel
    private var argumentTitle: String? = null
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var courseDetailsAdapter: CourseDetailsAdapter
    private var loaderShown = false
    private var batchCode: String? = null


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
    ): View? {
        binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        setAdapter()
        buttonClicks()
        setObserver()
        setHandler()
    }

    private fun initializeViews() {
        binding.parentLL.visibility =View.GONE

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]
    }

    private fun setAdapter() {
        binding.recyclerViewAttendance.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            courseDetailsAdapter = CourseDetailsAdapter( viewModel ) {courseId, courseCode, courseName, teacherName, subjectTotalMarks ->
                    if (argumentTitle == "Marks") {
                        navigateToNextScreen(
                            CourseData(
                                courseId = courseId,
                                courseCode = courseCode,
                                courseName = courseName,
                                teacherName = teacherName,
                                subjectTotalMarks = subjectTotalMarks,
                                batchCode = batchCode!!
                            )
                        )
                    }

            }
            adapter = courseDetailsAdapter
        }
    }

    private fun navigateToNextScreen(courseData: CourseData) {
        val bundle = Bundle().apply {
            putParcelable("courseData", courseData)
        }
        findNavController().navigate(R.id.action_courseFragment_to_testMarksFragment, bundle)
    }

    private fun setObserver() {
        setStudentClassAndCoursesObserver()
    }

    private fun setStudentClassAndCoursesObserver() {
        viewModel.studentClassAndCoursesResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                with(binding) {
                    classBatch.text = it.batchcode
                    batchCode = it.batchcode
                    courseDetailsAdapter.setCourseDetailsList(it.courses)
                }
            }
        }
    }

    private fun buttonClicks() {
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        },2500)
    }

    private fun showLoader() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.showLoadingDialog("Loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }

}