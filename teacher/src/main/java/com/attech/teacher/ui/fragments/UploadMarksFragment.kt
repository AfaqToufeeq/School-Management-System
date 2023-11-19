// UploadMarksFragment.kt
package com.attech.teacher.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.attech.teacher.databinding.FragmentUploadMarksBinding
import com.attech.teacher.models.MarksData
import com.attech.teacher.repository.TeacherRepository
import com.attech.teacher.utils.MAIN_MENU
import com.attech.teacher.viewmodel.TeacherViewModel
import com.attech.teacher.viewmodelfactory.TeacherViewModelFactory

class UploadMarksFragment : Fragment() {
    private lateinit var binding: FragmentUploadMarksBinding
    private var argumentTitle: String? = null
    private val viewModel: TeacherViewModel by viewModels { TeacherViewModelFactory(TeacherRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = requireArguments().getString(MAIN_MENU)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadMarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        observeViewModel()
        events()
    }

    private fun init() {
        binding.smsText.text = argumentTitle

        // Populate the spinner with class names
        viewModel.classes.observe(viewLifecycleOwner) { classes ->
            classes?.let {
                val classAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it)
                classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerClass.adapter = classAdapter
            }
        }

        // Example of submitting marks
        binding.btnSubmit.setOnClickListener {
            val selectedClass = binding.spinnerClass.selectedItem as? String
            val subject = binding.etSubject.text.toString()
            val marks = binding.etMarks.text.toString().toDouble()

            if (selectedClass != null && subject.isNotEmpty()) {
                // In this example, we're using a placeholder value (1) as the studentId.
                val marksData = MarksData(1, subject, marks)
                viewModel.uploadMarks(marksData)
            }
        }
    }

    private fun observeViewModel() {
        // Observe relevant LiveData from the ViewModel
        // For example, observe a LiveData that indicates whether marks upload was successful
        viewModel.uploadSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                // Handle success, navigate or show a success message
            } else {
                // Handle failure, show an error message
            }
        }
    }

    private fun events() {
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }

}
