package com.example.readersapplicationn.repositary

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.readersapplicationn.data.DataOrException
import com.example.readersapplicationn.data.Resource
import com.example.readersapplicationn.model.Book
import com.example.readersapplicationn.model.Item
import com.example.readersapplicationn.network.BookApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BookRepositary @Inject constructor(private val bookApi: BookApi) {
//    private val bookInfodataOrException = DataOrException<Item,Boolean,java.lang.Exception>()
//
//    private val dataOrException = DataOrException<List<Item>,Boolean,java.lang.Exception>()
//    suspend fun getBooks(searchQuery:String) : DataOrException<List<Item>,
//            Boolean,Exception>{
//        try {
//            dataOrException.loading=true
//            dataOrException.data=bookApi.getAllBooks(searchQuery).items
//            if(dataOrException.data!!.isNotEmpty()) dataOrException.loading=false
//
//        }catch (e:Exception){
//            dataOrException.e=e
//        }
//        return dataOrException
//    }
//    suspend fun getBookInfo(bookId:String):DataOrException<Item,Boolean,Exception>{
//        val response = try{
//            bookInfodataOrException.loading=true
//            bookInfodataOrException.data=bookApi.getBookInfo(bookId = bookId)
//           if(bookInfodataOrException.data.toString().isNotEmpty()) bookInfodataOrException.loading=false
//           else{}
//        }catch (e:Exception){
//            dataOrException.e=e
//        }
//        return bookInfodataOrException
//    }
//    suspend fun getBooks(searchQuery:String):Resource<List<Item>>{
//        return try {
//            Resource.Loading(data = "Loading...")
//            val itemList=bookApi.getAllBooks(searchQuery).items
//            if(itemList.isNotEmpty()) Resource.Loading(data = false)
//            Resource.Success(data = itemList)
//        }catch (exception:Exception){
//            Resource.Error(message = exception.message.toString())
//        }
//
//    }
//    suspend fun getBookInfo(bookId:String):Resource<Item>{
//        val response=try{
//            Resource.Loading(data = true)
//            bookApi.getBookInfo(bookId = bookId)
//        }catch (exception:Exception){
//            return Resource.Error(message = "An Error occured ${exception.message.toString()}")
//        }
//        Resource.Loading(data = false)
//        return Resource.Success(data = response)
//    }
suspend fun getBooks(searchQuery: String): Resource<List<Item>> {
    return try {
        Log.d("Network", "getBooks: Calling API for query: $searchQuery")
     
        Resource.Loading(data = "Loading...")

        val itemList = bookApi.getAllBooks(searchQuery).items

        Log.d("Network", "getBooks: API call successful. Item count: ${itemList.size}")

        if (itemList.isNotEmpty()) {
            Resource.Success(data = itemList)
        } else {
            Resource.Error(message = "No items found.")
        }
    } catch (exception: Exception) {
        Log.e("Network", "getBooks: API call failed - ${exception.message}")
        Resource.Error(message = exception.message.toString())
    }
}


    suspend fun getBookInfo(bookId: String): Resource<Item> {
        return try {
            withContext(Dispatchers.IO) {
                Resource.Loading(data = true)
                val response = bookApi.getBookInfo(bookId = bookId)
                Resource.Loading(data = false)
                Resource.Success(data = response)
            }
        } catch (exception: Exception) {
            Resource.Error(message = "An Error occurred ${exception.message.toString()}")
        }
    }

}