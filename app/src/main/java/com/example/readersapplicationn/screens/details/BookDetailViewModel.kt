package com.example.readersapplicationn.screens.details

import androidx.lifecycle.ViewModel
import com.example.readersapplicationn.model.Item
import com.example.readersapplicationn.repositary.BookRepositary
import com.google.rpc.context.AttributeContext.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(private val repositary: BookRepositary) :ViewModel()
{
    suspend fun getBookInfo(bookId:String) : com.example.readersapplicationn.data.Resource<Item>{
        return repositary.getBookInfo(bookId = bookId)
    }
}
