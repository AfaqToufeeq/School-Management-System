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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.sms.adapters.CourseDetailsAdapter
import com.attech.sms.adapters.TestMarksAdapter
import com.attech.sms.databinding.FragmentAttendanceBinding
import com.attech.sms.databinding.FragmentTestMarksBinding
import com.attech.sms.models.StudentClassAndCourses
import com.attech.sms.models.TestMark
import com.attech.sms.models.TestMarksRequest
import com.attech.sms.models.TestMarksResponse
import com.attech.sms.network.RetrofitClientInstance
import com.attech.sms.repository.RetrofitRepository
import com.attech.sms.utils.LoadingDialog
import com.attech.sms.utils.MAIN_MENU
import com.attech.sms.utils.PickerManager
import com.attech.sms.utils.PickerManager.token
import com.attech.sms.utils.PickerManager.userId
import com.attech.sms.utils.USER_TYPE
import com.attech.sms.viewmodel.RetrofitViewModel
import com.attech.sms.viewmodelfactory.RetrofitViewModelFactory

class TestMarksFragment : Fragment() {

    private var _binding: FragmentTestMarksBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RetrofitViewModel
    var myClass: String? = null
    private var loaderShown = false
    private var title: String = ""
    private lateinit var loadingDialog: LoadingDialog
    private var argumentTitle: String? = null
    private  var courseDetailsAdapter = CourseDetailsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(MAIN_MENU) ?: ""
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

        init()
        setObserers()
    }

    private fun setObserers() {

        Log.d("Testinsx","$userId")
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


        }

        viewModel.getMarks(TestMarksRequest(
            type = USER_TYPE,
            token = token!!,
            course = 1,
            student = 1,
            bcode = "Class 5"
        ))

        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        },4000)


        viewModel.testMarks.observe(viewLifecycleOwner) {
            Log.d("checkMarks","Score: ${it.score}")
        }

    }

    private fun init() {
        binding.smsText.text = title
        val adapter = TestMarksAdapter()
        binding.recyclerViewTestMarks.adapter = adapter
        binding.recyclerViewTestMarks.layoutManager = LinearLayoutManager(requireContext())

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]
    }

    private fun showLoader() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.showLoadingDialog("loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }
}
