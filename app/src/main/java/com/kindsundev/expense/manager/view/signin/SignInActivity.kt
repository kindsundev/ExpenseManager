package com.kindsundev.expense.manager.view.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.kindsundev.expense.manager.databinding.ActivitySignBinding

class SignInActivity : AppCompatActivity(), SignInContract.ViewInterface {
    private lateinit var binding: ActivitySignBinding
    private lateinit var signInPresenter: SignInPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInPresenter = SignInPresenter(this)

        registerSignInListener()
    }

    private fun registerSignInListener() {
        binding.btnLogin.setOnClickListener { onClickLogin() }
        // more
    }

    private fun onClickLogin() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        signInPresenter.handlerSignIn(email, password)
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
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        // next to home activity
    }

    override fun onStop() {
        super.onStop()
        signInPresenter.onCleared()
    }

    override fun onDestroy() {
        super.onDestroy()
        signInPresenter.onCleared()
    }
}