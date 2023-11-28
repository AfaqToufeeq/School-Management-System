package com.app.admin.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.admin.R
import com.app.admin.databinding.FragmentAddFinanceBinding
import com.app.admin.models.Finance
import com.app.admin.models.FinanceResponse
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.ImageUtil
import com.app.admin.utils.MAIN_MENU
import com.app.admin.utils.PickerManager.token
import com.app.admin.utils.USER_TYPE
import com.app.admin.utils.Utils
import com.app.admin.utils.Utils.showToast
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class AddFinanceFragment : Fragment() {

    private lateinit var binding: FragmentAddFinanceBinding
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
        binding = FragmentAddFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bitmap = BitmapFactory.decodeResource(requireActivity().resources, R.drawable.person)
        base64ImageString = ImageUtil.bitmapToBase64(bitmap)

        initialize()
        events()
    }

    private fun initialize() {
        binding.smsText.text = argumentTitle

        pickSingleMediaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleMediaPickerResult(result)
            }

        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel =
            ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
    }

    private fun events() {
        binding.apply {
            leftIcon.setOnClickListener { findNavController().popBackStack() }

            uploadImageButton.setOnClickListener {
               launchMediaPicker()
            }
            addFinanceMemberButton.setOnClickListener {
                requireActivity().runOnUiThread {
                    showLoading(true)
                }
                addFinance()
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
            base64ImageString = uri?.let { ImageUtil.convertUriToBase64(requireActivity(), it) }
            if (base64ImageString!=null)
                binding.financeIcon.setImageURI(uri)
        } else {
            showToast(requireActivity(), "Failed picking media.")
        }
    }

    private fun addFinance() {
        val financeMemberName = binding.financeMemberNameEditText.text.toString()
        val financeMemberEmail = binding.financeMemberEmailEditText.text.toString()
        val usernameEditText = binding.usernameEditText.text.toString()
        val passwordEditText = binding.passwordEditText.text.toString()

        if (financeMemberName.isNotEmpty() && financeMemberEmail.isNotEmpty() &&
            usernameEditText.isNotEmpty() && passwordEditText.isNotEmpty()
        ) {
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    viewModel.addFinancePerson(
                        Finance(
                            type = USER_TYPE,
                            token = token!!,
                            name = financeMemberName,
                            email = financeMemberEmail,
                            role = "Finance",
                            username = usernameEditText,
                            password = passwordEditText,
                            image = base64ImageString!!
                        )
                    )
                }
                logResponse(response)
            }
        } else {
            showToast(requireActivity(), "Please fill in all fields")
        }

    }

    private fun logResponse(response: Response<FinanceResponse>) {
        if (response.isSuccessful) {
            showLoading(false)
            showToast(requireActivity(), "Finance added successfully")
            handleBackPressed()
        } else {
            Log.d("ErrorLogResponse", "Some Issues: ${response.errorBody()!!} ${response.code()}")
            showLoading(false)
            showToast(requireActivity(), "Failed to add finance")
        }

    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }


}
