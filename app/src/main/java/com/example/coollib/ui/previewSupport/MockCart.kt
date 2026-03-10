package com.example.coollib.ui.previewSupport

import com.example.coollib.data.remote.APIConfig
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Cart

object MockCart {

    val list = listOf(

        Cart(
            id = 274,
            isbn = "9780142437230",
            title = "Don Quixote",
            author = "Miguel de Cervantes",
            publisher = "Penguin Classics",
            year = 1605,
            coverUrl = "${APIConfig.BASEURL}/img/cover/9780142437230.webp"
        ),

        Cart(
            id = 265,
            isbn = "9780553380163",
            title = "A Brief History of Time",
            author = "Stephen Hawking",
            publisher = "Bantam",
            year = 1988,
            coverUrl = "${APIConfig.BASEURL}/img/cover/9780553380163.webp"
        ),

        Cart(
            id = 270,
            isbn = "9780132350884",
            title = "Clean Code",
            author = "Robert C. Martin",
            publisher = "Prentice Hall",
            year = 2008,
            coverUrl = "${APIConfig.BASEURL}/img/cover/9780132350884.webp"
        ),

        Cart(
            id = 243,
            isbn = "9780812513738",
            title = "The Shadow Rising (The Wheel of Time, Book 4)",
            author = "Robert Jordan",
            publisher = "Tor Books",
            year = 1992,
           coverUrl = "${APIConfig.BASEURL}/img/cover/9780812513738.webp"
        ),
    )
}