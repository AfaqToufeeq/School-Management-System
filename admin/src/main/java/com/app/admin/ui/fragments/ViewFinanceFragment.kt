package com.app.admin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.admin.R
import com.app.admin.adapters.FinanceAdapter
import com.app.admin.adapters.StudentAdapter
import com.app.admin.databinding.FragmentStudentListBinding
import com.app.admin.databinding.FragmentViewFinanceBinding
import com.app.admin.models.AdminRemoveAction
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.MAIN_MENU
import com.app.admin.utils.PickerManager
import com.app.admin.utils.USER_TYPE
import com.app.admin.utils.Utils
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ViewFinanceFragment : Fragment() {
    private lateinit var binding: FragmentViewFinanceBinding
    private var argumentTitle: String? = null
    private lateinit var financeAdapter: FinanceAdapter
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = requireArguments().getString(MAIN_MENU)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setAdapter()
        setObserver()
        events()
    }

    private fun setAdapter() {
        binding.financeRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        financeAdapter =  FinanceAdapter { financeModel->
            removeData(financeModel.id)
            viewModel.deleteValue(financeModel)
        }
        binding.financeRecyclerView.adapter = financeAdapter
    }

    private fun init() {
        binding.apply {
            smsText.text = argumentTitle
            showLoading(true)

            val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
            viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
        }
    }

    private fun removeData(financeID: Int) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    viewModel.deleteData(
                        AdminRemoveAction(USER_TYPE, PickerManager.token!!, financeID, "finance")
                    )
                }
                if (response.isSuccessful) {
                    Utils.showToast(requireActivity(), "Finance is removed")
                } else {
                    Utils.showToast(requireActivity(), "Failed to remove")
                }
            } catch (e: Exception) {
                Utils.showToast(requireActivity(), "Failed to remove: ${e.message}")
            } finally {
                showLoading(false)
            }
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
        viewModel.finance.observe(viewLifecycleOwner) { finance ->
            showLoading(false)
            financeAdapter.submitList(finance)
            binding.financeRecyclerView.visibility = View.VISIBLE
        }
    }
}