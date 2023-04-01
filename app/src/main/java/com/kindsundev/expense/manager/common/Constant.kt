package com.kindsundev.expense.manager.common

class Constant {
    companion object {
        const val SPECIAL_CHARACTERS = "[.,^~/'\"!@#$%&*()_+=|<>?{}\\[\\]~-]"

        const val LOADING_DIALOG_NAME = "LOADING_FRAGMENT"
        const val LOGOUT_DIALOG_NAME = "LOGOUT_FRAGMENT"
        const val UPDATE_NAME_DIALOG_NAME = "UPDATE_NAME_FRAGMENT"
        const val UPDATE_EMAIL_DIALOG_NAME = "UPDATE_EMAIL_FRAGMENT"
        const val UPDATE_PASSWORD_DIALOG_NAME = "UPDATE_PASSWORD_FRAGMENT"

        const val WALLET_BOTTOM_SHEET_NAME = "WALLET_BOTTOM_SHEET"
        const val CATEGORY_TRANSACTION_NAME = "CATEGORY_TRANSACTION_ITEM"
        const val TRANSACTION_TYPE_EXPENSE = "expense"
        const val TRANSACTION_TYPE_INCOME = "income"
        const val TRANSACTION_TYPE_DEBT = "debt"

        const val NOTE_FRAGMENT_BUTTON_LIST_STATUS = "CURRENT_LIST_INIT"
        const val BAG_FRAGMENT_BALANCE_VISIBILITY = "STATE_VISIBILITY"

        const val MY_REFERENCE_NAME = "users"
        const val MY_REFERENCE_CHILD_WALLETS = "wallets"
        const val MY_REFERENCE_CHILD_TRANSACTION = "transactions"

        const val REF_FIELD_BALANCE = "balance"
        const val FAKE_DATA_NULL = "[404] -> this transaction is null"

        const val KEY_WALLET = "wallets"
        const val KEY_BILL = "bills"
        const val KEY_CURRENT_WALLET = "CURRENT_WALLET_CLICKED"
    }
}