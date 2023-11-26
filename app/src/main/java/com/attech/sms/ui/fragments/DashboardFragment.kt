package com.attech.sms.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.attech.sms.models.GetAttendanceModel
import com.attech.sms.models.GetCourse
import com.attech.sms.models.GetCourseResponse
import com.attech.sms.models.StudentClassAndCourses
import com.attech.sms.models.StudentDetailsResponse
import com.attech.sms.models.TeacherDetailsResponse
import com.attech.sms.network.RetrofitClientInstance
import com.attech.sms.repository.RetrofitRepository
import com.attech.sms.repository.StudentRepository
import com.attech.sms.utils.ImageUtil
import com.attech.sms.utils.LoadingDialog
import com.attech.sms.utils.MAIN_MENU
import com.attech.sms.utils.PickerManager
import com.attech.sms.utils.PickerManager.token
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
    private lateinit var loadingDialog: LoadingDialog


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
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment,
                Bundle().apply { putString(MAIN_MENU, "Profile View")})
        }
    }

    private fun initialize() {
        if (!loaderShown) {
            showLoader()
            loaderShown = true
        }
        val repository = StudentRepository()
        studentViewModel = ViewModelProvider(this, StudentViewModelFactory(repository))[StudentViewModel::class.java]

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]

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
        setStudentDataObserver()
        setLocalStudentDataObserver()
        fetchAllCourseData()
    }

    private fun fetchAllCourseData() {
        viewModel.getCourses(
            GetCourse(
                type = USER_TYPE,
                token = token!!
            )
        )

        viewModel.getCourses.observe(viewLifecycleOwner) { courses->
            fetchCourseTeachers(courses)
        }
    }

    private fun fetchCourseTeachers(courses: List<GetCourseResponse>?) {
        courses?.forEach {course->
            viewModel.getCourseTeachers(
                type = USER_TYPE,
                token = token!!,
                course = course.id
            )
        }

        viewModel.courseTeachers.observe(viewLifecycleOwner) { teacher ->
            fetchTeacherCourses(teacher)
        }
    }

    private fun fetchTeacherCourses(teachers: List<TeacherDetailsResponse>?) {
        teachers?.forEach { teacher->
            viewModel.getTeacherCourses(
                type = USER_TYPE,
                token = token!!,
                teacher = teacher.id
            )
        }
    }

    private fun setLocalStudentDataObserver() {
        PickerManager.liveStudentData.observe(viewLifecycleOwner) { student ->
            updateViews(student!!)
            fetchStudentClassesAndCoursesObserver(student.id)
        }
    }

    private fun fetchStudentClassesAndCoursesObserver(studentId: Int) {
        viewModel.getStudentClassAndCourses(
            StudentClassAndCourses(
                type = USER_TYPE,
                token = token!!,
                id= studentId
            )
        )

        viewModel.studentClassAndCoursesResponse.observe(viewLifecycleOwner) { batch->
            fetchAttendanceData(batch.batchcode, studentId )
        }
    }

    private fun fetchAttendanceData(batchCode: String, studentId: Int) {
        viewModel.getAttendance(
            GetAttendanceModel(
                type = USER_TYPE,
                token = token!!,
                bcode = batchCode,
                student = studentId
            )
        )

        viewModel.getAttendance.observe(viewLifecycleOwner) {
            callHandler()
        }
    }

    private fun setStudentDataObserver() {
        with(viewModel) {
            fetchStudents(USER_TYPE, token!!)
            students.observe(viewLifecycleOwner) { students ->
                PickerManager.setStudentData(students.firstOrNull { it.username == PickerManager.userName })
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateViews(it: StudentDetailsResponse) {
        if (it.image!=null)
            binding.personImageView.setImageBitmap(ImageUtil.decodeBase64ToBitmap(it.image))
        else
            binding.personImageView.setImageResource(R.drawable.profile_icon)
        binding.personNameTextView.text = "${it.firstname} ${it.lastname}"
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
        loadingDialog.showLoadingDialog("Loading, Please wait...")
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
                "Marks" -> navigate(R.id.action_dashboardFragment_to_courseFragment, bundle)
                "Fee Status" -> navigate(R.id.action_dashboardFragment_to_attendanceFragment, bundle)
            }
        }
    }

    private fun callHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        }, 1500)
    }

}