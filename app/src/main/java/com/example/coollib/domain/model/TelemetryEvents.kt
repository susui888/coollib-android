package com.example.coollib.domain.model

object TelemetryEvents {

    object Screens {
        const val HOME = "HOME_SCREEN"
        const val BOOK_DETAIL = "BOOK_DETAIL_SCREEN"
        const val LOGIN = "LOGIN_SCREEN"
        const val REGISTER = "REGISTER_SCREEN"
        const val SEARCH = "SEARCH_SCREEN"
        const val BOOK = "BOOK_SCREEN"
        const val CART = "CART_SCREEN"
        const val REVIEW = "REVIEW_SCREEN"
        const val SCANNER = "SCANNER_SCREEN"
        const val ABOUT = "ABOUT_SCREEN"
        const val STATICS = "STATICS_SCREEN"
        const val LOAN = "LOAN_SCREEN"
    }

    object Actions {
        const val AUTH_LOGIN_SUCCESS = "AUTH_LOGIN_SUCCESS"
        const val BOOK_ADD_WISHLIST = "BOOK_ADD_WISHLIST"
        const val BOOK_REMOVE_WISHLIST = "BOOK_REMOVE_WISHLIST"
        const val BOOK_RENT_ACTION = "BOOK_RENT_ACTION"

        // 保留用于 ViewModel 核心非致命异常捕捉的事件名
        const val HOME_DATA_LOAD_SUCCESS = "HOME_DATA_LOAD_SUCCESS"
        const val HOME_DATA_LOAD_FAILURE = "HOME_DATA_LOAD_FAILURE"
    }
}