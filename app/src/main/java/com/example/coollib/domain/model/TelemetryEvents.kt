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
        const val BOOK_ADD_CART = "BOOK_ADD_CART"
        const val BOOK_REMOVE_CART = "BOOK_REMOVE_CART"
        const val BOOK_ADD_WISHLIST = "BOOK_ADD_WISHLIST"
        const val BOOK_REMOVE_WISHLIST = "BOOK_REMOVE_WISHLIST"

        const val BOOK_RENT_ACTION = "BOOK_RENT_ACTION"
        const val AUTH_LOGIN_SUCCESS = "AUTH_LOGIN_SUCCESS"

        const val BOOK_SEARCH = "BOOK_SEARCH"
        const val BOOK_POST_REVIEW_SUCCESS = "BOOK_POST_REVIEW_SUCCESS"


        const val HOME_DATA_LOAD_SUCCESS = "HOME_DATA_LOAD_SUCCESS"
        const val HOME_DATA_LOAD_FAILURE = "HOME_DATA_LOAD_FAILURE"   // 首页/通用加载失败

        const val BORROW_ACTION_FAILURE = "BORROW_ACTION_FAILURE"     // 借阅网络结算失败
        const val WISHLIST_ACTION_FAILURE = "WISHLIST_ACTION_FAILURE" // 心愿单网络同步失败
        const val BOOK_DETAIL_LOAD_FAILURE = "BOOK_DETAIL_LOAD_FAILURE" // 书籍详情/评论加载失败
    }
}