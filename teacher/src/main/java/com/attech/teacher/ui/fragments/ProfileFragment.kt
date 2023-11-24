package com.attech.teacher.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.attech.teacher.R
import com.attech.teacher.databinding.FragmentProfileBinding
import com.attech.teacher.network.RetrofitClientInstance
import com.attech.teacher.repository.RetrofitRepository
import com.attech.teacher.utils.ImageUtil
import com.attech.teacher.utils.MAIN_MENU
import com.attech.teacher.utils.PickerManager.liveTeacherData
import com.attech.teacher.utils.Utils
import com.attech.teacher.viewmodel.RetrofitViewModel
import com.attech.teacher.viewmodelfactory.RetrofitViewModelFactory


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var title: String = ""
    private lateinit var viewModel: RetrofitViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(MAIN_MENU) ?: ""
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setData()
        events()
    }

    private fun init() {
        binding.smsText.text = title
        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
    }

    private fun events() {
        binding.buttonLogout.setOnClickListener { logout() }
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }


    private fun setData() {
        with(binding) {
            liveTeacherData.observe(viewLifecycleOwner) {
                it?.let {
                    if (it.image!=null)
                        imageViewProfile.setImageBitmap(ImageUtil.decodeBase64ToBitmap(it.image))
                    else
                        imageViewProfile.setImageResource(R.drawable.profile_icon)

                    textViewFullName.text = "${it.firstname} ${it.lastname}"
                    textViewContact.text = it.contact
                    textViewNic.text = it.nic
                    textViewAddress.text = it.address
                    textViewUsername.text = it.username
                }

            }
        }
    }

    private fun logout() {
        showLoading(true)
       Handler(Looper.getMainLooper()).postDelayed({
           Utils.showToast(requireActivity(), "Logout Successfully")
           navigateToLogin()
       },1500)
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_profileFragment_to_loginActivity)
    }
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
