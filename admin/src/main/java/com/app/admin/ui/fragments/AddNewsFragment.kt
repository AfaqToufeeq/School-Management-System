package com.app.admin.ui.fragments

import com.app.admin.utils.ImageUtil
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.admin.R
import com.app.admin.databinding.FragmentAddNewsBinding
import com.app.admin.models.AddNewsModel
import com.app.admin.models.Teacher
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
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
import java.util.Calendar

class AddNewsFragment : Fragment() {

    private lateinit var binding: FragmentAddNewsBinding
    private var argumentTitle: String? = null
    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: RetrofitViewModel
    private var base64ImageString: String? = null
    private var selectedDate: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argumentTitle = requireArguments().getString(MAIN_MENU)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bitmap = BitmapFactory.decodeResource(requireActivity().resources, R.drawable.placeholder)
        base64ImageString = ImageUtil.bitmapToBase64(bitmap)

        init()
        events()
    }

    private fun init() {
        binding.smsText.text = argumentTitle

        pickSingleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleMediaPickerResult(result)
            }


        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel =
            ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
    }


    private fun handleMediaPickerResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            base64ImageString = uri?.let { ImageUtil.convertUriToBase64(requireActivity(), it) }
            if (base64ImageString!=null)
                binding.imageView.setImageURI(uri)
        } else {
            showToast(requireActivity(), "Failed picking media.")
        }
    }

    private fun events() {
        binding.apply {
            leftIcon.setOnClickListener { findNavController().popBackStack() }

            uploadImageButton.setOnClickListener { launchMediaPicker() }

            addNewsButton.setOnClickListener { addNews() }

            datePickerButton.setOnClickListener { showDatePicker() }
        }
    }

    private fun addNews() {
        val titleNews = binding.titleEditText.text.toString()
        val des = binding.descriptionEditText.text.toString()

        if (!titleNews.isNullOrEmpty() && !des.isNullOrEmpty() && !selectedDate.isNullOrEmpty()) {
            showLoading(true)
            val addNewsModel = AddNewsModel(
                type = USER_TYPE,
                token = token!!,
                title = titleNews,
                description = des,
                date = selectedDate,
                image = base64ImageString!!
            )

            lifecycleScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        viewModel.addNewsEvents(addNewsModel)
                    }
                    if (response.isSuccessful) {
                        showToast(requireActivity(), "Event added successfully")
                    } else {
                        showToast(requireActivity(), "Failed to add events")
                    }
                } catch (e: Exception) {
                    showToast(requireActivity(), "Failed to add events: ${e.message}")
                } finally {
                    showLoading(false)
                }
            }
        } else
            showToast(requireActivity(),"Please complete all fields")
    }

    private fun launchMediaPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickSingleMediaLauncher.launch (
                Intent(MediaStore.ACTION_PICK_IMAGES)
                    .apply { type = "image/*" }
            )
        } else
            openImageSelection()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.selectedDate.text = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    private fun openImageSelection() = ImageUtil.selectImageFromGallery(this)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        ImageUtil.handleImageSelectionResult(requestCode, resultCode, data) {
            binding.imageView.setImageURI(it)
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

}

