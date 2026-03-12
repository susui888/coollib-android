package com.example.coollib.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.domain.model.Book
import com.example.coollib.domain.model.Category
import com.example.coollib.domain.usecase.BookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        private val bookUseCase: BookUseCase
) : ViewModel() {
    private val _category = MutableStateFlow<List<Category>>(emptyList())
    val category: StateFlow<List<Category>> = _category.asStateFlow()

    init {
        loadCategory()
    }

    fun loadCategory(){
        viewModelScope.launch {
            _category.value = bookUseCase.getCategory()
        }
    }

    val lastViewBooks: StateFlow<List<Book>> = bookUseCase.getBooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

}