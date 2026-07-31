package com.dreamcode.networkrequestwithretrofitinandroid.ui.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dreamcode.networkrequestwithretrofitinandroid.model.MovieReview
import com.dreamcode.networkrequestwithretrofitinandroid.networking.MovieDiaryApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
  movieDiaryApi: MovieDiaryApi,
  onProfileTapped: () -> Unit,
) {
  val screenScope = rememberCoroutineScope()
  val snackbarHostState = remember { SnackbarHostState() }
  var openDialog by remember { mutableStateOf(false) }
  var movieReviewList by remember { mutableStateOf<List<MovieReview>>(emptyList()) }

  LaunchedEffect(Unit) {
    movieDiaryApi.getMovies { movies, error ->
      if (!movies.isNullOrEmpty()) {
        movieReviewList = movies
      } else {
        screenScope.launch {
          snackbarHostState.showSnackbar(error?.message ?: "An error occurred")
        }
      }
    }
  }

  if (openDialog) {
    NewEntryDialog(
      onDismissRequest = { openDialog = false },
      onConfirmation = { movieReview ->
        movieDiaryApi.postReview(movieReview, onResponse = { newReview, error ->
          if (newReview != null) {
            val newList = movieReviewList.toMutableList()
            newList.add(newReview)
            movieReviewList = newList
          } else {
            screenScope.launch {
              snackbarHostState.showSnackbar(error?.message ?: "An error occurred")
            }
          }
        })
        openDialog = false
      },
    )
  }
  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
    TopAppBar(title = {
      Text(text = "MovieDiary")
    }, actions = {
      IconButton(onClick = {
        screenScope.launch { onProfileTapped() }
      }) {
        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Profile icon")
      }
    })
  }, floatingActionButton = {
    FloatingActionButton(onClick = { openDialog = true }) {
      Icon(imageVector = Icons.Default.Add, contentDescription = "Add new entry")
    }
  }) { paddingValues ->
    Column(modifier = Modifier.padding(paddingValues)) {
      LazyColumn(content = {
        items(movieReviewList.size) { index ->
          Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
            MovieItem(movieReviewList[index])
          }
        }
      })
    }
  }
}