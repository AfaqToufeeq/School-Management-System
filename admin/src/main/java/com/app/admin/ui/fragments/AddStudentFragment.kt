package com.app.admin.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        init()
        events()
    }

    private fun init() {
        binding.toolbar.smsText.text = argumentTitle
        pickSingleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = result.data?.data
                    binding.studentIcon.setImageURI(uri)
                } else {
                    Toast.makeText(requireActivity(), "Failed picking media.", Toast.LENGTH_SHORT).show()
                }
            }

        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]

    }

    private fun events() {
        binding.apply {
            toolbar.leftIcon.setOnClickListener { findNavController().popBackStack() }

            uploadImageButton.setOnClickListener {
                pickSingleMediaLauncher.launch(Intent(Intent.ACTION_PICK).setType("image/*"))
            }

            addStudentButton.setOnClickListener {
//                val type = USER_TYPE
//                val authToken = token
//                val firstName = firstNameEditText.text.toString()
//                val lastName = lastNameEditText.text.toString()
//                val rollNo = rollNoEditText.text.toString()
//                val contact = contactEditText.text.toString()
//                val nic = nicEditText.text.toString()
//                val address = addressEditText.text.toString()
//                val username = usernameEditText.text.toString()
//                val password = passwordEditText.text.toString()

                val type = "admin" // Replace with the actual user type
                val authToken = token
                Log.d("API_ERROR","TOken $token")
                val firstName = "John"
                val lastName = "Doe"
                val rollNo = "12345"
                val contact = "1234567890"
                val nic = "ABC123456"
                val address = "123 Main Street, City"
                val username = "john_doe"
                val password = "secretpassword"

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
                viewModel.addStudent(type, authToken!!,student)

                viewModel.addStudentResult.observe(viewLifecycleOwner) { studentResponse ->
                    if (studentResponse!=null) {
                        showToast(requireActivity(), "Student added successfully")
                    } else {
                        showToast(requireActivity(), "Failed to add student")
                    }
                }
            }
        }
    }
}
