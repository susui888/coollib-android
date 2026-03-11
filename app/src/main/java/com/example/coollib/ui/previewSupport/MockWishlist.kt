package com.example.coollib.ui.previewSupport

import com.example.coollib.data.remote.APIConfig
import com.example.coollib.domain.model.Wishlist

object MockWishlist {

    val list = listOf(
        Wishlist(
            id = 248,
            isbn = "9780812575583",
            title = "Winter's Heart (The Wheel of Time, Book 9)",
            author = "Robert Jordan",
            publisher = "Tor Books",
            year = 2000,
            coverUrl = "${APIConfig.SERVER}/img/cover/9780812575583.webp"
        ),

        Wishlist(
            id = 253,
            isbn = "9780765325969",
            title = "A Memory of Light (The Wheel of Time, Book 14)",
            author = "Robert Jordan & Brandon Sanderson",
            publisher = "Tor Books",
            year = 2013,

            coverUrl = "${APIConfig.SERVER}/img/cover/9780765325969.webp"
        )
    )
}