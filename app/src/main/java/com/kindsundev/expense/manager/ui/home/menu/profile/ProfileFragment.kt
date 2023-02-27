package com.kindsundev.expense.manager.ui.home.menu.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private val args : ProfileFragmentArgs by navArgs()

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var bitmap: Bitmap? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        registerActivityResult()
        registerReadExternalPermissions()
    }

    private fun registerActivityResult() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                if (intent == null || intent.data == null) {
                    Logger.error("Loading image from gallery failed")
                } else {
                    val uri = intent.data
                    uri?.let { initBitmap(it) }
                    binding!!.imgUser.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun initBitmap(uri: Uri) {
        bitmap = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(
                requireContext().contentResolver, uri
            )
            ImageDecoder.decodeBitmap(source)
        }
    }

    private fun registerReadExternalPermissions() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted  ->
            if (isGranted) {
                openPhotoGallery()
            } else {
                val message = "This feature required a permission that the user has denied"
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openPhotoGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activityResultLauncher.launch(Intent.createChooser(intent, "Select picture"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        displayUserInfo()
        initListener()
        return binding!!.root
    }

    private fun displayUserInfo() {
        val userDetail = args.user
        binding!!.edtName.hint = userDetail.name
        binding!!.edtEmail.hint = userDetail.email
        binding!!.edtPhoneNumber.hint = userDetail.phoneNumber
    }

    private fun initListener() {
        binding!!.btnBack.setOnClickListener {  }
        binding!!.btnChangePassword.setOnClickListener {  }
        binding!!.imgUser.setOnClickListener { onClickUpdateImage() }
        binding!!.btnUpdate.setOnClickListener { onClickUpdateProfile() }
    }

    private fun onClickUpdateImage() {
        val permissions = Manifest.permission.READ_EXTERNAL_STORAGE
        requestPermissionLauncher.launch(permissions)
    }

    private fun onClickUpdateProfile() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}