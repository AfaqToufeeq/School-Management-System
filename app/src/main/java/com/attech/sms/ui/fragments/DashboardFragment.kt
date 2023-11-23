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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.sms.R
import com.attech.sms.adapters.DashboardAdapter
import com.attech.sms.adapters.NewsAdapter
import com.attech.sms.callbacks.OnItemClick
import com.attech.sms.databinding.FragmentDashboardBinding
import com.attech.sms.models.StudentClassAndCourses
import com.attech.sms.network.RetrofitClientInstance
import com.attech.sms.repository.RetrofitRepository
import com.attech.sms.repository.StudentRepository
import com.attech.sms.utils.ImageUtil
import com.attech.sms.utils.LoadingDialog
import com.attech.sms.utils.MAIN_MENU
import com.attech.sms.utils.PickerManager
import com.attech.sms.utils.PickerManager.allBatchesList
import com.attech.sms.utils.PickerManager.batchClass
import com.attech.sms.utils.PickerManager.studentData
import com.attech.sms.utils.PickerManager.userId
import com.attech.sms.utils.USER_TYPE
import com.attech.sms.viewmodel.RetrofitViewModel
import com.attech.sms.viewmodel.StudentViewModel
import com.attech.sms.viewmodelfactory.RetrofitViewModelFactory
import com.attech.sms.viewmodelfactory.StudentViewModelFactory
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class DashboardFragment : Fragment(), OnItemClick {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var dashboardAdapter: DashboardAdapter
    private lateinit var newsAdapter: NewsAdapter
    private val timer = Timer()
    private lateinit var viewModel: RetrofitViewModel
    private var loaderShown = false
    private var title: String = ""
    private lateinit var loadingDialog: LoadingDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setObservers()
        setRecyclerView()
        events()
    }

    private fun events() {
        binding.profileCL.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment,
                Bundle().apply { putString(MAIN_MENU, "Profile View")})
        }
    }

    private fun init() {
        val repository = StudentRepository()
        studentViewModel = ViewModelProvider(this, StudentViewModelFactory(repository))[StudentViewModel::class.java]

            val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
            viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]

    }

    private fun setObservers() {
        studentViewModel.apply {
            dashboardItemsLiveData.observe(viewLifecycleOwner) {
                dashboardAdapter.submitList(it)
            }

            newsItemsLiveData.observe(viewLifecycleOwner) {
                newsAdapter.submitList(it)
            }
        }

        with(viewModel) {
            fetchStudents(USER_TYPE, PickerManager.token!!)
            fetchBatches(USER_TYPE, PickerManager.token!!)

            students.observe(viewLifecycleOwner) { students ->
                studentData = students.firstOrNull { it.username == PickerManager.userName }
                studentData?.let {
                    userId = it.id
                    if (it.image!=null)
                        binding.personImageView.setImageBitmap(ImageUtil.decodeBase64ToBitmap(it.image))
                    else
                        binding.personImageView.setImageResource(R.drawable.profile_icon)
                    binding.personNameTextView.text = "${it.firstname} ${it.lastname}"
                }
            }

            Log.d("Testinsx","${userId}")
            viewModel.getStudentClassAndCourses(
                StudentClassAndCourses(
                type = USER_TYPE,
                token = PickerManager.token!!,
                id= userId
                )
            )

            viewModel.studentClassAndCoursesResponse.observe(viewLifecycleOwner) {
                batchClass = it.batchcode
                Log.d("Testinsx","${batchClass}")

            }
            allBatches.observe(viewLifecycleOwner) { batches ->
                if (batches != null) {
                    allBatchesList = batches
                    Log.d("fetchBatches", "Success")
                } else {
                    Log.d("fetchBatches", "Failed")
                }
            }
        }
    }

    private fun setTimerToScroll(layoutManager: LinearLayoutManager) {
        // Schedule automatic scrolling every 5 seconds
        timer.scheduleAtFixedRate(5 * 1000, 5 * 1000) {
            requireActivity().runOnUiThread {
                val currentPosition = layoutManager.findFirstVisibleItemPosition()
                val nextPosition = if (currentPosition < layoutManager.itemCount - 1) currentPosition + 1 else 0
                binding.viewPagerDashboard.smoothScrollToPosition(nextPosition)
            }
        }
    }

    private fun setRecyclerView() {
        binding.apply {
            dashboardRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)

            setTimerToScroll(
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,
                    false
                ).also { viewPagerDashboard.layoutManager = it }
            )

            dashboardAdapter = DashboardAdapter(this@DashboardFragment)
            newsAdapter = NewsAdapter()
            viewPagerDashboard.adapter = newsAdapter
            dashboardRecyclerView.adapter = dashboardAdapter
        }

    }


    private fun showLoader() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.showLoadingDialog("loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
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
                "Attendance" -> navigate(R.id.action_dashboardFragment_to_attendanceFragment, bundle)
                "Courses" -> navigate(R.id.action_dashboardFragment_to_courseFragment, bundle)
                "Past Papers" -> navigate(R.id.action_dashboardFragment_to_pastPapersFragment, bundle)
                "Performance" -> navigate(R.id.action_dashboardFragment_to_attendanceFragment, bundle)
                "Marks" -> navigate(R.id.action_dashboardFragment_to_testMarksFragment, bundle)
                "Fee Status" -> navigate(R.id.action_dashboardFragment_to_attendanceFragment, bundle)
            }
        }
    }
}