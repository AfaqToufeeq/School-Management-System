package com.attech.teacher.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.teacher.R
import com.attech.teacher.adapters.DashboardAdapter
import com.attech.teacher.adapters.NewsAdapter
import com.attech.teacher.callbacks.OnItemClick
import com.attech.teacher.databinding.FragmentDashboardBinding
import com.attech.teacher.models.BatchesModel
import com.attech.teacher.network.RetrofitClientInstance
import com.attech.teacher.repository.RetrofitRepository
import com.attech.teacher.repository.TeacherRepository
import com.attech.teacher.utils.ImageUtil
import com.attech.teacher.utils.MAIN_MENU
import com.attech.teacher.utils.PickerManager
import com.attech.teacher.utils.PickerManager.allBatchesList
import com.attech.teacher.utils.PickerManager.batchCodes
import com.attech.teacher.utils.PickerManager.getTeacherCourses
import com.attech.teacher.utils.PickerManager.teacherData
import com.attech.teacher.utils.PickerManager.userId
import com.attech.teacher.utils.USER_TYPE
import com.attech.teacher.viewmodel.RetrofitViewModel
import com.attech.teacher.viewmodel.TeacherViewModel
import com.attech.teacher.viewmodelfactory.RetrofitViewModelFactory
import com.attech.teacher.viewmodelfactory.TeacherViewModelFactory
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class DashboardFragment : Fragment(), OnItemClick {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var studentViewModel: TeacherViewModel
    private lateinit var dashboardAdapter: DashboardAdapter
    private lateinit var newsAdapter: NewsAdapter
    private val timer = Timer()
    private lateinit var viewModel: RetrofitViewModel


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

    private fun setObservers() {
        studentViewModel.apply {
            dashboardItemsLiveData.observe(viewLifecycleOwner) { dashboardAdapter.submitList(it) }
            newsItemsLiveData.observe(viewLifecycleOwner) { newsAdapter.submitList(it) }
        }

        with(viewModel) {
            fetchTeachers(USER_TYPE, PickerManager.token!!)
            teachers.observe(viewLifecycleOwner) { teachers ->
                teacherData = teachers.firstOrNull { it.username == PickerManager.userName }
                teacherData?.let {
                    with(binding) {
                        if (it.image!=null)
                            personImageView.setImageBitmap(ImageUtil.decodeBase64ToBitmap(it.image))
                        else
                            personImageView.setImageResource(R.drawable.profile_icon)
                        personNameTextView.text = "${it.firstname} ${it.lastname}"
                        userId = it.id
                    }
                }
            }

            fetchBatches(USER_TYPE, PickerManager.token!!)
            allBatches.observe(viewLifecycleOwner) { batches ->
                if (batches!=null)
                {
                    allBatchesList = batches
                    Log.d("fetchBatches","Success")
                }
                else
                    Log.d("fetchBatches","Failed")
            }


            getCourseTeacher(USER_TYPE, PickerManager.token!!, userId)
            teacherCourses.observe(viewLifecycleOwner) { teacherCourses ->
                if (teacherCourses!=null)
                {
                    getTeacherCourses = teacherCourses
                    Log.d("getTeacherCourses","Success")
                }
                else
                    Log.d("getTeacherCourses","Failed")
            }

        }
    }

    private fun init() {
        val repository = TeacherRepository()
        studentViewModel = ViewModelProvider(this, TeacherViewModelFactory(repository))[TeacherViewModel::class.java]

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]

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
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                    .also { viewPagerDashboard.layoutManager = it }
            )

            dashboardAdapter = DashboardAdapter(this@DashboardFragment)
            newsAdapter = NewsAdapter()
            viewPagerDashboard.adapter = newsAdapter
            dashboardRecyclerView.adapter = dashboardAdapter
        }
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
                "Mark Attendance" -> navigate(R.id.action_dashboardFragment_to_attendanceFragment, bundle)
                "View Students" -> navigate(R.id.action_dashboardFragment_to_viewStudentsFragment, bundle)
                "Upload Marks" -> navigate(R.id.action_dashboardFragment_to_uploadMarksFragment, bundle)
            }
        }
    }
}