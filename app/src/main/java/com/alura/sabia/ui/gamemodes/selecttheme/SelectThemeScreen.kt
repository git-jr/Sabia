package com.alura.sabia.ui.gamemodes.selecttheme

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.alura.sabia.ui.gamemodes.complete.TextContainer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectThemeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            Text(text = "Selecione um dos temas abaixo ou tire uma foto para ser o tema de estudo hoje")
        }

        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlowRow(
                modifier = modifier,
            ) {
                repeat(9) {
                    TextContainer(
                        text = "Tema $it",
                        borderColor = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray)
                    .height(200.dp)
                    .fillMaxWidth()
            ) {
                var scaleState by remember { mutableStateOf(ContentScale.Crop) }
                AsyncImage(
                    "",
                    contentDescription = "Imagem selecionada",
                    contentScale = scaleState,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxSize()
                        .clickable {
                            scaleState = if (scaleState == ContentScale.Crop) ContentScale.FillHeight else ContentScale.Crop
                        }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextContainer(
                    text = "Tirar foto",
                    borderColor = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
                TextContainer(
                    text = "Selecionar foto",
                    borderColor = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Text(text = "O Tema será a imagem selecionada")
        }

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                }
            ) {
                Text("Começar")
            }
        }
    }
}