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
import com.example.birthdaylistoblopg.PersonUIState
import com.example.birthdaylistoblopg.data.Person
import com.example.birthdaylistoblopg.ui.theme.BirthdayListOblOpgTheme
import com.google.firebase.auth.FirebaseUser


enum class SortOrder { NAME, AGE, BIRTHDAY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    user: FirebaseUser?,
    onSignOut: () -> Unit,
    personUIState: PersonUIState,
    modifier: Modifier = Modifier,
    onNavigateToLoginPage: () -> Unit = {},
    onNavigateToAddPage: () -> Unit = {},
    onNavigateToEditPage: (Person) -> Unit = {}
) {
    var filter by remember { mutableStateOf("") }
    var sortOrder by remember { mutableStateOf(SortOrder.NAME) }
    var isAscending by remember { mutableStateOf(true) }

    fun onSortClick(newOrder: SortOrder) {
        if (sortOrder == newOrder) {
            isAscending = !isAscending
        } else {
            sortOrder = newOrder
            isAscending = true
        }
    }

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
                title = {
                    Text("My Friends")
                },
                actions = {
                    Button(onClick = { onSignOut() }) {
                        Text("Sign Out")
                    }
                }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (personUIState.error != null) {
                Text(text = personUIState.error)
            } else {
                val people = personUIState.persons

                val filteredAndSortedPeople = people
                    .filter { 
                        it.name.contains(filter, ignoreCase = true) || 
                        it.age.toString().contains(filter) 
                    }
                    .let { list ->
                        val comparator = compareBy<Person> {
                            when (sortOrder) {
                                SortOrder.NAME -> it.name
                                SortOrder.AGE -> it.age
                                SortOrder.BIRTHDAY -> it.birthday
                            }
                        }
                        if (isAscending) list.sortedWith(comparator)
                        else list.sortedWith(comparator.reversed())
                    }
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
                    label = { Text("Filter by name or age") },
                    singleLine = true,
                )

                Text(text = "Sort by:", style = MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onSortClick(SortOrder.NAME) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Name" + (if (sortOrder == SortOrder.NAME) (if (isAscending) " ↑" else " ↓") else ""))
                    }
                    Button(
                        onClick = { onSortClick(SortOrder.AGE) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Age" + (if (sortOrder == SortOrder.AGE) (if (isAscending) " ↑" else " ↓") else ""))
                    }
                    Button(
                        onClick = { onSortClick(SortOrder.BIRTHDAY) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("B-day" + (if (sortOrder == SortOrder.BIRTHDAY) (if (isAscending) " ↑" else " ↓") else ""))
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
                            onClick = { onNavigateToEditPage(person) }
                        )
                    }
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
            Text(
                text = "Birthday: ${person.birthDayOfMonth}/${person.birthMonth}/${person.birthYear}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonCardPreview() {
    BirthdayListOblOpgTheme {
        PersonCard(
            person = Person(1, "testpass@mail.dk", "John Doe", 1999, 1, 1, "test", 25),
            onClick = {}
        )
    }
}

