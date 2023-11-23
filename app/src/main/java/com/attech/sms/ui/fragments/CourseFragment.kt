package com.attech.sms.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.sms.adapters.CourseDetailsAdapter
import com.attech.sms.databinding.FragmentAttendanceBinding
import com.attech.sms.models.StudentClassAndCourses
import com.attech.sms.network.RetrofitClientInstance
import com.attech.sms.repository.RetrofitRepository
import com.attech.sms.utils.LoadingDialog
import com.attech.sms.utils.MAIN_MENU
import com.attech.sms.utils.PickerManager.batchClass
import com.attech.sms.utils.PickerManager.token
import com.attech.sms.utils.PickerManager.userId
import com.attech.sms.utils.USER_TYPE
import com.attech.sms.viewmodel.RetrofitViewModel
import com.attech.sms.viewmodelfactory.RetrofitViewModelFactory


class CourseFragment : Fragment() {
    private lateinit var binding: FragmentAttendanceBinding
    private lateinit var viewModel: RetrofitViewModel
    var myClass: String? = null
    private var loaderShown = false
    private var title: String = ""

    private lateinit var loadingDialog: LoadingDialog
    private var argumentTitle: String? = null
    private  var courseDetailsAdapter = CourseDetailsAdapter()



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


        init()
        buttonClicks()
    }

    private fun buttonClicks() {
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }


    private fun init() {
        binding.smsText.text = title
        binding.recyclerViewAttendance.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = courseDetailsAdapter
        }
        binding.parentLL.visibility =View.GONE
        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]

        Log.d("Testinsx","${userId}")
        viewModel.getStudentClassAndCourses(
            StudentClassAndCourses(
                type = USER_TYPE,
                token = token!!,
                id= userId
            )
        )

        viewModel.studentClassAndCoursesResponse.observe(viewLifecycleOwner) {
            binding.classBatch.text = it.batchcode
            Log.d("Chekcbatch", "courses: ${it.courses}: ${it.batchcode}")
            courseDetailsAdapter.setCourseDetailsList(it.courses)

        }
       Handler(Looper.getMainLooper()).postDelayed({
           hideLoader()
       },4000)
    }

    private fun showLoader() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.showLoadingDialog("loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }
}