package com.example.birthdaylistoblopg.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import java.text.DateFormat

@OptIn(ExperimentalMaterial3Api::class) // TopAppBar
@Composable
fun PersonScreen(
    modifier: Modifier = Modifier,
    user: FirebaseUser?,
    onSignOut: () -> Unit,
    onNavigateToLoginPage: () -> Unit,
    onNavigateToListPage: () -> Unit,
    navigateBack: () -> Unit
) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var remarks by remember { mutableStateOf("") }
    var isDatePickerDialogOpen by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    val dateFormatterLocal = DateFormat.getDateInstance()

    if (user == null) {
        onNavigateToLoginPage()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                title = { Text("Person") },
                actions = {
                    Button(onClick = { onSignOut() }) {
                        Text("Sign Out")
                    }
                })
        }) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Person Details", style = MaterialTheme.typography.headlineLarge)

            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                enabled = false,
                label = { Text("ID") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                //Format Long? to a String
                value = selectedDate?.let { dateFormatterLocal.format(it) } ?: "",
                onValueChange = { },
                label = { Text("DOB") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { isDatePickerDialogOpen = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (isDatePickerDialogOpen) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { /* no reaction */ },
                    confirmButton = {
                        TextButton(onClick = {
                            isDatePickerDialogOpen = false
                            selectedDate = datePickerState.selectedDateMillis
                        })
                        { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { isDatePickerDialogOpen = false })
                        { Text("Cancel") }
                    }) {
                    DatePicker(state = datePickerState)
                }
            }

            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text("Remarks") },
                singleLine = false,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    // TODO check if the word is not empty
                    if (!name.isEmpty()) {
                        onNavigateToListPage()
                    }
                }) {
                    Text("Add")
                }
            }
        }

    }
}


