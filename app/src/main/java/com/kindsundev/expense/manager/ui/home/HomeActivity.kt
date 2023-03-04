package com.kindsundev.expense.manager.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.UserModel
import com.kindsundev.expense.manager.data.shared.PrivateSharedPreferences
import com.kindsundev.expense.manager.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), HomeContract.View {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homePresenter: HomePresenter
    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            setupBottomNav()
        }

        homePresenter = HomePresenter(this)
        user = homePresenter.getCurrentUser()

        saveIdToken()
        getIdToken()
    }

    private fun getIdToken() {
        Logger.warn("Home: [GET] ${PrivateSharedPreferences(this).getUserIdTokenLogged()}")
    }

    private fun setupBottomNav() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeNavHostFragment)
        navHostFragment?.let {
            val navController = navHostFragment.findNavController()
            binding.bottomNavigationView.setupWithNavController(navController)
        }
    }

    private fun saveIdToken() {
        user?.id?.let {
            PrivateSharedPreferences(this).saveUserIdTokenLogged(it)
            Logger.warn("Home: [SAVE] ${user!!.id.toString()}")
        }
    }

    fun getCurrentUserLogged(): Bundle {
        val bundle = Bundle()
        bundle.putSerializable(Constant.GET_CURRENT_USER_NAME, user)
        return bundle
    }

    override fun onLoad() {}

    override fun onError(message: String) {}

    override fun onSuccess() {}
}