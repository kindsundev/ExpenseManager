package com.kindsundev.expense.manager.ui.home.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.UserModel
import com.kindsundev.expense.manager.databinding.FragmentMenuBinding
import com.kindsundev.expense.manager.ui.custom.LogoutDialog
import com.kindsundev.expense.manager.utils.loadUserAvatar

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding

    private lateinit var menuPresenter: MenuPresenter
    private var menuFragmentManager: FragmentManager? = null
    private var user: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuPresenter = MenuPresenter()
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
        user = menuPresenter.getCurrentUser()
        user?.let {
            var name: String? = it.name.toString()
            val email = it.email.toString()
            val photoUrl = it.photoUrl
            if (name.isNullOrEmpty()) name = "Edit Here"
            binding!!.tvUserName.text = name
            binding!!.tvUserEmail.text = email
            activity?.loadUserAvatar(photoUrl, R.drawable.img_user_default, binding!!.imgUserAvatar)
        }
    }

    private fun initListener() {
        binding!!.imgUserAvatar.setOnClickListener { onStartUserInfo() }
        binding!!.rlLogout.setOnClickListener { onCLickLogout() }
        binding!!.rlTutorial.setOnClickListener { openTutorialYoutube() }
        binding!!.rlContact.setOnClickListener { openContactEmail() }
        binding!!.rlSettings.setOnClickListener {
            it.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToSettingsFragment())
        }
        binding!!.rlFeedback.setOnClickListener {
            it.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToFeedbackFragment())
        }
        binding!!.rlPolicy.setOnClickListener {
            it.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToPolicyFragment())
        }
        binding!!.rlAbout.setOnClickListener {
            it.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToAboutFragment())
        }
    }

    private fun onStartUserInfo() {
        val action = user?.let { MenuFragmentDirections.actionMenuFragmentToProfileFragment(it) }
        if (action != null) {
            binding!!.imgUserAvatar.findNavController().navigate(action)
        }
    }

    private fun onCLickLogout() {
        menuFragmentManager?.let {
            LogoutDialog().show(it, Constant.LOGOUT_DIALOG_NAME)
        }
    }

    private fun openTutorialYoutube() {
        val myYoutubeChannelId = "UCprYNL7hlWXV9sj08FijDpw"
        val uri = Uri.parse("http://www.youtube.com/channel/$myYoutubeChannelId")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun openContactEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.duongtiendev@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Expense Manager App")
        }
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}