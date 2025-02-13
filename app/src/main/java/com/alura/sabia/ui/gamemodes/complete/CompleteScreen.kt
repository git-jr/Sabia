package com.alura.sabia.ui.gamemodes.complete

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alura.sabia.ui.components.BottomSheetError
import com.alura.sabia.ui.components.BottomSheetSuccess
import com.alura.sabia.ui.components.LoadBox
import com.alura.sabia.ui.components.PressButton
import com.alura.sabia.ui.components.PressButtonStyle

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompleteScreen(
    modifier: Modifier = Modifier,
    onGoToNextScreen: () -> Unit
) {
    val viewModel = hiltViewModel<CompleteViewModel>()
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.load) {
                LoadBox()
            } else {
                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Clique nas palavras abaixo que completam a frase corretamente."
                    )

                    Spacer(modifier = Modifier.padding(16.dp))


                    FlowRow(
                        modifier = modifier,
                    ) {
                        state.phraseToComplete.split(" ").forEach { word ->
                            if (word.contains("*")) {
                                SuggestionChip(
                                    onClick = { viewModel.removeAnswer(word) },
                                    label = { Text(word.replace("*", "")) },
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                )
                            } else {
                                SuggestionChip(
                                    border = BorderStroke(1.dp, Color.Unspecified),
                                    onClick = {},
                                    label = { Text(word.replace("*", "")) },
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
                                },
                                label = { Text(suggestion) },
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(32.dp))

                    Row(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        PressButton(
                            text = "VERIFICAR",
                            modifier = Modifier.wrapContentSize(),
                            onClick = {
                                viewModel.checkResult()
                            }
                        )

                        PressButton(
                            style = PressButtonStyle(
                                normalColor = MaterialTheme.colorScheme.primary,
                                pressedColor = MaterialTheme.colorScheme.secondary,
                                customIcon = {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Refresh Icon",
                                        tint = Color.White
                                    )
                                }
                            ),
                            onClick = {
                                viewModel.generatePhrase()
                            }
                        )
                    }
                }
            }
        }

        if (state.showBottomSheetError) {
            BottomSheetError(
                onDismissRequest = {
                    viewModel.updateShowBottomSheetError(false)
                },
                onPositiveClick = {
                    viewModel.updateShowBottomSheetError(false)
                }
            )
        }

        if (state.showBottomSheetSuccess) {
            BottomSheetSuccess(
                explanation = state.translationPhrase,
                onDismissRequest = {
                    viewModel.updateShowBottomSheetSuccess(false)
                },
                onPositiveClick = {
                    viewModel.updateShowBottomSheetSuccess(false)
                    onGoToNextScreen()
                }
            )
        }
    }
}