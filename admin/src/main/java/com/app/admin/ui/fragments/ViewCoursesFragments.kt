package com.app.admin.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.admin.R
import com.app.admin.adapters.CourseAdapter
import com.app.admin.adapters.StudentAdapter
import com.app.admin.databinding.FragmentCoursesBinding
import com.app.admin.databinding.FragmentViewCoursesFragmentsBinding
import com.app.admin.models.Course
import com.app.admin.models.CoursesResponse
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.LoadingDialog
import com.app.admin.utils.PickerManager
import com.app.admin.utils.USER_TYPE
import com.app.admin.utils.Utils
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class ViewCoursesFragments : Fragment() {
    private lateinit var binding: FragmentViewCoursesFragmentsBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var viewModel: RetrofitViewModel
    private val courseAdapter = CourseAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewCoursesFragmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setAdapter()
        setObserver()
        setEventListeners()
    }

    private fun setAdapter() {
        binding.coursesRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.coursesRecyclerView.adapter = courseAdapter
    }

    private fun setObserver() {
        viewModel.getCourses.observe(viewLifecycleOwner) {
            courseAdapter.setCourses(it)
        }
    }

    private fun init() {
        loadingDialog = LoadingDialog(requireActivity())
        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(
            requireActivity(),
            RetrofitViewModelFactory(retrofitRepository)
        )[RetrofitViewModel::class.java]
    }



    private fun setEventListeners() {
        binding.leftIcon.setOnClickListener { backNavigation() }
    }

    private fun backNavigation() {
        findNavController().popBackStack()
    }

    private fun showLoader() {
        loadingDialog.showLoadingDialog("Loading...Please wait")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }

    private fun setHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        }, 1500)
    }
}
