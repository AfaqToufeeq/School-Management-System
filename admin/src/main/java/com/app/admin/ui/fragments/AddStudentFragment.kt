package com.app.admin.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.admin.databinding.FragmentAddStudentBinding
import com.app.admin.models.Student
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.MAIN_MENU
import com.app.admin.utils.PickerManager.token
import com.app.admin.utils.USER_TYPE
import com.app.admin.utils.Utils.showToast
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory

class AddStudentFragment : Fragment() {

    private lateinit var binding: FragmentAddStudentBinding
    private var argumentTitle: String? = null
    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = requireArguments().getString(MAIN_MENU)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()
        setupEvents()
    }

    private fun initialize() {
        binding.smsText.text = argumentTitle

        pickSingleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleMediaPickerResult(result)
            }

        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel =
            ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
    }

    private fun setupEvents() {
        binding.apply {
            leftIcon.setOnClickListener {handleBackPressed()}

            uploadImageButton.setOnClickListener {
                launchMediaPicker()
            }

            addStudentButton.setOnClickListener {
                addStudent()
            }
        }
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
            binding.studentIcon.setImageURI(uri)
        } else {
            showToast(requireActivity(), "Failed picking media.")
        }
    }

    private fun addStudent() {
        val type = USER_TYPE
        val authToken = token
        val firstName = binding.firstNameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()
        val rollNo = binding.rollNoEditText.text.toString()
        val contact = binding.contactEditText.text.toString()
        val nic = binding.nicEditText.text.toString()
        val address = binding.addressEditText.text.toString()
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val student = Student(
            type,
            authToken!!,
            firstName,
            lastName,
            rollNo,
            contact,
            nic,
            address,
            username,
            password
        )

        viewModel.addStudent(type, authToken, student)

        viewModel.addStudentResult.observe(viewLifecycleOwner) { studentResponse ->
            if (studentResponse!!) {
                showToast(requireActivity(), "Student added successfully")
                handleBackPressed()
            } else {
                showToast(requireActivity(), "Failed to add student")
            }
        }
    }

}
