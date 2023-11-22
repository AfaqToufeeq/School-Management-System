package com.app.admin.ui.fragments

import com.app.admin.utils.ImageUtil.convertUriToBase64
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
import com.app.admin.databinding.FragmentAddTeacherBinding
import com.app.admin.models.Teacher
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.MAIN_MENU
import com.app.admin.utils.PickerManager
import com.app.admin.utils.USER_TYPE
import com.app.admin.utils.Utils
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory


class AddTeacherFragment : Fragment() {

    private lateinit var binding: FragmentAddTeacherBinding
    private var argumentTitle: String? = null
    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: RetrofitViewModel
    private var base64ImageString: String? = null


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

            addTeacherButton.setOnClickListener {
                requireActivity().runOnUiThread {
                    showLoading(true)
                }
                addTeacher()
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
            base64ImageString = uri?.let { convertUriToBase64(requireActivity(), it) }
            if (base64ImageString!=null)
                binding.teacherIcon.setImageURI(uri)
        } else {
            Utils.showToast(requireActivity(), "Failed picking media.")
        }
    }

    private fun addTeacher() {
        val type = USER_TYPE
        val authToken = PickerManager.token
        val firstName = binding.firstNameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()
        val contact = binding.contactEditText.text.toString()
        val nic = binding.nicEditText.text.toString()
        val address = binding.addressEditText.text.toString()
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val teacher = Teacher(
            type,
            authToken!!,
            firstName,
            lastName,
            contact,
            nic,
            address,
            username,
            password,
            base64ImageString
        )

        viewModel.addTeacher(type, authToken, teacher)

        viewModel.addTeacherResult.observe(viewLifecycleOwner) { response ->
            if (response!!) {
                Utils.showToast(requireActivity(), "Teacher added successfully")
                handleBackPressed()
            } else {
                Utils.showToast(requireActivity(), "Failed to add teacher")
            }
            showLoading(false)
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
