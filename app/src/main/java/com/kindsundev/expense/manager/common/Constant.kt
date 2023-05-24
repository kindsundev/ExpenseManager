package com.kindsundev.expense.manager.common

class Constant {
    companion object {
        const val SPECIAL_CHARACTERS = "[.,^~/'\"!@#$%&*()_+=|<>?{}\\[\\]~-]"

        const val LOADING_DIALOG_NAME = "LOADING_FRAGMENT"
        const val LOGOUT_DIALOG_NAME = "LOGOUT_FRAGMENT"
        const val UPDATE_NAME_DIALOG_NAME = "UPDATE_NAME_FRAGMENT"
        const val UPDATE_EMAIL_DIALOG_NAME = "UPDATE_EMAIL_FRAGMENT"
        const val UPDATE_PASSWORD_DIALOG_NAME = "UPDATE_PASSWORD_FRAGMENT"

        const val REPORT_WALLET_BOTTOM_SHEET_WALLET_NAME = "REPORT_WALLET_BOTTOM_SHEET"
        const val TRANSACTION_WALLET_BOTTOM_SHEET_WALLET_NAME = "TRANS_WALLET_BOTTOM_SHEET"
        const val TRANSACTION_WALLET_BOTTOM_SHEET_PLAN_NAME = "TRANS_PLAN_BOTTOM_SHEET"
        const val TRANSACTION_WALLET_BOTTOM_SHEET_DEBT_NAME = "TRANS_DEBT_BOTTOM_SHEET"
        const val TRANSACTION_WALLET_BOTTOM_SHEET_TRANSACTION_NAME = "TRANSACTION_BOTTOM_SHEET"
        const val BUDGET_WALLET_BOTTOM_SHEET_DETAIL_WALLET_NAME = "BUDGET_WALLET_DETAIL_BOTTOM_SHEET"
        const val BUDGET_WALLET_BOTTOM_SHEET_WALLET_NAME = "BUDGET_WALLET_BOTTOM_SHEET"
        const val BUDGET_SEARCH_WALLET_BOTTOM_SHEET_WALLET_NAME = "BUDGET_SEARCH_WALLET_BOTTOM_SHEET"

        const val CATEGORY_TRANSACTION_NAME = "CATEGORY_TRANSACTION_ITEM"
        const val TRANSACTION_TYPE_EXPENSE = "expense"
        const val TRANSACTION_TYPE_INCOME = "income"
        const val TRANSACTION_TYPE_DEBT = "debt"

        const val NOTE_FRAGMENT_BUTTON_LIST_STATUS = "CURRENT_LIST_INIT"
        const val BAG_FRAGMENT_BALANCE_VISIBILITY = "STATE_VISIBILITY"
        const val BAG_FRAGMENT_BEFORE_WALLET_ID= "BEFORE_WALLET_ID"

        const val TRANSACTION_STATE_INCOME = "INCOME > EXPENSE"
        const val TRANSACTION_STATE_EXPENSE = "INCOME < EXPENSE"
        const val TRANSACTION_STATE_BALANCE = "INCOME == EXPENSE"
        const val GREEN_COLOR_CODE = "#338a3e"
        const val TEA_COLOR_CODE = "#60BDBD"
        const val PURPLE_COLOR_CODE = "#FFBB86FC"
        const val YELLOW_COLOR_CODE = "#c88719"
        const val GRAY_COLOR_CODE = "#A8A8A8"
        const val RED_COLOR_CODE = "#ffcc0000"

        const val MY_REFERENCE_NAME = "users"
        const val MY_REFERENCE_CHILD_WALLETS = "wallets"
        const val MY_REFERENCE_CHILD_TRANSACTIONS = "transactions"
        const val MY_REFERENCE_CHILD_PLANS = "plans"
        const val REF_FIELD_BALANCE_OF_WALLET = "balance"
        const val REF_FIELD_CURRENT_BALANCE_OF_PLAN = "currentBalance"

        const val KEY_CURRENT_WALLET_ID = "CURRENT_WALLET_ID"
        const val VALUE_DATA_IS_NULL= "NULL"
    }
}