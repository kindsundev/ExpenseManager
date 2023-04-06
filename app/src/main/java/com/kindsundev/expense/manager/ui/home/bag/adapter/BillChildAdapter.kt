package com.kindsundev.expense.manager.ui.home.bag.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kindsundev.expense.manager.R
import com.kindsundev.expense.manager.common.Constant
import com.kindsundev.expense.manager.data.model.TransactionModel
import com.kindsundev.expense.manager.databinding.LayoutWalletBillItemBinding
import com.kindsundev.expense.manager.utils.formatDisplayCurrencyBalance

class BillChildAdapter(
    private val date: String,
    private val transactions: ArrayList<TransactionModel>,
    private val listener: BillAdapterContract.Listener
): RecyclerView.Adapter<BillChildAdapter.BillViewHolder>() {

    inner class BillViewHolder(val binding: LayoutWalletBillItemBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = LayoutWalletBillItemBinding.inflate(layoutInflater, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val bill = transactions[position]
        initBillInfo(holder.binding, bill)
        holder.binding.root.setOnClickListener {
            listener.onClickTransaction(date, bill)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initBillInfo(binding: LayoutWalletBillItemBinding, bill: TransactionModel) {
        binding.tvCategoryName.text = bill.category.toString()
        if (bill.note == "") {
            binding.tvNote.visibility = View.GONE
        } else {
            binding.tvNote.visibility = View.VISIBLE
            binding.tvNote.text = bill.note.toString()
        }
        binding.tvAmount.text = formatDisplayCurrencyBalance(bill.amount.toString())
        initTextColor(binding, bill.type.toString())
        initCategoryIcon(binding, bill.category.toString())
        initCategoryIconBackground(binding, bill.category.toString())
    }

    private fun initTextColor(binding: LayoutWalletBillItemBinding, type: String) {
        if (isExpenseType(type)) {
            binding.tvAmount.setTextColor(Color.parseColor(Constant.RED_COLOR_CODE))
        } else {
            binding.tvAmount.setTextColor(Color.parseColor(Constant.GREEN_COLOR_CODE))
        }
    }

    private fun isExpenseType(type: String?) = type == Constant.TRANSACTION_TYPE_EXPENSE

    private fun initCategoryIcon(binding: LayoutWalletBillItemBinding, categoryName: String) {
        when (categoryName) {
            "Eat" -> initImageResource(binding, R.drawable.ic_eat)
            "Bill" -> initImageResource(binding, R.drawable.ic_bill)
            "Move" -> initImageResource(binding, R.drawable.ic_car)
            "House" -> initImageResource(binding, R.drawable.ic_house)
            "Shopping" -> initImageResource(binding, R.drawable.ic_shopping)
            "Entertainment" -> initImageResource(binding, R.drawable.ic_entertainment)
            "Outfit" -> initImageResource(binding, R.drawable.ic_outfit)
            "Travel" -> initImageResource(binding, R.drawable.ic_plane)
            "Beautify" -> initImageResource(binding, R.drawable.ic_palette)
            "Party" -> initImageResource(binding, R.drawable.ic_party)
            "Gift" -> initImageResource(binding, R.drawable.ic_gift_card)
            "Charity" -> initImageResource(binding, R.drawable.ic_atm)
            "Doctor" -> initImageResource(binding, R.drawable.ic_medication)
            "Sport" -> initImageResource(binding, R.drawable.ic_soccer)
            "Insurance" -> initImageResource(binding, R.drawable.ic_yard)
            "Child" -> initImageResource(binding, R.drawable.ic_child)
            "Fees" -> initImageResource(binding, R.drawable.ic_currency_exchange)
            "Drop" -> initImageResource(binding, R.drawable.ic_money_off)
            "Salary" -> initImageResource(binding, R.drawable.ic_event_available)
            "Bonus" -> initImageResource(binding, R.drawable.ic_radar)
            "Interest Rate" -> initImageResource(binding, R.drawable.ic_percent)
            "Other" -> initImageResource(binding, R.drawable.ic_price_change_orange)
            "Lend" -> initImageResource(binding, R.drawable.ic_attach_email)
            "Repayment" -> initImageResource(binding, R.drawable.ic_mark_email_read)
            "Borrow" -> initImageResource(binding, R.drawable.ic_email_unread)
            "Debt Collection" -> initImageResource(binding, R.drawable.ic_all_inbox)
            else -> initImageResource(binding, R.drawable.ic_eat)
        }
    }

    private fun initCategoryIconBackground(binding: LayoutWalletBillItemBinding, categoryName: String) {
        when(categoryName) {
            "Entertainment" -> binding.icCategory.setBackgroundResource(R.drawable.bg_tea_circle)
            "Outfit" -> binding.icCategory.setBackgroundResource(R.drawable.bg_tea_circle)
            "Travel" -> binding.icCategory.setBackgroundResource(R.drawable.bg_tea_circle)
            "Beautify" -> binding.icCategory.setBackgroundResource(R.drawable.bg_tea_circle)
            "Party" -> binding.icCategory.setBackgroundResource(R.drawable.bg_tea_circle)
            "Gift" -> binding.icCategory.setBackgroundResource(R.drawable.bg_purple_circle)
            "Charity" -> binding.icCategory.setBackgroundResource(R.drawable.bg_purple_circle)
            "Doctor" -> binding.icCategory.setBackgroundResource(R.drawable.bg_red_circle)
            "Sport" -> binding.icCategory.setBackgroundResource(R.drawable.bg_red_circle)
            "Insurance" -> binding.icCategory.setBackgroundResource(R.drawable.bg_red_circle)
            "Child" -> binding.icCategory.setBackgroundResource(R.drawable.bg_orange_circle)
            "Fees" -> binding.icCategory.setBackgroundResource(R.drawable.bg_gray_blue_circle)
            "Drop" -> binding.icCategory.setBackgroundResource(R.drawable.bg_gray_blue_circle)
            "Salary" -> binding.icCategory.setBackgroundResource(R.drawable.bg_dark_circle)
            "Bonus" -> binding.icCategory.setBackgroundResource(R.drawable.bg_dark_circle)
            "Interest Rate" -> binding.icCategory.setBackgroundResource(R.drawable.bg_dark_circle)
            "Other" -> binding.icCategory.setBackgroundResource(R.drawable.bg_dark_circle)
            "Lend" -> binding.icCategory.setBackgroundResource(R.drawable.bg_gray_blue_circle)
            "Repayment" -> binding.icCategory.setBackgroundResource(R.drawable.bg_gray_blue_circle)
            "Borrow" -> binding.icCategory.setBackgroundResource(R.drawable.bg_gray_blue_circle)
            "Debt Collection" -> binding.icCategory.setBackgroundResource(R.drawable.bg_gray_blue_circle)
            else -> {}
        }
    }

    private fun initImageResource(binding: LayoutWalletBillItemBinding, resource: Int) {
        binding.icCategory.setImageResource(resource)
    }

    override fun getItemCount() = transactions.size
}