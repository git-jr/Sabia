package com.alura.sabia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alura.sabia.ui.gamemodes.complete.CompleteScreen
import com.alura.sabia.ui.gamemodes.identifyItems.IdentifyItemsScreen
import com.alura.sabia.ui.gamemodes.readAndRespond.ReadAndRespondScreen
import com.alura.sabia.ui.gamemodes.selecttheme.SelectThemeScreen
import com.alura.sabia.ui.gamemodes.sendPhoto.SendImageScreen
import com.alura.sabia.ui.gamemodes.translation.TranslationScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var navigateToGame by remember { mutableStateOf(true) }
            if (navigateToGame) {
                Game()
            } else {
                Home(
                    onNavigateToGame = {
                        navigateToGame = true
                    }
                )
            }
        }
    }
}

@Composable
fun Home(
    modifier: Modifier = Modifier,
    onNavigateToGame: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello!",
            modifier = modifier
        )

        Button(onClick = onNavigateToGame) {
            Text("Play")
        }
    }
}

@Composable
fun Game(modifier: Modifier = Modifier) {
    val listAllModes = listOf(
        GameModes.SELECT_THEME_OR_PHOTO,
        GameModes.COMPLETE_TRANSLATION,
        GameModes.SEND_PHOTO,
        GameModes.TRANSLATE_PHRASE,
        GameModes.READ_AND_RESPOND,
        GameModes.IDENTIFY_ITEMS
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    var currentMode by remember { mutableStateOf(listAllModes[currentIndex]) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (currentIndex > 0) {
                        currentIndex--
                        currentMode = listAllModes[currentIndex]
                    }
                }
            ) {
                Text("Anterior")
            }
            Button(onClick = {
                if (currentIndex < listAllModes.size - 1) {
                    currentIndex++
                    currentMode = listAllModes[currentIndex]
                }
            }) {
                Text("Proximo")
            }
        }

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (currentMode) {
                GameModes.SELECT_THEME_OR_PHOTO -> {
                    SelectThemeScreen()
                }

                GameModes.COMPLETE_TRANSLATION -> {
                    CompleteScreen()
                }

                GameModes.TRANSLATE_PHRASE -> {
                    TranslationScreen()
                }

                GameModes.READ_AND_RESPOND -> {
                    ReadAndRespondScreen()
                }

                GameModes.IDENTIFY_ITEMS -> {
                    IdentifyItemsScreen()
                }

                GameModes.SEND_PHOTO -> {
                    SendImageScreen()
                }
            }
        }
    }
}