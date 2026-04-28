package com.example.coollib.ui.previewSupport

import com.example.coollib.domain.model.Review
import java.time.Instant

object MockReviews {

    val list = listOf(
        Review(
            id = 1,
            bookId = 253, // Don Quixote
            userId = 101,
            userName = "Susui",
            rating = 5,
            content = "A timeless masterpiece! The struggle of Don Quixote is both hilarious and deeply moving.",
            createdAt = Instant.parse("2026-01-15T10:30:00Z")
        ),
        Review(
            id = 2,
            bookId = 263,
            userId = 102,
            userName = "cake",
            rating = 4,
            content = "Very long but definitely worth the read. The translation in this edition is excellent.",
            createdAt = Instant.parse("2026-02-20T14:20:00Z")
        ),
        Review(
            id = 3,
            bookId = 264, // Clean Code
            userId = 103,
            userName = "ryan",
            rating = 5,
            content = "Essential reading for every software engineer. It changed the way I think about variables and functions.",
            createdAt = Instant.parse("2026-03-05T09:00:00Z")
        ),
        Review(
            id = 4,
            bookId = 265, // A Brief History of Time
            userId = 104,
            userName = "Susui",
            rating = 4,
            content = "Mind-blowing concepts made relatively accessible. Some parts are still quite dense though!",
            createdAt = Instant.parse("2026-04-10T18:45:00Z")
        ),
        Review(
            id = 5,
            bookId = 243, // The Shadow Rising
            userId = 105,
            userName = "Susui",
            rating = 5,
            content = "Best book in the series so far! The world-building is just incredible.",
            createdAt = Instant.parse("2026-04-25T21:10:00Z")
        )
    )


    fun getForBook(bookId: Int): List<Review> = list.filter { it.bookId == bookId }
}