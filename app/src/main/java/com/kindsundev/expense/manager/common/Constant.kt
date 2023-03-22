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
        const val TRANSACTION_TYPE_EXPENSE = "Expense"
        const val TRANSACTION_TYPE_INCOME = "Income"
        const val TRANSACTION_TYPE_DEBT = "Debt"

        const val MY_SHARED_PREFERENCES_NAME = "SAVE_CURRENT_FRAGMENT"
        const val MY_BUTTON_STATUS = "CURRENT_BUTTON_CHECKED"

        const val MY_REFERENCE_NAME = "users"
        const val MY_REFERENCE_CHILD_WALLETS = "wallets"
        const val MY_REFERENCE_CHILD_TRANSACTION = "transactions"

        const val REF_FIELD_BALANCE = "balance"
    }
}