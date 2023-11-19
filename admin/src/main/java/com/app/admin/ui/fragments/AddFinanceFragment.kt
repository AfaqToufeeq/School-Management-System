package com.app.admin.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.app.admin.databinding.FragmentAddFinanceBinding
import com.app.admin.utils.MAIN_MENU

class AddFinanceFragment : Fragment() {

    private lateinit var binding: FragmentAddFinanceBinding
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
        binding = FragmentAddFinanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        events()
    }

    private fun init() {
        binding.smsText.text = argumentTitle
        pickSingleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = result.data?.data
                    Toast.makeText(requireActivity(), "Successfully Selected!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireActivity(), "Failed picking media.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun events() {
        binding.apply {
            leftIcon.setOnClickListener { findNavController().popBackStack() }

            uploadImageButton.setOnClickListener {
                pickSingleMediaLauncher.launch(
                    Intent(Intent.ACTION_PICK)
                        .setType("image/*")
                )
            }
        }
    }
}
