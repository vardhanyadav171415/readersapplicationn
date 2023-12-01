package com.example.readersapplicationn.screens
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.readersapplicationn.navigation.ReaderScreens
import com.example.readersapplicationn.screens.login.ReaderLoginScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@Composable
fun ReaderSplashScreen(navController: NavHostController) {
   val scale = remember {
      androidx.compose.animation.core.Animatable(0f)
   }

   LaunchedEffect(key1 = true) {
      scale.animateTo(
         targetValue = 0.9f,
         animationSpec = tween(durationMillis = 800, easing = { OvershootInterpolator(8f).getInterpolation(it) })
      )
      delay(2000L)
      if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
         navController.navigate(ReaderScreens.LoginScreen.name)
      } else {
         navController.navigate(ReaderScreens.ReaderHomeScreen.name)
      }
   }

   Box(
      modifier = Modifier
         .fillMaxSize()
         .background(
            brush = Brush.verticalGradient(
               colors = listOf(
                  Color(0xFF1A237E), // Start color
                  Color(0xFF1565C0), // End color
               ),
               startY = 0f,
               endY = 500f
            )
         ),
      contentAlignment = Alignment.Center
   ) {
      Surface(
         modifier = Modifier
            .size(330.dp)
            .scale(scale.value),
         shape = CircleShape,
         color = Color.White.copy(alpha = 0.9f),
         border = BorderStroke(width = 2.dp, color = Color.LightGray)
      ) {
         Column(
            modifier = Modifier
               .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
         ) {
            ReaderLogo(modifier = Modifier.alpha(0.8f))
            Spacer(modifier = Modifier.height(15.dp))
            Text(
               text = "\"Read. Change. Yourself \"",
               style = MaterialTheme.typography.h5,
               color = Color.LightGray
            )
         }
      }
   }
}

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
   Text(
      modifier = modifier.padding(bottom = 16.dp),
      text = "A. Reader",
      style = MaterialTheme.typography.h3,
      color = Color.Red.copy(alpha = 0.8f)
   )
}

