package com.app.admin.ui.fragments

import com.app.admin.utils.ImageUtil.convertUriToBase64
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.admin.R
import com.app.admin.databinding.FragmentAddTeacherBinding
import com.app.admin.models.BatchStudents
import com.app.admin.models.GetCourseResponse
import com.app.admin.models.Teacher
import com.app.admin.models.TeacherCourseModel
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.ImageUtil
import com.app.admin.utils.MAIN_MENU
import com.app.admin.utils.PickerManager
import com.app.admin.utils.PickerManager.token
import com.app.admin.utils.USER_TYPE
import com.app.admin.utils.Utils
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray


class AddTeacherFragment : Fragment() {

    private lateinit var binding: FragmentAddTeacherBinding
    private var argumentTitle: String? = null
    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: RetrofitViewModel
    private var base64ImageString: String? = null
    private var teacherId: Int? = 0
    private var selectedCourseId: Int = 0
    private var coursesList: List<GetCourseResponse>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = requireArguments().getString(MAIN_MENU)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTeacherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bitmap = BitmapFactory.decodeResource(requireActivity().resources, R.drawable.person)
        base64ImageString = ImageUtil.bitmapToBase64(bitmap)

        initialize()
        setObservers()
        setupClassSpinner()
        setupEvents()
    }

    private fun initialize() {
        binding.smsText.text = argumentTitle

        pickSingleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleMediaPickerResult(result)
            }

        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
    }

    private fun setObservers() {
        courseObserver()
        addTeacherObserver()
    }

    private fun courseObserver() {
        viewModel.getCourses.observe(viewLifecycleOwner) { courses ->
            loadCoursesSpinner(courses.map { it.name })
            coursesList = courses
        }
    }

    private fun loadCoursesSpinner(courses: List<String>) {
        courses?.let {
            val classAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it)
            classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCourses.adapter = classAdapter
        }
    }

    private fun setupClassSpinner() {
        binding.spinnerCourses.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCourse = (parent?.getItemAtPosition(position) as? String)!!
                selectedCourseId = coursesList?.firstOrNull { it.name == selectedCourse }?.id!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun addTeacherObserver() {
        viewModel.addTeacherResult.observe(viewLifecycleOwner) { teacherResponse ->
            if (teacherResponse!=0) {
                teacherId = teacherResponse
                Handler(Looper.getMainLooper()).postDelayed({
                    showLoading(false)
                    setPageVisibility(true)
                }, 1000)
            } else {
                showLoading(false)
                Utils.showToast(requireActivity(), "Failed to add teacher")
            }
        }
    }

    private fun setupEvents() {
        binding.apply {
            leftIcon.setOnClickListener {handleBackPressed()}

            uploadImageButton.setOnClickListener { launchMediaPicker() }

            nextPageButton.setOnClickListener {
                requireActivity().runOnUiThread {
                    showLoading(true)
                    addTeacher()
                }
            }

            addTeacherButton.setOnClickListener {
                requireActivity().runOnUiThread {
                    showLoading(true)
                    addCourseTeacher()
                }
            }
        }
    }


    private fun addCourseTeacher() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    viewModel.addCourseTeacher(
                        TeacherCourseModel(
                            type = USER_TYPE,
                            token = token!!,
                            course = selectedCourseId,
                            teacher = teacherId!!
                        )
                    )
                }

                if (response.isSuccessful)
                    Utils.showToast(requireActivity(), "Teacher is added successfully")
                else
                    Utils.showToast(requireActivity(), "Failed to add teacher")

            } catch (e: Exception) {
                Utils.showToast(requireActivity(), "Failed to add: ${e.message}")
            } finally {
                showLoading(false)
                handleBackPressed()
            }
        }
    }

    private fun setPageVisibility(flag: Boolean) {
        binding.page1.visibility = if (flag) View.GONE else View.VISIBLE
        binding.page2.visibility = if (flag) View.VISIBLE else View.GONE
    }

    private fun handleBackPressed() {
        findNavController().popBackStack()
    }

    private fun launchMediaPicker() {
        pickSingleMediaLauncher.launch(Intent(Intent.ACTION_PICK).setType("image/*"))
    }

    private fun handleMediaPickerResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            base64ImageString = uri?.let { convertUriToBase64(requireActivity(), it) }
            if (base64ImageString!=null)
                binding.teacherIcon.setImageURI(uri)
        } else {
            Utils.showToast(requireActivity(), "Failed picking media.")
        }
    }

    private fun addTeacher() {
        val firstName = binding.firstNameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()
        val contact = binding.contactEditText.text.toString()
        val nic = binding.nicEditText.text.toString()
        val address = binding.addressEditText.text.toString()
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        viewModel.addTeacher(
            Teacher(
                USER_TYPE,
                token!!,
                firstName,
                lastName,
                contact,
                nic,
                address,
                username,
                password,
                base64ImageString
            ))
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

}
