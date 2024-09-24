package com.example.assingment4quizappwithrecomposition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assingment4quizappwithrecomposition.ui.theme.Assingment4QuizAppWithRecompositionTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assingment4QuizAppWithRecompositionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuizApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun QuizApp(modifier: Modifier = Modifier) {
    val questions = listOf(
        "What is Naruto's last name?" to "Uzumaki",
        "Who is the leader of Akatsuki?" to "Pain",
        "What is Sasuke's clan name?" to "Uchiha",
        "What beast is sealed inside Naruto?" to "Kurama",
        "What village is Naruto from?" to "Konoha",
        "Who was the first Hokage?" to "Hashirama"
    )

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var quizComplete by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    if (quizComplete) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Quiz Complete!")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                currentQuestionIndex = 0
                userInput = ""
                quizComplete = false
            }) {
                Text("Restart Quiz")
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = questions[currentQuestionIndex].first,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Your Answer") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val correctAnswer = questions[currentQuestionIndex].second
                    coroutineScope.launch {
                        if (userInput.equals(correctAnswer, ignoreCase = true)) {
                            snackbarHostState.showSnackbar("Correct!")
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                                userInput = ""
                            } else {
                                quizComplete = true
                            }
                        } else {
                            snackbarHostState.showSnackbar("Incorrect, try again!")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Answer")
            }

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizAppPreview() {
    Assingment4QuizAppWithRecompositionTheme {
        QuizApp()
    }
}
