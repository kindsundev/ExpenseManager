package com.kindsundev.expense.manager.ui.home.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.auth.FirebaseUser
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.firebase.AuthFirebase
import com.kindsundev.expense.manager.databinding.FragmentMenuBinding
import com.kindsundev.expense.manager.ui.custom.LogoutDialog
import com.kindsundev.expense.manager.utils.startLoadingDialog

class MenuFragment : Fragment(), MenuContract.View {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding
    private var menuFragmentManager: FragmentManager? = null
    private lateinit var auth: AuthFirebase
    private lateinit var menuPresenter: MenuPresenter
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuPresenter = MenuPresenter(this)
        auth = AuthFirebase()
        user = menuPresenter.getCurrentUser()
        menuFragmentManager = activity?.supportFragmentManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        displayUserInfo()
        initListener()
        return binding!!.root
    }

    private fun displayUserInfo() {
        var name = user?.displayName.toString()
        if (name.isEmpty()) name = "Please enter your name!"
        val email = user?.email.toString()

        binding!!.tvUserName.text = name
        binding!!.tvUserEmail.text = email
    }

    private fun initListener() {
        binding!!.btnLogout.setOnClickListener { onCLickLogout() }
    }

    private fun onCLickLogout() {
        menuFragmentManager?.let {
            val message = "Do you want really logout?"
            menuFragmentManager?.let {
                LogoutDialog(message).show(it, Constant.LOGOUT_DIALOG_NAME)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLoad() {
        menuFragmentManager?.let { startLoadingDialog(it, true) }
    }

    override fun onError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess() {
        menuFragmentManager?.let { startLoadingDialog(it, false) }
    }
}