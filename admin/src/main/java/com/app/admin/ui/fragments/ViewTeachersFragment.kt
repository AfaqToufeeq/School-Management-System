package com.app.admin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.admin.adapters.ViewTeachersAdapter
import com.app.admin.databinding.FragmentViewTeachersBinding
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.MAIN_MENU
import com.app.admin.utils.PickerManager
import com.app.admin.utils.USER_TYPE
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory

class ViewTeachersFragment : Fragment() {

    private lateinit var binding: FragmentViewTeachersBinding
    private var teacherAdapter = ViewTeachersAdapter()
    private var argumentTitle: String? = null
    private lateinit var viewModel: RetrofitViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = requireArguments().getString(MAIN_MENU)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewTeachersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setObserver()
        events()
    }

    private fun init() {
        binding.apply {
            smsText.text = argumentTitle
            teacherRecyclerView.visibility = View.GONE
            showLoading(true)
            teacherRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
            viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
            teacherRecyclerView.adapter = teacherAdapter
        }
    }

    private fun events() {
        binding.apply {
            leftIcon.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun setObserver() {
        with(viewModel) {
            fetchTeachers(USER_TYPE, PickerManager.token!!)

            teachers.observe(viewLifecycleOwner) { teachers ->
                showLoading(false)
                teacherAdapter.submitList(teachers)
                binding.teacherRecyclerView.visibility = View.VISIBLE
            }
        }
    }
}
