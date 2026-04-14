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
import com.example.birthdaylistoblopg.data.Person
import com.google.firebase.auth.FirebaseUser
import java.text.DateFormat
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class) // TopAppBar
@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    user: FirebaseUser?,
    onSignOut: () -> Unit,
    onNavigateToLoginPage: () -> Unit,
    onNavigateToListPage: () -> Unit,
    navigateBack: () -> Unit,
    addPerson: (Person) -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var remarks by remember { mutableStateOf("") }
    var isDatePickerDialogOpen by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    val dateFormatterLocal = DateFormat.getDateInstance()
    var nameIsError by remember { mutableStateOf(false) }
    var dateIsError by remember { mutableStateOf(false) }

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
                actions = {
                    Button(onClick = { onSignOut() }) {
                        Text("Sign Out")
                    }
                },
                title = { Text("Add Birthday") })
        }) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Add Birthday", style = MaterialTheme.typography.headlineLarge)
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameIsError = false
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text("Name") },
                isError = nameIsError,
                supportingText = {
                    if (nameIsError) {
                        Text(text = "Name is required")
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                //Format Long? to a String
                value = selectedDate?.let { dateFormatterLocal.format(it) } ?: "",
                onValueChange = { dateIsError = false },
                label = { Text("DOB") },
                isError = dateIsError,
                supportingText = {
                    if (dateIsError) {
                        Text(text = "Date of birth is required")
                    }
                },
                enabled = true,
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
                    if (name.isEmpty()) {
                        nameIsError = true
                        return@Button
                    }
                    if (selectedDate == null) {
                        dateIsError = true
                        return@Button
                    }
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = selectedDate!!
                    }
                    val birthYear = calendar.get(Calendar.YEAR)
                    val birthMonth = calendar.get(Calendar.MONTH) + 1
                    val birthDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                    val person = Person(
                        userId = "testpass@mail.dk",
                        remarks = remarks,
                        name = name,
                        age = 0,
                        birthYear = birthYear,
                        birthMonth = birthMonth,
                        birthDayOfMonth = birthDayOfMonth
                    )
                    addPerson(person)
                    onNavigateToListPage()
                }) {
                    Text("Add")
                }
            }
        }

    }
}


