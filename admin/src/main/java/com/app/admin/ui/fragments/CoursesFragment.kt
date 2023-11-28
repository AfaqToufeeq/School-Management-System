package com.app.admin.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.admin.databinding.FragmentCoursesBinding
import com.app.admin.models.Course
import com.app.admin.models.CoursesResponse
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.LoadingDialog
import com.app.admin.utils.PickerManager.token
import com.app.admin.utils.USER_TYPE
import com.app.admin.utils.Utils
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class CoursesFragment : Fragment() {
    private lateinit var binding: FragmentCoursesBinding
    private var argumentTitle: String? = null
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var viewModel: RetrofitViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setEventListeners()
    }

    private fun init() {
        loadingDialog = LoadingDialog(requireActivity())
        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]
    }


    private fun addCourse() {
        val courseCode = binding.editCourseCode.text.toString()
        val courseName = binding.editCourseName.text.toString()
        val courseMarks = binding.editMaxmarks.text.toString()

        if (!courseCode.isNullOrEmpty() && !courseName.isNullOrEmpty() && !courseMarks.isNullOrEmpty()) {
            showLoader()
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    viewModel.addCourse(
                        Course(
                            type = USER_TYPE,
                            token = token!!,
                            code = courseCode,
                            name = courseName,
                            maxMarks = courseMarks.toInt()
                        )
                    )
                }
                logResponse(response)
            }
        }

    }

    private fun logResponse(response: Response<CoursesResponse>) {
        if (response.isSuccessful) {
            hideLoader()
            Utils.showToast(requireActivity(), "Successfully Added")
            backNavigation()
        } else {
            hideLoader()
            Utils.showToast(requireActivity(), "Some Issues: ${response.errorBody()!!}")
            Log.d("ErrorLogResponse", "Some Issues: ${response.errorBody()!!} ${response.code()}")
        }

    }

    private fun setEventListeners() {
        binding.leftIcon.setOnClickListener { backNavigation() }
        binding.buttonUpload.setOnClickListener { addCourse() }
    }

    private fun backNavigation() {
        findNavController().popBackStack()
    }

    private fun showLoader() {
        loadingDialog.showLoadingDialog("Adding...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }

    private fun setHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        }, 1500)
    }
}
