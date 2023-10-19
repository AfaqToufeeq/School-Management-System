package com.app.admin.ui.fragments

import ImageUtil
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.app.admin.databinding.FragmentAddNewsBinding
import com.app.admin.utils.MAIN_MENU

class AddNewsFragment : Fragment() {

    private lateinit var binding: FragmentAddNewsBinding
    private var argumentTitle: String? = null
    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>


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

        init()
        events()
    }

    private fun init() {
        binding.toolbar.smsText.text = argumentTitle
        pickSingleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) {
                    Toast.makeText(requireActivity(), "Failed picking media.", Toast.LENGTH_SHORT).show()
                } else {
                    val uri = it.data?.data
                    binding.imageView.setImageURI(uri)
                }
            }
    }

    private fun events() {
        binding.apply {
           toolbar.leftIcon.setOnClickListener { findNavController().popBackStack() }

            uploadImageButton.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pickSingleMediaLauncher.launch (
                        Intent(MediaStore.ACTION_PICK_IMAGES)
                            .apply { type = "image/*" }
                    )
                } else
                    openImageSelection()
            }
        }
    }

    private fun openImageSelection() = ImageUtil.selectImageFromGallery(this)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        ImageUtil.handleImageSelectionResult(requestCode, resultCode, data) {
            binding.imageView.setImageURI(it)
        }
    }

}

