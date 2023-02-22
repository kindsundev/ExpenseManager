package com.kindsundev.expense.manager.view.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kindsundev.expense.manager.databinding.ActivitySignUpBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignUpActivity : AppCompatActivity(), SignUpContract.ViewInterface {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpPresenter : SignUpPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signUpPresenter = SignUpPresenter(this)
        registerSignUpListener()
    }

    private fun registerSignUpListener() {
        binding.btnLogin.setOnClickListener { onClickSignUp() }
        binding.ivFacebookLogin.setOnClickListener { onFeatureIsDevelop() }
        binding.ivGmailLogin.setOnClickListener { onFeatureIsDevelop() }
        binding.ivTwitterLogin.setOnClickListener { onFeatureIsDevelop() }
        binding.tvForgetPassword.setOnClickListener { onFeatureIsDevelop() }
        binding.tvSignup.setOnClickListener { onClickSignIn() }
    }

    private fun onClickSignUp() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        signUpPresenter.handlerSignUp(email, password)
    }

    private fun onFeatureIsDevelop() {
        Toast.makeText(this, "This feature is in development", Toast.LENGTH_SHORT).show()
    }

    private fun onClickSignIn() { finish() }

    override fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onError(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, "Success, please login!", Toast.LENGTH_LONG).show()
        runBlocking {
            delay(500)
        }
        finish()
    }

    override fun onStop() {
        super.onStop()
        binding.progressBar.visibility = View.GONE
        signUpPresenter.onCleared()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.progressBar.visibility = View.GONE
        signUpPresenter.onCleared()
    }
}