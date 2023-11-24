package com.attech.teacher.ui.fragments

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.teacher.adapters.StudentAdapter
import com.attech.teacher.databinding.FragmentViewStudentsBinding
import com.attech.teacher.network.RetrofitClientInstance
import com.attech.teacher.repository.RetrofitRepository
import com.attech.teacher.repository.TeacherRepository
import com.attech.teacher.utils.MAIN_MENU
import com.attech.teacher.utils.PickerManager
import com.attech.teacher.utils.PickerManager.token
import com.attech.teacher.utils.USER_TYPE
import com.attech.teacher.utils.Utils.showToast
import com.attech.teacher.viewmodel.RetrofitViewModel
import com.attech.teacher.viewmodel.TeacherViewModel
import com.attech.teacher.viewmodelfactory.RetrofitViewModelFactory
import com.attech.teacher.viewmodelfactory.TeacherViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewStudentsFragment : Fragment() {

    private lateinit var binding: FragmentViewStudentsBinding
    private lateinit var studentAdapter: StudentAdapter
    private var argumentTitle: String? = null
    private val studentviewModel: TeacherViewModel by viewModels { TeacherViewModelFactory(TeacherRepository()) }
    private lateinit var viewModel: RetrofitViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = requireArguments().getString(MAIN_MENU)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        observeViewModel()
        setEventListeners()
    }

    private fun initializeViews() {
        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]

        binding.smsText.text = arguments?.getString(MAIN_MENU)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

//        studentAdapter = StudentAdapter()
        binding.recyclerView.adapter = studentAdapter

        setupClassSpinner()
    }

    private fun setupClassSpinner() {
        val batchCodes = PickerManager.allBatchesList!!.map { it.batchcode }
        batchCodes?.let {
            val classAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, it)
            classAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spinnerClass.adapter = classAdapter
        }

        binding.spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedClass = parent?.getItemAtPosition(position) as? String
                selectedClass?.let { viewModel.getBatchStudents(USER_TYPE, token!!, selectedClass)}
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun observeViewModel() {
        viewModel.batchStudents.observe(viewLifecycleOwner) { batches ->
            if (batches.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    showLoading(true)
                    delay(1500)
                    showLoading(false)
                    binding.recyclerView.visibility = View.GONE
                    showToast(requireActivity(),"No Student is registered in this class")
                }
                Log.d("ViewStudentsFragment", "Batches list is empty $batches")
            } else {
                showLoading(false)
                binding.recyclerView.visibility = View.VISIBLE
                studentAdapter.setStudents(batches)
                Log.d("ViewStudentsFragment", "Batches list size: ${batches.size} ")
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun setEventListeners() {
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }
}
