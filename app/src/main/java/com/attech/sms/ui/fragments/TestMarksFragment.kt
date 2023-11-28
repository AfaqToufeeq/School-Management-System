package com.attech.sms.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.sms.adapters.TestMarksAdapter
import com.attech.sms.databinding.FragmentTestMarksBinding
import com.attech.sms.models.CourseData
import com.attech.sms.models.TestMarksRequest
import com.attech.sms.models.TestMarksResponse
import com.attech.sms.network.RetrofitClientInstance
import com.attech.sms.repository.RetrofitRepository
import com.attech.sms.utils.LoadingDialog
import com.attech.sms.utils.PickerManager.token
import com.attech.sms.utils.PickerManager.userIdData
import com.attech.sms.utils.USER_TYPE
import com.attech.sms.viewmodel.RetrofitViewModel
import com.attech.sms.viewmodelfactory.RetrofitViewModelFactory

class TestMarksFragment : Fragment() {

    private lateinit var courseData: CourseData
    private var _binding: FragmentTestMarksBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RetrofitViewModel
    private var loaderShown = false
    private val testAdapter = TestMarksAdapter()
    private lateinit var loadingDialog: LoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleValues()
        if (!loaderShown) {
            showLoader()
            loaderShown = true
        }
    }

    private fun getBundleValues() {
        courseData = (arguments?.getParcelable("courseData") as? CourseData)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestMarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        fetchMarksData()
        setAdapter()
        setObservers()
        events()
    }

    private fun events() {
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }

    private fun initializeViews() {
        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]
    }

    private fun setAdapter() {
        with(binding.recyclerViewTestMarks) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = testAdapter
        }
    }

    private fun fetchMarksData() {
        viewModel.getMarks(
            TestMarksRequest(
                type = USER_TYPE,
                token = token!!,
                course = courseData.courseId,
                student = userIdData.value!!,
                bcode = courseData.batchCode
            )
        )
    }

    private fun setObservers() {
        setTestMarksObserver()
    }

    private fun setTestMarksObserver() {
        viewModel.testMarks.observe(viewLifecycleOwner) { testMarksResponse->
            val testMarksList = listOf(testMarksResponse).toMutableList()
            testAdapter.setTestMarksList(filterAssignmentMarks(testMarksList), courseData)
            setHandler()
        }
    }

    private fun filterAssignmentMarks(testMarksList: MutableList<TestMarksResponse>): MutableList<TestMarksResponse> {
        val modifiedList = mutableListOf<TestMarksResponse>()
        testMarksList.forEach { testMarksResponse ->
            val scoreString = testMarksResponse.score
            val scores = scoreString.split(", ") // Splitting the score string by ", "

            scores.forEachIndexed { index, score ->
                // Building a new TestMarksResponse for each assignment score
                val newTestMarksResponse = TestMarksResponse(
                    testMarksResponse.id,
                    testMarksResponse.student,
                    testMarksResponse.course,
                    score,
                    testMarksResponse.marked_by
                )

                modifiedList.add(newTestMarksResponse)
            }
        }
        return modifiedList
    }

    private fun setHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        },1500)
    }

    private fun showLoader() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.showLoadingDialog("Loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }
}
