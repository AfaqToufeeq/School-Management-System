package com.app.admin.ui.fragments

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
import com.app.admin.databinding.FragmentAddStudentBinding
import com.app.admin.models.BatchStudents
import com.app.admin.models.Student
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.ImageUtil
import com.app.admin.utils.MAIN_MENU
import com.app.admin.utils.PickerManager.token
import com.app.admin.utils.USER_TYPE
import com.app.admin.utils.Utils.showToast
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class AddStudentFragment : Fragment() {

    private lateinit var binding: FragmentAddStudentBinding
    private var argumentTitle: String? = null
    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: RetrofitViewModel
    private var base64ImageString: String? = null
    private var selectedClass: String = ""
    private var studentId: Int? = 0


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
        val bitmap = BitmapFactory.decodeResource(requireActivity().resources, R.drawable.person)
        base64ImageString = ImageUtil.bitmapToBase64(bitmap)

        initialize()
        setObservers()
        setupClassSpinner()
        setupEvents()
    }

    private fun initialize() {
        binding.smsText.text = argumentTitle
        pickSingleMediaLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
                handleMediaPickerResult(result)
            }

        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
    }

    private fun setObservers() {
        batchObserver()
        addStudentObserver()
    }

    private fun batchObserver() {
        viewModel.allBatches.observe(viewLifecycleOwner) { batches ->
            loadBatchesSpinner(batches.map { it.batchcode })
        }
    }

    private fun loadBatchesSpinner(batches: List<String>) {
        batches?.let {
            val classAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it)
            classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerClass.adapter = classAdapter
        }
    }

    private fun setupClassSpinner() {
        binding.spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedClass = (parent?.getItemAtPosition(position) as? String)!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun addStudentObserver() {
        viewModel.addStudentResult.observe(viewLifecycleOwner) { studentResponse ->
            if (studentResponse!=0) {
                studentId = studentResponse
                Handler(Looper.getMainLooper()).postDelayed({
                    showLoading(false)
                    setPageVisibility(true)
                }, 1000)
            } else {
                showLoading(false)
                showToast(requireActivity(), "Failed to add student")
            }
        }
    }

    private fun addBatchStudents() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    viewModel.addBatchStudents(
                        BatchStudents(
                            type = USER_TYPE,
                            token = token!!,
                            bcode = selectedClass,
                            students = JSONArray().put(studentId),
                            teachers = JSONArray()
                        )
                    )
                }

                if (response.isSuccessful)
                    showToast(requireActivity(), "Student is added successfully")
                else
                    showToast(requireActivity(), "Failed to add student")

            } catch (e: Exception) {
                showToast(requireActivity(), "Failed to add: ${e.message}")
            } finally {
                showLoading(false)
                handleBackPressed()
            }
        }
    }


    private fun setupEvents() = binding.apply {
        leftIcon.setOnClickListener { handleBackPressed() }
        uploadImageButton.setOnClickListener { launchMediaPicker() }

        nextPageButton.setOnClickListener {
            requireActivity().runOnUiThread {
                showLoading(true)
                addStudent()
            }
        }

        addStudentButton.setOnClickListener {
            requireActivity().runOnUiThread {
                showLoading(true)
                addBatchStudents()
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
            base64ImageString = uri?.let { ImageUtil.convertUriToBase64(requireActivity(), it) }
            if (base64ImageString!=null)
                binding.studentIcon.setImageURI(uri)
        } else {
            showToast(requireActivity(), "Failed picking media.")
        }
    }

    private fun addStudent() {
        val firstName = binding.firstNameEditText.text.toString()
        val lastName = binding.lastNameEditText.text.toString()
        val rollNo = binding.rollNoEditText.text.toString()
        val contact = binding.contactEditText.text.toString()
        val nic = binding.nicEditText.text.toString()
        val address = binding.addressEditText.text.toString()
        val username = binding.usernameEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        viewModel.addStudent(
            Student(
            USER_TYPE,
            token!!,
            firstName,
            lastName,
            rollNo,
            contact,
            nic,
            address,
            username,
            password,
            base64ImageString
        )
        )

    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

}
