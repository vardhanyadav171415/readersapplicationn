package com.example.readersapplicationn.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readersapplicationn.data.Resource
import com.example.readersapplicationn.model.Item
import com.example.readersapplicationn.repositary.BookRepositary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepositary) : ViewModel() {
    var list: List<Item> by mutableStateOf(emptyList())
    var isLoading: Boolean by mutableStateOf(false)

    init {
        loadBooks()
    }

    fun loadBooks(query: String = "android") {
        searchBooks(query)
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            isLoading = true

            try {
                when (val response = repository.getBooks(query)) {
                    is Resource.Success -> {
                        list = response.data ?: emptyList()
                    }
                    is Resource.Error -> {
                        Log.e("Network", "searchBooks: Failed getting books - ${response.message}")
                    }
                    else -> {
                        Log.e("Network", "searchBooks: Unexpected result")
                    }
                }
            } catch (exception: Exception) {
                Log.e("Network", "searchBooks: ${exception.message}")
            } finally {
                isLoading = false
            }
        }
    }
}
