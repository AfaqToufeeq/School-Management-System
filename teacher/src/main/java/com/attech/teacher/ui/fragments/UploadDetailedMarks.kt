package com.attech.teacher.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.attech.teacher.databinding.FragmentUploadDetailedMarksBinding
import com.attech.teacher.models.MarksData
import com.attech.teacher.models.UploadMarksResponse
import com.attech.teacher.network.RetrofitClientInstance
import com.attech.teacher.repository.RetrofitRepository
import com.attech.teacher.utils.MAIN_MENU
import com.attech.teacher.utils.PickerManager.token
import com.attech.teacher.utils.USER_TYPE
import com.attech.teacher.utils.Utils
import com.attech.teacher.viewmodel.RetrofitViewModel
import com.attech.teacher.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class UploadDetailedMarks : Fragment() {
    private lateinit var selectedClass: String
    private lateinit var selectedCourse: String
    private lateinit var name: String
    private lateinit var rollNo: String
    private var studentId: Int = 0
    private var selectedClassID: Int = 0
    private var selectedCourseID: Int = 0
    private lateinit var binding: FragmentUploadDetailedMarksBinding
    private lateinit var viewModel: RetrofitViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getIntentAttributes()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadDetailedMarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        setViews()
        setEventListeners()
    }


    private fun getIntentAttributes() {
        selectedCourseID = arguments?.getInt("selectedCourseID") ?: 0
        selectedClassID = arguments?.getInt("selectedClassID") ?: 0
        studentId = arguments?.getInt("studentId") ?: 0
        selectedCourse = arguments?.getString("selectedCourseName") ?: ""
        selectedClass = arguments?.getString("selectedClassName")?: ""
        rollNo = arguments?.getString("rollNo") ?: ""
        name = arguments?.getString("name") ?: ""
    }


    private fun uploadData() {

        val getScore = binding.editTextScore.text.toString()

        lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    viewModel.uploadMarks(
                        MarksData(
                            type = USER_TYPE,
                            token = token!!,
                            course = selectedCourseID,
                            student = studentId,
                            bcode = selectedClass,
                            score = getScore.toInt()
                        )
                    )
                }
            logResponse(response)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeViews() {
        binding.smsText.text = arguments?.getString(MAIN_MENU)

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]
    }

    private fun setViews() {
        binding.courseTV.text = selectedCourse
        binding.studentTV.text = name
        binding.rollNoTV.text = rollNo
        binding.bcodeTV.text = selectedClass
    }


    private fun logResponse(response: Response<UploadMarksResponse>) {
        if (response.isSuccessful) {
            Utils.showToast(requireActivity(), "Successfully Uploaded")
            backNavigation()
        } else {
            Utils.showToast(requireActivity(), "Some Issues: ${response.errorBody()!!}")
            Log.d("ErrorLogResponse", "Some Issues: ${response.errorBody()!!} ${response.code()}")
        }

    }

    private fun setEventListeners() {
        binding.leftIcon.setOnClickListener { backNavigation() }
        binding.buttonUpload.setOnClickListener { uploadData() }
    }

    private fun backNavigation() {
        findNavController().popBackStack()
    }


}