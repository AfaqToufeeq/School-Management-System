package com.attech.teacher.ui.fragments

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
import com.attech.teacher.R
import com.attech.teacher.adapters.DashboardAdapter
import com.attech.teacher.adapters.NewsAdapter
import com.attech.teacher.callbacks.OnItemClick
import com.attech.teacher.databinding.FragmentDashboardBinding
import com.attech.teacher.models.TeacherClasses
import com.attech.teacher.models.TeacherDetailsResponse
import com.attech.teacher.network.RetrofitClientInstance
import com.attech.teacher.repository.RetrofitRepository
import com.attech.teacher.repository.TeacherRepository
import com.attech.teacher.utils.ImageUtil
import com.attech.teacher.utils.LoadingDialog
import com.attech.teacher.utils.MAIN_MENU
import com.attech.teacher.utils.PickerManager
import com.attech.teacher.utils.PickerManager.allBatchesList
import com.attech.teacher.utils.PickerManager.getTeacherCourses
import com.attech.teacher.utils.PickerManager.userIdData
import com.attech.teacher.utils.USER_TYPE
import com.attech.teacher.viewmodel.RetrofitViewModel
import com.attech.teacher.viewmodel.TeacherViewModel
import com.attech.teacher.viewmodelfactory.RetrofitViewModelFactory
import com.attech.teacher.viewmodelfactory.TeacherViewModelFactory
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class DashboardFragment : Fragment(), OnItemClick {
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var studentViewModel: TeacherViewModel
    private lateinit var dashboardAdapter: DashboardAdapter
    private lateinit var newsAdapter: NewsAdapter
    private val timer = Timer()
    private lateinit var viewModel: RetrofitViewModel
    private var loaderShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setRecyclerView()
        setEvents()
    }

    private fun setEvents() {
        binding.profileCL.setOnClickListener {
            findNavController().navigate(
                R.id.action_dashboardFragment_to_profileFragment,
                Bundle().apply { putString(MAIN_MENU, "Profile View") }
            )
        }
    }

    private fun setObservers() {
        setViewModelObservers()
        setStudentViewModelObservers()
    }

    private fun setStudentViewModelObservers() {
        studentViewModel.apply {
            dashboardItemsLiveData.observe(viewLifecycleOwner) { dashboardAdapter.submitList(it) }
            newsItemsLiveData.observe(viewLifecycleOwner) { newsAdapter.submitList(it) }
        }
    }


    private fun setViewModelObservers() {
        setTeacherDataObserver()
        setAllBatchesObserver()
        setTeacherCoursesObserver()
        setLocalTeacherDataObserver()
    }

    private fun setLocalTeacherDataObserver() {
        PickerManager.liveTeacherData.observe(viewLifecycleOwner) { teacher ->
            updateViews(teacher!!)
            fetchTeacherCoursesBatchesData(teacher.id)
            getCourseTeacher(teacher.id)
            callHandler()

        }
    }

    private fun getCourseTeacher(id: Int) {
        viewModel.getCourseTeacher(USER_TYPE, PickerManager.token!!, id)
    }

    private fun fetchTeacherCoursesBatchesData(id: Int) {
        viewModel.getTeacherClasses(
            TeacherClasses(
                type = USER_TYPE,
                token = PickerManager.token!!,
                id= id
            )
        )
    }

    private fun setTeacherCoursesObserver() {
        with(viewModel) {
            teacherCourses.observe(viewLifecycleOwner) { teacherCourses ->
                if (teacherCourses != null) {
                    getTeacherCourses = teacherCourses
                    Log.d("getTeacherCourses", "Success")
                } else {
                    Log.d("getTeacherCourses", "Failed")
                }
            }
        }
    }

    private fun setAllBatchesObserver() {
        with(viewModel) {
            fetchBatches(USER_TYPE, PickerManager.token!!)
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

    private fun setTeacherDataObserver() {
        with(viewModel) {
            fetchTeachers(USER_TYPE, PickerManager.token!!)
            teachers.observe(viewLifecycleOwner) { teachers ->
                PickerManager.setTeacherData(teachers.firstOrNull { it.username == PickerManager.userName })
            }
        }
    }

    private fun updateViews(it: TeacherDetailsResponse) {
        with(binding) {
            personNameTextView.text = "${it.firstname} ${it.lastname}"
            if (it.image != null)
                personImageView.setImageBitmap(ImageUtil.decodeBase64ToBitmap(it.image))
            else
                personImageView.setImageResource(R.drawable.profile_icon)
        }
    }

    private fun initialize() {
        if (!loaderShown) {
            showLoader()
            loaderShown = true
        }
        val repository = TeacherRepository()
        studentViewModel =
            ViewModelProvider(
                this,
                TeacherViewModelFactory(repository)
            )[TeacherViewModel::class.java]

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel =
            ViewModelProvider(
                requireActivity(),
                RetrofitViewModelFactory(retrofitRepository)
            )[RetrofitViewModel::class.java]
    }

    private fun showLoader() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.showLoadingDialog("loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }

    private fun setTimerToScroll(layoutManager: LinearLayoutManager) {
        // Schedule automatic scrolling every 5 seconds
        timer.scheduleAtFixedRate(5 * 1000, 5 * 1000) {
            requireActivity().runOnUiThread {
                val currentPosition = layoutManager.findFirstVisibleItemPosition()
                val nextPosition =
                    if (currentPosition < layoutManager.itemCount - 1) currentPosition + 1 else 0
                binding.viewPagerDashboard.smoothScrollToPosition(nextPosition)
            }
        }
    }

    private fun setRecyclerView() {
        binding.apply {
            dashboardRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)

            setTimerToScroll(
                LinearLayoutManager(
                    requireActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                ).also { viewPagerDashboard.layoutManager = it }

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
                "Mark Attendance" -> navigate(
                    R.id.action_dashboardFragment_to_attendanceFragment,
                    bundle
                )
//                "View Students" -> navigate(R.id.action_dashboardFragment_to_viewStudentsFragment, bundle)
                "Upload Marks" -> navigate(
                    R.id.action_dashboardFragment_to_uploadMarksFragment,
                    bundle
                )
            }
        }
    }

    private fun callHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        }, 1500)
    }
}