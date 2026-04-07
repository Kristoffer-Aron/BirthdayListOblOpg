package com.example.birthdaylistoblopg.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.birthdaylistoblopg.data.Person


enum class SortOrder { NAME, AGE, BIRTHDAY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    onNavigateToAddPage: () -> Unit = {},
    onNavigateToEditPage: () -> Unit = {}
) {
    var filter by remember { mutableStateOf("") }
    var sortOrder by remember { mutableStateOf(SortOrder.NAME) }

    val people = remember {
        listOf(
            Person("John Doe", 25, "1999-05-15"),
            Person("Jane Smith", 30, "1994-11-20"),
            Person("Alice Johnson", 22, "2002-01-10"),
            Person("Bob Brown", 28, "1996-08-05")
        )
    }

    val filteredAndSortedPeople = people
        .filter { it.name.contains(filter, ignoreCase = true) }
        .sortedWith(compareBy {
            when (sortOrder) {
                SortOrder.NAME -> it.name
                SortOrder.AGE -> it.age
                SortOrder.BIRTHDAY -> it.birthday
            }
        })

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("My Friends") })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = "Friendlist", style = MaterialTheme.typography.headlineLarge)
                Button(onClick = { onNavigateToAddPage() }) {
                    Text("Add")
                }
            }
            OutlinedTextField(
                value = filter,
                onValueChange = { filter = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                label = { Text("Filter by name") },
                singleLine = true,
            )

            Text(text = "Sort by:", style = MaterialTheme.typography.labelLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { sortOrder = SortOrder.NAME }, modifier = Modifier.weight(1f)) {
                    Text("Name")
                }
                Button(onClick = { sortOrder = SortOrder.AGE }, modifier = Modifier.weight(1f)) {
                    Text("Age")
                }
                Button(
                    onClick = { sortOrder = SortOrder.BIRTHDAY },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("B-day")
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredAndSortedPeople) { person ->
                    PersonCard(
                        person = person,
                        onClick = onNavigateToEditPage
                    )
                }
            }
        }
    }
}

@Composable
fun PersonCard(
    person: Person,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${person.name}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Age: ${person.age}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Birthday: ${person.birthday}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview
@Composable
fun ListScreenPreview() {
    ListScreen()
}
