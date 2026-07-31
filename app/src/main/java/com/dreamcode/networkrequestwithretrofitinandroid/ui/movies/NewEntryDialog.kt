package com.dreamcode.networkrequestwithretrofitinandroid.ui.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dreamcode.networkrequestwithretrofitinandroid.model.MovieReview

@Composable
fun NewEntryDialog(
  onDismissRequest: () -> Unit,
  onConfirmation: (MovieReview) -> Unit,
) {

  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var comment by remember { mutableStateOf("") }

  Dialog(onDismissRequest = { onDismissRequest() }) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .height(375.dp)
        .padding(16.dp),
      shape = RoundedCornerShape(16.dp),
    ) {
      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxSize()
          .padding(12.dp)
      ) {
        OutlinedTextField(
          modifier = Modifier.padding(8.dp),
          value = title,
          onValueChange = { title = it },
          label = { Text("Title") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
          modifier = Modifier.padding(8.dp),
          value = description,
          onValueChange = { description = it },
          label = { Text(text = "Description") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
          modifier = Modifier.padding(8.dp),
          value = comment,
          onValueChange = { comment = it },
          label = { Text("Comment") },
          singleLine = true,
        )
        Button(onClick = {
          if (title.isNotBlank() && description.isNotBlank() && comment.isNotBlank()) {
            onConfirmation(MovieReview(title, description, comment))
          }
        }) {
          Text(text = "Add")
        }
      }
    }
  }
}