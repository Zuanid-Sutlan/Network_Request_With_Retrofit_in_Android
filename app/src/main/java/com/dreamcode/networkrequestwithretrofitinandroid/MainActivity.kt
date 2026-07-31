package com.dreamcode.networkrequestwithretrofitinandroid

import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.getSystemService
import com.dreamcode.networkrequestwithretrofitinandroid.networking.ConnectivityChecker
import com.dreamcode.networkrequestwithretrofitinandroid.ui.App
import com.dreamcode.networkrequestwithretrofitinandroid.ui.login.LoginScreen
import com.dreamcode.networkrequestwithretrofitinandroid.ui.movies.MoviesScreen
import com.dreamcode.networkrequestwithretrofitinandroid.ui.navigation.Screens
import com.dreamcode.networkrequestwithretrofitinandroid.ui.profile.ProfileScreen
import com.dreamcode.networkrequestwithretrofitinandroid.ui.register.RegisterScreen
import com.dreamcode.networkrequestwithretrofitinandroid.ui.theme.NetworkRequestWithRetrofitInAndroidTheme

class MainActivity : ComponentActivity() {

    private val connectivityManager by lazy { getSystemService<ConnectivityManager>() }
    private val connectivityChecker by lazy { ConnectivityChecker(connectivityManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember { mutableStateOf(Screens.LOGIN) }
            var userLoggedIn by remember { mutableStateOf(false) }

            NetworkRequestWithRetrofitInAndroidTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    if (userLoggedIn) currentScreen = Screens.MOVIES
                    
                    when (currentScreen) {
                        Screens.LOGIN -> {
                            LoginScreen(
                                App.movieApi,
                                connectivityChecker,
                                onLogin = { token ->
                                    App.saveUserToken(token)
                                    userLoggedIn = true
                                    currentScreen = Screens.MOVIES
                                },
                                onRegisterTapped = { currentScreen = Screens.REGISTER }
                            )
                        }

                        Screens.REGISTER -> {
                            RegisterScreen(
                                movieDiaryApi = App.movieApi,
                                connectivityChecker = connectivityChecker,
                                onUserRegistered = { currentScreen = Screens.LOGIN },
                                onLoginTapped = { currentScreen = Screens.LOGIN })
                        }

                        Screens.MOVIES -> {
                            MoviesScreen(
                                movieDiaryApi = App.movieApi,
                                onProfileTapped = { currentScreen = Screens.PROFILE },
                            )
                        }

                        Screens.PROFILE -> {
                            ProfileScreen(
                                movieDiaryApi = App.movieApi,
                                onBack = { currentScreen = Screens.MOVIES },
                                onLogout = {
                                    App.saveUserToken("")
                                    App.saveRefreshToken(0L)
                                    currentScreen = Screens.LOGIN
                                })
                        }
                    }
                }
            }
        }
    }
}