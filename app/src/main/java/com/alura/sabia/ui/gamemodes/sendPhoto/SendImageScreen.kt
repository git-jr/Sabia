package com.alura.sabia.ui.gamemodes.sendPhoto

import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.alura.sabia.ui.camera.CameraInitializer

@Composable
fun SendImageScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<SendImageViewModel>()
    val state by viewModel.uiState.collectAsState()

    val contentResolver = LocalContext.current.contentResolver

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri ->
        imageUri?.let {
            val bitmap = contentResolver?.let { contentResolver ->
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, it)
                }
            }

            bitmap?.let { bitmapNew -> viewModel.addNewItemImage(bitmapNew) }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.requestSubjectImage("Animals")
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.load) {
                Text(text = "Carregando...")
            } else {
                Text(text = state.requestText)

                Spacer(modifier = Modifier.height(16.dp))

                state.selectedImage?.let {
                    AsyncImage(
                        it,
                        contentDescription = "Imagem selecionada",
                        modifier = Modifier.size(200.dp)
                    )
                }

                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            viewModel.updateShowCameraState(true)
                        }
                    ) {
                        Text("Camêra")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            pickMedia.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    ) {
                        Text("Galeria")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.checkImage()
                    }
                ) {
                    Text("Enviar Imagem")
                }

                Spacer(modifier = Modifier.padding(32.dp))

                state.isCorrect?.let {
                    Text(
                        text = if (it) "Correto" else "Incorreto"
                    )
                }
            }
        }


        if (state.showCamera) {
            val context = LocalContext.current
            CameraInitializer(
                onImageSaved = { filePath ->
                    viewModel.addNewItemImage(filePath)
                    viewModel.updateShowCameraState(false)
                },
                onError = {
                    Toast.makeText(
                        context,
                        "Erro ao salvar imagem", Toast.LENGTH_SHORT
                    )
                        .show()
                    viewModel.updateShowCameraState(false)
                }
            )
        }
    }
}