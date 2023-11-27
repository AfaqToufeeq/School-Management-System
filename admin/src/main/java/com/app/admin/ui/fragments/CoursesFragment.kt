package com.app.admin.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import com.app.admin.R
import com.app.admin.databinding.FragmentAddTeacherBinding
import com.app.admin.databinding.FragmentCoursesBinding
import com.app.admin.viewmodel.RetrofitViewModel


class CoursesFragment : Fragment() {
   private lateinit var binding: FragmentCoursesBinding
    private var argumentTitle: String? = null
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

}