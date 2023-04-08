package com.kindsundev.expense.manager.ui.home.bag.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.BillModel
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.data.model.WalletModel
import com.kindsundev.expense.manager.databinding.FragmentSearchTransactionBinding
import com.kindsundev.expense.manager.ui.custom.DateSelectionDialog
import com.kindsundev.expense.manager.ui.custom.LoadingDialog
import com.kindsundev.expense.manager.ui.custom.ResultDateTimeCallback
import com.kindsundev.expense.manager.ui.home.HomeActivity
import com.kindsundev.expense.manager.ui.home.bag.adapter.BillAdapterContract
import com.kindsundev.expense.manager.ui.home.bag.adapter.BillParentAdapter
import com.kindsundev.expense.manager.ui.home.bag.detail.TransactionBottomSheet
import com.kindsundev.expense.manager.ui.home.bag.detail.TransactionDetailContract
import com.kindsundev.expense.manager.utils.displaySwitchBottomNavigation
import com.kindsundev.expense.manager.utils.showToast
import com.kindsundev.expense.manager.utils.startLoadingDialog

class TransactionSearchFragment: Fragment(), TransactionSearchContract.View {
    private var _binding: FragmentSearchTransactionBinding? = null
    private val binding get() = _binding
    private val loadingDialog by lazy { LoadingDialog() }

    private val args: TransactionSearchFragmentArgs by navArgs()
    private lateinit var searchPresenter: TransactionSearchPresenter
    private lateinit var transactionBottomSheet: TransactionBottomSheet
    private lateinit var mCurrentTimePicker: String
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
        displaySwitchBottomNavigation(requireActivity() as HomeActivity, false)
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
                mCurrentTimePicker = newDateTime
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
        displaySwitchBottomNavigation(requireActivity() as HomeActivity, true)
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
            adapter = BillParentAdapter(mBill, object: BillAdapterContract.Listener {
                override fun onClickTransaction(date: String, transaction: TransactionModel) {
                    showTransactionDetail(date, transaction)
                }
            })
        }
    }

    private fun switchLayout() {
        binding!!.ivSearch.visibility = View.GONE
        binding!!.rcvTransaction.visibility = View.VISIBLE
    }

    private fun showTransactionDetail(date: String, transaction: TransactionModel?) {
        transactionBottomSheet = TransactionBottomSheet(object : TransactionDetailContract.Result {
            override fun notificationSuccess(result: Boolean) {
                if (result) { refreshData() }
            }
        },mWallet, date, transaction!!)
        transactionBottomSheet.show(parentFragmentManager, Constant.BUDGET_SEARCH_WALLET_BOTTOM_SHEET_WALLET_NAME)
    }

    private fun refreshData() {
        searchPresenter.searchTransactionInDay(mWallet.id!!.toString(), mCurrentTimePicker)
    }
}