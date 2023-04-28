package com.kindsundev.expense.manager.ui.signin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kindsundev.expense.manager.R

import com.kindsundev.expense.manager.databinding.ActivitySignInBinding
import com.kindsundev.expense.manager.ui.signup.SignUpActivity
import com.kindsundev.expense.manager.utils.onFeatureIsDevelop
import com.kindsundev.expense.manager.utils.startPrepareWalletActivity

class SignInActivity : AppCompatActivity(), SignInContract.View {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var signInPresenter: SignInPresenter

    override fun getCurrentContext(): Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signInPresenter = SignInPresenter(this)
        initListener()
    }

    private fun initListener() {
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

    private fun onClickSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    override fun onLoad() {
        binding.tvForgetPassword.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.tvForgetPassword.visibility = View.VISIBLE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        binding.progressBar.visibility = View.GONE
        binding.tvForgetPassword.visibility = View.VISIBLE
        Toast.makeText(this, R.string.login_successful, Toast.LENGTH_SHORT).show()
        startPrepareWalletActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        signInPresenter.cleanUp()
    }
}