package com.kindsundev.expense.manager.ui.home.bag.search

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentSearchTransactionBinding
import com.kindsundev.expense.manager.ui.custom.DateSelectionDialog
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.custom.ResultDateTimeCallback
import com.kindsundev.expense.manager.ui.home.bag.BagContract
import com.kindsundev.expense.manager.ui.home.bag.adapter.BillParentAdapter
import com.kindsundev.expense.manager.utils.showToast
import com.kindsundev.expense.manager.utils.startLoadingDialog

class TransactionSearchFragment: Fragment(), TransactionSearchContract.View {
    private var _binding: FragmentSearchTransactionBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private val args: TransactionSearchFragmentArgs by navArgs()
    private lateinit var searchPresenter: TransactionSearchPresenter
    private lateinit var mBill : ArrayList<BillModel>
    private lateinit var mWallet: WalletModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWallet = args.wallet
        searchPresenter = TransactionSearchPresenter(this)
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
                searchPresenter.searchTransactionInDay(mWallet.id!!.toString(), newDateTime)
            }
        }).onShowDatePickerDialog()
    }

    private fun onClickBack() {
        findNavController().popBackStack(R.id.bagFragment, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        searchPresenter.cleanUp()
    }

    override fun onLoad() {
        startLoadingDialog(loadingDialog, parentFragmentManager, true)
    }

    override fun onError(message: String) {
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
        activity?.showToast(message)
    }

    override fun onSuccess() {
        initRecyclerViewTransaction()
        startLoadingDialog(loadingDialog, parentFragmentManager, false)
    }

    private fun initRecyclerViewTransaction(){
        mBill = searchPresenter.getBill()
        switchLayout()
        binding!!.rcvTransaction.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = BillParentAdapter(mBill, object: BagContract.Listener {
                override fun onClickTransaction(transaction: TransactionModel) {
                    val message = "This transaction is ${transaction.type}"
                    Snackbar.make(binding!!.myCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun switchLayout() {
        binding!!.ivSearch.visibility = View.GONE
        binding!!.rcvTransaction.visibility = View.VISIBLE
    }
}