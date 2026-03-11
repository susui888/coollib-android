package com.example.coollib.domain.model


enum class SearchType {
    ALL, CATEGORY, AUTHOR, PUBLISHER, YEAR, SEARCH
}

data class SearchQuery(
    val category: Int? = null,
    val author: String? = null,
    val publisher: String? = null,
    val year: Int? = null,
    val searchTerm: String? = null
){
    val searchType: SearchType
        get() = when {
            category != null -> SearchType.CATEGORY
            author != null -> SearchType.AUTHOR
            publisher != null -> SearchType.PUBLISHER
            year != null -> SearchType.YEAR
            !searchTerm.isNullOrBlank() -> SearchType.SEARCH
            else -> SearchType.ALL
        }
}

