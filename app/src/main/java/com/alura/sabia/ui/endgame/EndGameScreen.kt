package com.alura.sabia.ui.endgame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.alura.sabia.R
import com.alura.sabia.ui.components.LoadBox
import com.alura.sabia.ui.components.PressButton
import com.alura.sabia.ui.components.PressButtonStyle

@Composable
fun EndGameScreen(
    modifier: Modifier = Modifier,
    onGoHome: () -> Unit
) {
    val viewModel = hiltViewModel<EndGameViewModel>()
    val state by viewModel.uiState.collectAsState()

    if (state.load) {
        LoadBox()
    } else {
        Column(
            modifier = modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(50.dp))
            ) {
                AsyncImage(
                    R.mipmap.ic_launcher,
                    contentDescription = "App logo",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }


            Text(
                text = "Parabéns!",
                fontWeight = FontWeight.Bold,
                fontSize = 56.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Você concluiu a lição de hoje!",
                fontSize = 16.sp,
            )


            Text(
                text = "${state.consecutiveDays} dias consecutivos",
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                PressButton(
                    text = "Voltar para a tela inicial",
                    modifier = Modifier.wrapContentSize(),
                    style = PressButtonStyle(
                        normalColor = MaterialTheme.colorScheme.primary,
                        pressedColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onClick = { onGoHome() }
                )
            }
        }
    }
}