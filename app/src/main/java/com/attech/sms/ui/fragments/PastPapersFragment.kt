package com.attech.sms.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.attech.sms.databinding.FragmentPastPapersBinding
import com.attech.sms.repository.StudentRepository
import com.attech.sms.utils.MAIN_MENU
import com.attech.sms.viewmodel.StudentViewModel
import com.attech.sms.viewmodelfactory.StudentViewModelFactory


class PastPapersFragment : Fragment() {
    private lateinit var binding: FragmentPastPapersBinding
    private var title: String = ""
    private lateinit var viewModel: StudentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(MAIN_MENU) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPastPapersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setObservers()
        events()
    }

    private fun setObservers() {
        viewModel.webPageUrl.observe(viewLifecycleOwner) { url ->
            binding.webView.loadUrl(url)
        }
    }

    private fun init() {
        binding.smsText.text = title
        val repository = StudentRepository()
        viewModel = ViewModelProvider(requireActivity(), StudentViewModelFactory(repository))[StudentViewModel::class.java]
    }

    private fun events() {
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }

}