package com.kindsundev.expense.manager.view.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.kindsundev.expense.manager.databinding.ActivitySignInBinding
import com.kindsundev.expense.manager.utils.startHomeActivity
import com.kindsundev.expense.manager.view.signup.SignUpActivity

class SignInActivity : AppCompatActivity(), SignInContract.View {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var signInPresenter: SignInPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signInPresenter = SignInPresenter(this)
        registerSignInListener()
    }

    private fun registerSignInListener() {
        binding.btnLogin.setOnClickListener { onClickSignIn() }
        binding.ivFacebookLogin.setOnClickListener { onFeatureIsDevelop() }
        binding.ivGmailLogin.setOnClickListener { onFeatureIsDevelop() }
        binding.ivTwitterLogin.setOnClickListener { onFeatureIsDevelop() }
        binding.tvForgetPassword.setOnClickListener { onFeatureIsDevelop() }
        binding.tvSignup.setOnClickListener { onClickSignUp() }
    }

    private fun onClickSignIn() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        signInPresenter.handlerSignIn(email, password)
    }

    private fun onFeatureIsDevelop() {
        Toast.makeText(this, "This feature is in development", Toast.LENGTH_SHORT).show()
    }

    private fun onClickSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    override fun onLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onError(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
        startHomeActivity()
    }

    override fun onStop() {
        super.onStop()
        binding.progressBar.visibility = View.GONE
        signInPresenter.onCleared()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.progressBar.visibility = View.GONE
        signInPresenter.onCleared()
    }
}