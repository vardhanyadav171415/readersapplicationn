package com.example.readersapplicationn.components

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.readersapplicationn.model.Item
import com.example.readersapplicationn.model.MBook
import com.example.readersapplicationn.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TitleSection(modifier: Modifier = Modifier, label:String){
    Surface(modifier = Modifier.padding(start = 5.dp, end = 1.dp)) {
        Column {
            Text(text =label, fontSize = 19.sp, fontStyle = FontStyle.Normal, textAlign = TextAlign.Left
            )

        }
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun UserForm(loading:Boolean=false,isCreateAccount:Boolean=false,
             onDone:(String,String) -> Unit={email,pwd->}) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())
        .height(250.dp)
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if(isCreateAccount) Text("Please enter a valid email and password that is at least 6 characters", modifier = Modifier.padding(3.dp)) else ""
        emailInput(emailState = email, enabled = true, onAction = KeyboardActions {
            passwordFocusRequest.requestFocus()
        })
        PasswordInput(modifier=Modifier.focusRequester(passwordFocusRequest),
            passwordState=password,
            labelId="Password",
            enabled=true,
            passwordVisibility=passwordVisibility,onAction= KeyboardActions{
                if(!valid) return@KeyboardActions
                onDone(email.value.trim(),password.value.trim())
            })

        SubmitButton(textId = if(isCreateAccount)"Create Account" else "Login",loading=loading,
            validInputs=valid){
            onDone(email.value.trim(),password.value.trim())
            keyboardController?.hide()
        }

    }

}

@Composable
fun SubmitButton(textId: String, loading: Boolean, validInputs: Boolean,
                 onClick:()->Unit) {
    Button(onClick = onClick, modifier = Modifier
        .padding(3.dp)
        .fillMaxWidth(), enabled = !loading && validInputs, shape = CircleShape
    ) {
        if(loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text=textId, modifier = Modifier.padding(5.dp))

    }
}

@Composable
fun PasswordInput(modifier: Modifier, passwordState: MutableState<String>, labelId: String, enabled: Boolean, passwordVisibility: MutableState<Boolean>,
                  imeAction: ImeAction = ImeAction.Done,
                  onAction: KeyboardActions = KeyboardActions.Default) {
    val visualTransformation = if(passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        label = { Text(labelId) },
        singleLine = true,
        enabled = enabled,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction), visualTransformation =visualTransformation, trailingIcon ={PasswordVisibility(passwordVisibility=passwordVisibility)})
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value= !visible }) {
        Icons.Default.Close
    }
}


@Composable
fun emailInput(modifier: Modifier=Modifier,
               emailState: MutableState<String>,
               labelId:String="Email",
               enabled:Boolean=true,
               imeAction: ImeAction = ImeAction.Next,
               onAction: KeyboardActions = KeyboardActions.Default){
    InputField(modifier=modifier,
        valueState = emailState,
        labelId=labelId,
        enabled=enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction)

}

@Composable
fun InputField(modifier: Modifier=Modifier,
               valueState: MutableState<String>,
               labelId: String,
               enabled: Boolean,
               isSingleLine:Boolean=true,
               keyboardType: KeyboardType = KeyboardType.Text,
               imeAction: ImeAction = ImeAction.Next,
               onAction: KeyboardActions = KeyboardActions.Default) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(labelId) },
        singleLine = isSingleLine,
        enabled = enabled,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )


}


@Composable
fun ReaderAppBar(title:String,
                 icon: ImageVector?=null,
                 showProfile:Boolean=true,
                 navController: NavController,
                 onBackArrowClicked:()->Unit={}
){
    TopAppBar(title={
        Row(verticalAlignment = Alignment.CenterVertically){
            if(showProfile){
                Image(imageVector = Icons.Default.LibraryBooks, contentDescription ="Logo Icons",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .scale(0.6f))
            }
            if(icon!=null){
                Icon(imageVector = icon, contentDescription = "icon", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.clickable { onBackArrowClicked.invoke() })
            }
            Text(text = title,color= Color.Red.copy(alpha = 0.7f), style = TextStyle(fontSize = 20.sp), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(40.dp))

        }
    }, actions = {
        IconButton(onClick = {
            FirebaseAuth.getInstance().signOut().run {
                navController.navigate(ReaderScreens.LoginScreen.name)
            }
        }) {
            if(showProfile) Row(){
                Icon(
                    imageVector = Icons.Filled.Logout, contentDescription = "Logout Icon",
                    tint = Color.Green.copy(alpha = 0.4f)
                )
            }else Box(){

            }
        }
    }, backgroundColor = Color.Transparent, elevation = 0.dp)

}

@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 29,
    onPress: () -> Unit = {}) {
    Surface(modifier = Modifier.clip(RoundedCornerShape(
        bottomEndPercent = radius,
        topStartPercent = radius)),
        color = Color(0xFF92CBDF)) {

        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = TextStyle(color = Color.White,
                fontSize = 15.sp),)

        }

    }


}

@Composable
fun BookRating(score: Double) {
    Surface(modifier = Modifier
        .padding(4.dp)
        .height(70.dp), shape = RoundedCornerShape(56.dp), elevation = 6.dp, color =Color.White ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.Filled.StarBorder,contentDescription = "Star Border", modifier = Modifier.padding(3.dp))
            Text(text = score.toString(), style = MaterialTheme.typography.subtitle1)
        }
    }
}


@Composable
fun FABContent(onTap: (String) -> Unit) {
    FloatingActionButton(onClick = { onTap("") }, shape =
    RoundedCornerShape(50.dp),
        backgroundColor = Color(0xFF92CBDF)
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription =
        "Book", tint = Color.White )
    }

}

 
@Composable
fun BookRow(book: Item, navController: NavController){
    Card(
        modifier = Modifier
            .clickable {
                navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
            }
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp) {
        Row(modifier=Modifier.padding(5.dp), verticalAlignment = Alignment.Top){
            val imageUrl:String = if (book.volumeInfo.imageLinks.smallThumbnail.isEmpty()==true){
                "https://sg-res.9appsdownloading.com/sg/res/jpg/2f/70/ecf6cd6a0de7cbbd207ef902f4af-ej8.jpg?x-oss-process=style/mq200"
            }
            else{
                book.volumeInfo.imageLinks.smallThumbnail
            }
            Image(painter= rememberImagePainter(data = imageUrl), contentDescription ="book image", modifier = Modifier.width(80.dp).fillMaxHeight().padding(end = 4.dp))
            Column {
                Text(text =book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                Text(text ="Author: ${book.volumeInfo.authors}", overflow = TextOverflow.Clip,fontStyle= FontStyle.Italic, style = MaterialTheme.typography.caption)
                Text(text ="Author: ${book.volumeInfo.publishedDate}", overflow = TextOverflow.Clip,fontStyle= FontStyle.Italic, style = MaterialTheme.typography.caption)
                Text(text ="Author: ${book.volumeInfo.categories}", overflow = TextOverflow.Clip,fontStyle= FontStyle.Italic, style = MaterialTheme.typography.caption)

            }
        }
    }
}