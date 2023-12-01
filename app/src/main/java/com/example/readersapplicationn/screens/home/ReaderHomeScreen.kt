package com.example.readersapplicationn.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.readersapplicationn.components.*
import com.example.readersapplicationn.model.MBook
import com.example.readersapplicationn.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import java.io.Reader

@Composable
fun ReaderHomeScreen(navController: NavHostController,viewModel: HomeScreenViewModel= hiltViewModel()) {
     Scaffold(topBar = { ReaderAppBar(title ="A.Reader" , navController = navController)}, floatingActionButton = {
         FABContent{
             navController.navigate(ReaderScreens.SearchScreen.name)
         }
     }) {
          Surface(modifier = Modifier.fillMaxSize()) {
              HomeContent(navController,viewModel=viewModel)
          }
     }
}

@Composable
fun HomeContent(navController: NavHostController,viewModel: HomeScreenViewModel) {
    var listOfBooks = emptyList<MBook>()
    val currentUser=FirebaseAuth.getInstance().currentUser
    if(!viewModel.data.value.data.isNullOrEmpty()){
        listOfBooks= viewModel.data.value?.data!!.toList()!!.filter { mBook ->
        mBook.userId==currentUser?.uid.toString()

        }
    }
    val currentUserName=if(!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) else
            "N/A"
    Column(modifier = Modifier.padding(2.dp), verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)) {
            TitleSection(label = "Your Reading .\n" + "Activity Right Now")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant
                )
                Text(
                    text = currentUserName!!,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()

            }
        }
        ReadingRightNowArea(books = listOfBooks, navController = navController)
        TitleSection(label = "Reading List")
        BookListArea(listOfBooks= listOfBooks,navController=navController)
    }


}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavHostController) {
   val addedBooks = listOfBooks.filter { mBook ->
       mBook.startedReading==null && mBook.finishedReading==null
   }
    HorizontalScollableContent(addedBooks){
          navController.navigate(ReaderScreens.UpdateScreen.name  + "/$it")
   }
}

@Composable
fun HorizontalScollableContent(listOfBooks: List<MBook>,
                               viewModel: HomeScreenViewModel= hiltViewModel(),onCardPressed:(String)->Unit) {
    val scrollState= rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(280.dp)
        .horizontalScroll(scrollState)){
        if(viewModel.data.value.loading==true){
            LinearProgressIndicator()
        }
        else{
            if(listOfBooks.isNullOrEmpty()){
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(text = "No books found. Add a book",
                     style=TextStyle(
                         color=Color.Red.copy(alpha = 0.4f),
                         fontWeight = FontWeight.Bold,
                         fontSize = 14.sp,

                     ))

                }
            }else{

            }
        }
        for (book in listOfBooks){
            ListCard(book){
            onCardPressed(book.googleBookId.toString())

            }
        }
    }
}


@Composable
fun ReadingRightNowArea(books:List<MBook>, navController: NavHostController){
    val readingNowList = books.filter { mBook ->
        mBook.startedReading !=null && mBook.finishedReading==null
    }
    HorizontalScollableContent(listOfBooks = readingNowList){
          navController.navigate(ReaderScreens.UpdateScreen.name +"/$it")
    }
}

@Composable
fun ListCard(book:MBook,onPressDetails:(String)->Unit={}) {
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp
    Card(shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White,
        elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable { onPressDetails.invoke(book.title.toString()) }) {

        Column(
            modifier = Modifier.width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            Row(horizontalArrangement = Arrangement.Center) {

                Image(
                    painter = rememberImagePainter(data = book.photoUrl.toString()),
                    contentDescription = "book image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(50.dp))

                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav Icon",
                        modifier = Modifier.padding(bottom = 1.dp)
                    )

                    BookRating(score = book.rating!!)
                }

            }
            Text(
                text = book.title.toString(), modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = book.authors.toString(), modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.caption
            )
        }

        val isStartedReading = remember{
            mutableStateOf(false)
        }
        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
            isStartedReading.value = book.startedReading !=null
            RoundedButton(label =if(isStartedReading.value) "Reading" else "Not Started", radius = 70)

        }

    }
}





