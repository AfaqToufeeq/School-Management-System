package com.attech.sms.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.attech.sms.databinding.ActivityLoginBinding
import com.attech.sms.network.RetrofitClientInstance
import com.attech.sms.repository.RetrofitRepository
import com.attech.sms.utils.AUTH_TOKEN
import com.attech.sms.utils.IS_LOGGED_IN
import com.attech.sms.utils.PickerManager
import com.attech.sms.utils.PickerManager.token
import com.attech.sms.utils.SharedPreferencesManager
import com.attech.sms.utils.USER_NAME
import com.attech.sms.utils.USER_TYPE
import com.attech.sms.utils.Utils.showToast
import com.attech.sms.viewmodel.RetrofitViewModel
import com.attech.sms.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        buttonClicks()

    }

    private fun init() {
        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(this, RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
//        if (isLoggedIn())  //False
//            navigateToMainActivity()
    }

    private fun buttonClicks() {
        binding.btnLogin.setOnClickListener { login() }
    }

    private fun login() {
        val type = USER_TYPE
        val username = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            showLoading(true)

            lifecycleScope.launch {
                try {
                    val token = withContext(Dispatchers.IO) {
                        viewModel.login(type, username, password)
                    }
                    if (token.token.isNotEmpty()) {
                        saveUserLoggedInState(token.token, username)
                        navigateToMainActivity()
                        Log.d("Token",token.token)
                        showToast(this@LoginActivity,"Login successful")
                    } else {
                        showToast(this@LoginActivity,"Login failed")
                    }
                } catch (e: Exception) {
                    showToast(this@LoginActivity,"Login failed: ${e.message}")
                } finally {
                    showLoading(false)
                }
            }
        } else {
            showToast(this@LoginActivity, "Please enter both username and password")
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun isLoggedIn(): Boolean {
        token = SharedPreferencesManager.getString(AUTH_TOKEN,"")
        return SharedPreferencesManager.getBoolean(IS_LOGGED_IN, false)
    }

    private fun saveUserLoggedInState(token: String, username: String) {
        SharedPreferencesManager.saveBoolean(IS_LOGGED_IN, true)
        SharedPreferencesManager.saveString(AUTH_TOKEN, token)
        SharedPreferencesManager.saveString(USER_NAME, username)
        PickerManager.token = token
        PickerManager.userName = username
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

}