package com.kindsundev.expense.manager.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kindsundev.expense.manager.databinding.ActivitySignUpBinding
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.utils.onFeatureIsDevelop
import com.kindsundev.expense.manager.utils.startHomeActivity
import com.kindsundev.expense.manager.utils.startLoadingDialog

class SignUpActivity : AppCompatActivity(), SignUpContract.View {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpPresenter : SignUpPresenter
    private val loadingDialog by lazy { LoadingDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signUpPresenter = SignUpPresenter(this)
        initListener()
    }

    private fun initListener() {
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

    private fun onClickSignIn() { finish() }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, supportFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, supportFragmentManager, false)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        startLoadingDialog(loadingDialog, supportFragmentManager, false)
        Toast.makeText(this, "Register successful", Toast.LENGTH_SHORT).show()
        startHomeActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        signUpPresenter.cleanUp()
    }
}