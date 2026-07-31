package com.dreamcode.networkrequestwithretrofitinandroid.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dreamcode.networkrequestwithretrofitinandroid.model.User
import com.dreamcode.networkrequestwithretrofitinandroid.networking.MovieDiaryApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
  movieDiaryApi: MovieDiaryApi,
  onBack: () -> Unit,
  onLogout: () -> Unit,
) {
  val screenScope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }
  var user by remember { mutableStateOf(User("", "")) }
  LaunchedEffect(Unit) {
    movieDiaryApi.getProfile { userResponse, throwable ->
      if (userResponse != null) {
        user = userResponse
      } else {
        screenScope.launch {
          snackbarHostState.showSnackbar(throwable?.message ?: "An error occurred")
        }
      }
    }
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
    TopAppBar(
      title = {
        Text(text = "MovieDiary")
      },
      navigationIcon = {
        IconButton(onClick = { onBack() }) {
          Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
        }
      },
      actions = {
        IconButton(
          onClick = {
            screenScope.launch { onLogout() }
          }) {
          Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Profile icon")
        }
      }
    )
  }
  ) { paddingValues ->
    Column(Modifier.padding(paddingValues)) {
      Card(
        Modifier
          .padding(12.dp)
          .fillMaxWidth()
      ) {
        Column(Modifier.padding(20.dp)) {
          Text(text = user.username)
          Text(text = user.email)
        }
      }
    }
  }
}