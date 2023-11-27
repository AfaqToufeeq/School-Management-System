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
import com.app.admin.adapters.BatchesAdapter
import com.app.admin.databinding.FragmentBatchBinding
import com.app.admin.models.AddBatch
import com.app.admin.models.BatchResponse
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.LoadingDialog
import com.app.admin.utils.MAIN_MENU
import com.app.admin.utils.PickerManager.token
import com.app.admin.utils.USER_TYPE
import com.app.admin.utils.Utils
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class BatchFragment : Fragment() {
    private lateinit var binding: FragmentBatchBinding
    private var argumentTitle: String? = null
    private lateinit var viewModel: RetrofitViewModel
    private var batchesAdapter = BatchesAdapter()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = arguments?.getString(MAIN_MENU) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBatchBinding.inflate(inflater, container, false)
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
        binding.batchesRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.batchesRecyclerView.adapter = batchesAdapter
    }

    private fun setObserver() {
        viewModel.allBatches.observe(viewLifecycleOwner) { batches ->
            batchesAdapter.submitList(batches)
            setHandler()
        }
    }

    private fun init() {
        loadingDialog = LoadingDialog(requireActivity())
        handleVisibility()
        binding.smsText.text = argumentTitle

        val retrofitRepository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(retrofitRepository))[RetrofitViewModel::class.java]
    }

    private fun handleVisibility() {
        val isAddBatch = (argumentTitle == "Add Batch")
        with(binding) {
            viewBatchParentView.visibility = if (isAddBatch) View.GONE else View.VISIBLE
            addBatchParentView.visibility = if (isAddBatch) View.VISIBLE else View.GONE
        }
        if (!isAddBatch) showLoader()
    }


    private fun addBatch() {
        val addBatch = binding.editTextBatch.text.toString()

        lifecycleScope.launch {
            val response = withContext(Dispatchers.IO) {
                viewModel.addBatch(
                    AddBatch(
                        type = USER_TYPE,
                        token = token!!,
                        bcode = "Class $addBatch"
                    )
                )
            }
            logResponse(response)
        }

    }

    private fun logResponse(response: Response<BatchResponse>) {
        if (response.isSuccessful) {
            Utils.showToast(requireActivity(), "Successfully Added")
            backNavigation()
        } else {
            Utils.showToast(requireActivity(), "Some Issues: ${response.errorBody()!!}")
            Log.d("ErrorLogResponse", "Some Issues: ${response.errorBody()!!} ${response.code()}")
        }

    }

    private fun setEventListeners() {
        binding.leftIcon.setOnClickListener { backNavigation() }
        binding.buttonAdd.setOnClickListener { addBatch() }
    }

    private fun backNavigation() {
        findNavController().popBackStack()
    }

    private fun showLoader() {
        loadingDialog.showLoadingDialog("loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }

    private fun setHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        },1500)
    }

}