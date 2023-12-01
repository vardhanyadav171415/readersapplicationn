package com.example.readersapplicationn.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.readersapplicationn.ReaderApp
import com.example.readersapplicationn.components.BookRow
import com.example.readersapplicationn.components.InputField
import com.example.readersapplicationn.components.ReaderAppBar
import com.example.readersapplicationn.model.MBook

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReaderSearchScreen (navController: NavController,viewModel: BookSearchViewModel= hiltViewModel()){
    Scaffold(topBar = {ReaderAppBar(title = "Search Books",icon= Icons.Default.ArrowBack, navController =navController, showProfile = false){
        navController.popBackStack()
    }}) {
        Surface {
            Column {
                SearchForm(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)) { searchQuery ->
                    viewModel.loadBooks(query = searchQuery)
                }
                Spacer(modifier = Modifier.height(13.dp))
                BookList(navController = navController, viewModel = viewModel)
            }
        }

    }

}

@Composable
fun BookList(navController: NavController, viewModel: BookSearchViewModel = hiltViewModel()) {
    val listOfBooks = viewModel.list

    if (viewModel.isLoading) {
        LoadingIndicator()
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            items(items = listOfBooks) { book ->
                BookRow(book = book, navController = navController)
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Row(
        modifier = Modifier
            .padding(end = 2.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator()
        Text(text = "Loading...")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column {
        val searchQueryState = remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = searchQueryState.value,
            onValueChange = {
                searchQueryState.value = it
            },
            label = { Text("Search") },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    if (searchQueryState.value.trim().isNotEmpty()) {
                        onSearch(searchQueryState.value.trim())
                        searchQueryState.value = ""
                        keyboardController?.hide()
                    }
                }
            ),
            modifier = modifier
        )
    }
}
