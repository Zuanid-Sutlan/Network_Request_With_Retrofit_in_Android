package com.dreamcode.networkrequestwithretrofitinandroid.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.dreamcode.networkrequestwithretrofitinandroid.networking.ConnectivityChecker
import com.dreamcode.networkrequestwithretrofitinandroid.networking.MovieDiaryApi
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
  movieDiaryApi: MovieDiaryApi,
  connectivityChecker: ConnectivityChecker,
  onLogin: (String) -> Unit,
  onRegisterTapped: () -> Unit,
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val screenScope = rememberCoroutineScope()
  val focusManager = LocalFocusManager.current

  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }


  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { innerPadding ->
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()
    ) {
      OutlinedTextField(
        modifier = Modifier.padding(8.dp),
        value = username,
        onValueChange = { username = it },
        label = { Text("Username") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
      )
      OutlinedTextField(
        modifier = Modifier.padding(8.dp),
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
      )
      Button(onClick = {
        focusManager.clearFocus()
        screenScope.launch {
          if (username.isNotBlank() && password.isNotBlank()) {
            if (connectivityChecker.hasNetworkConnected()) {
              movieDiaryApi.loginUser(username, password) { token, error ->
                if (token == null) {
                  screenScope.launch {
                    snackbarHostState.showSnackbar(error?.message ?: "An error occurred")
                  }
                } else {
                  onLogin(token)
                }
              }
            } else {
              snackbarHostState.showSnackbar("Check your network connection.")
            }
          } else {
            screenScope.launch {
              snackbarHostState.showSnackbar("Please fill in all the fields.")
            }
          }
        }
      }) {
        Text(text = "Login")
      }

      Spacer(modifier = Modifier.size(40.dp))

      Text(text = "Don't have an account yet?")

      TextButton(onClick = { onRegisterTapped() }) {
        Text("Register")
      }
    }
  }
}