package com.kindsundev.expense.manager.ui.home.bag.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Logger
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentSearchTransactionBinding
import com.kindsundev.expense.manager.ui.custom.DateSelectionDialog
import com.kindsundev.expense.manager.ui.custom.ResultDateTimeCallback

class TransactionSearchFragment: Fragment() {
    private var _binding: FragmentSearchTransactionBinding? = null
    private val binding get() = _binding
    private val args: TransactionSearchFragmentArgs by navArgs()
    private lateinit var mWallet: WalletModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWallet = args.wallet
        Logger.error(mWallet.name.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchTransactionBinding.inflate(layoutInflater)
        initListener()
        return binding!!.root
    }

    private fun initListener() {
        binding!!.itemTime.setOnClickListener { onClickSetDate() }
        binding!!.btnBack.setOnClickListener { onClickBack() }
    }

    private fun onClickSetDate() {
        DateSelectionDialog(requireContext(), object : ResultDateTimeCallback {
            override fun resultNewDateTime(newDateTime: String) {
                binding!!.tvDate.text = newDateTime
            }
        }).onShowDatePickerDialog()
    }

    private fun onClickBack() {
        findNavController().popBackStack(R.id.bagFragment, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}