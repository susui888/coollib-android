package com.example.coollib.ui.screens.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Category
import com.example.coollib.domain.model.SearchQuery
import com.example.coollib.domain.usecase.BookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookUseCase: BookUseCase
) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()
    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook.asStateFlow()


    fun searchBooks(query: SearchQuery) {
        viewModelScope.launch {
            _books.value = bookUseCase.searchBooks(query)
        }
    }

    fun selectBook(id: Int) {
        viewModelScope.launch {
            _selectedBook.value = bookUseCase.getBookById(id)
        }
    }


}