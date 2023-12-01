package com.example.readersapplicationn.screens.login

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.readersapplicationn.components.UserForm
import com.example.readersapplicationn.navigation.ReaderScreens
import com.example.readersapplicationn.screens.ReaderLogo
import com.example.readersapplicationn.screens.RecoverPassword

@Composable
fun ReaderLoginScreen(navController: NavHostController,viewModel: LoginScreenViewModel= androidx.lifecycle.viewmodel.compose.viewModel()) {
    val showLoginForm = rememberSaveable{
        mutableStateOf(false)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
            ReaderLogo()
            if(showLoginForm.value) UserForm(loading = false, isCreateAccount = false){email,password->
                viewModel.signInUserWithEmailAndPassword(email=email,password=password){
                    navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                }
            }

            else{
                UserForm(loading = false, isCreateAccount = true){email,password->
                    viewModel.createUserWithEmailAndPassword(email,password){
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            if(showLoginForm.value) Text(text = "Forgot Password", modifier = Modifier
                .padding(start = 230.dp)
                .clickable { navController.navigate(ReaderScreens.RecoverPassword.name) },color= Color.Blue)
            Row(modifier=Modifier.padding(15.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                val text=if(showLoginForm.value) "Sign up" else "Login"
                Text(text="New User?")
                Text(text = text,
                    Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp), fontWeight = FontWeight.Bold,color=MaterialTheme.colors.secondaryVariant)

            }
        }


    }
}

