package com.example.coollib.ui.previewSupport

import com.example.coollib.domain.model.Review
import com.example.coollib.domain.model.Book
import java.time.Instant

object MockReviews {

    val list = listOf(
        Review(
            id = 1,
            bookId = 253,
            book = Book(
                id = 253,
                title = "Don Quixote",
                author = "Miguel de Cervantes",
                isbn = "9780060934347",
                description = ""
            ),
            userId = 101,
            userName = "Susui",
            rating = 5,
            content = "A timeless masterpiece! The struggle of Don Quixote is both hilarious and deeply moving.",
            createdAt = Instant.parse("2026-01-15T10:30:00Z")
        ),
        Review(
            id = 2,
            bookId = 263,
            book = Book(
                id = 263,
                title = "The Great Gatsby",
                author = "F. Scott Fitzgerald",
                isbn = "9780743273565",
                description = ""
            ),
            userId = 102,
            userName = "cake",
            rating = 4,
            content = "Very long but definitely worth the read. The translation in this edition is excellent.",
            createdAt = Instant.parse("2026-02-20T14:20:00Z")
        ),
        Review(
            id = 3,
            bookId = 264,
            book = Book(
                id = 264,
                title = "Clean Code",
                author = "Robert C. Martin",
                isbn = "9780132350884",
                description = ""
            ),
            userId = 103,
            userName = "ryan",
            rating = 5,
            content = "Essential reading for every software engineer. It changed the way I think about variables and functions.",
            createdAt = Instant.parse("2026-03-05T09:00:00Z")
        ),
        Review(
            id = 4,
            bookId = 265,
            book = Book(
                id = 265,
                title = "A Brief History of Time",
                author = "Stephen Hawking",
                isbn = "9780553380163",
                description = ""
            ),
            userId = 104,
            userName = "Susui",
            rating = 4,
            content = "Mind-blowing concepts made relatively accessible. Some parts are still quite dense though!",
            createdAt = Instant.parse("2026-04-10T18:45:00Z")
        ),
        Review(
            id = 5,
            bookId = 243,
            book = Book(
                id = 243,
                title = "The Shadow Rising",
                author = "Robert Jordan",
                isbn = "9780312854317",
                description = ""
            ),
            userId = 105,
            userName = "Susui",
            rating = 5,
            content = "Best book in the series so far! The world-building is just incredible.",
            createdAt = Instant.parse("2026-04-25T21:10:00Z")
        )
    )

    fun getForBook(bookId: Int): List<Review> = list.filter { it.bookId == bookId }
}