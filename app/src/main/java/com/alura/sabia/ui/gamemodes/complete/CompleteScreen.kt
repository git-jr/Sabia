package com.alura.sabia.ui.gamemodes.complete

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ChipColors
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompleteScreen(modifier: Modifier = Modifier) {

    val viewModel = hiltViewModel<CompleteViewModel>()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        val fakeJson = """```json
{
"incomplete_sentence": "Animals play a vital role in the ___ ___",
"correct_sentence": "Animals play a vital role in the global ecosystem",
"correct_answers": ["global", "ecosystem"],
"suggestions": ["global", "ecosystem", "local", "environment", "habitat", "balance", "complex", "world"]
}
```"""

        viewModel.generatePhrase("Carros")
//        viewModel.processResponse(fakeJson)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Clique nas palavras abaixo que completa a frase corretamente"
        )

        Spacer(modifier = Modifier.padding(16.dp))


        FlowRow(
            modifier = modifier,
        ) {
            state.phraseToComplete.split(" ").forEach { word ->
                if (word.contains("*")) {
                    TextContainer(
                        text = word.replace("*", ""),
                        borderColor = Color.Green,
                        onClick = {
                            viewModel.removeAnswer(word)
                        }
                    )
                } else {
                    TextContainer(
                        text = word
                    )

                }
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            state.suggestions.forEach { suggestion ->
                SuggestionChip(
                    onClick = {
                        viewModel.checkAnswer(suggestion)
                        Log.d("Suggestion chip", "hello world")
                    },
                    label = { Text(suggestion) },
                )
            }
        }

        Spacer(modifier = Modifier.padding(32.dp))

        Button(
            onClick = {
                viewModel.checkResult()
            }
        ) {
            Text("Verificar")
        }

        Spacer(modifier = Modifier.padding(32.dp))

        state.isCorrect?.let {
            Text(
                text = if (it) "Correto" else "Incorreto"
            )
        }

    }
}

@Composable
fun TextContainer(
    modifier: Modifier = Modifier,
    text: String,
    borderColor: Color = Color.Unspecified,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .border(1.dp, borderColor, shape = CircleShape)
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
        )
    }
}