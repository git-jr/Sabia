package com.alura.sabia.ui.gamemodes.sendImage

import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.alura.sabia.R
import com.alura.sabia.ui.camera.CameraInitializer
import com.alura.sabia.ui.components.BottomSheetResult
import com.alura.sabia.ui.components.LoadBox
import com.alura.sabia.ui.components.PressButton
import com.alura.sabia.ui.components.PressButtonStyle

@Composable
fun SendImageScreen(
    modifier: Modifier = Modifier,
    onGoToNextScreen: () -> Unit
) {
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

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
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
                    Spacer(modifier = Modifier.height(100.dp))
                    SelectionContainer {
                        Text(text = state.requestText)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.LightGray)
                            .size(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "Image Icon",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(50.dp)
                        )

                        var scaleState by remember { mutableStateOf(ContentScale.Crop) }
                        AsyncImage(
                            state.selectedImage,
                            contentDescription = "Imagem selecionada",
                            contentScale = scaleState,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .size(300.dp)
                                .clickable {
                                    scaleState =
                                        if (scaleState == ContentScale.Crop) ContentScale.FillHeight else ContentScale.Crop
                                }
                        )

                    }
                }

                Column(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween
                    ) {
                        PressButton(
                            style = PressButtonStyle(
                                normalColor = MaterialTheme.colorScheme.primary,
                                pressedColor = MaterialTheme.colorScheme.secondary,
                                horizontalPadding = 32.dp,
                                customIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_camera),
                                        contentDescription = "Camera Icon",
                                        tint = Color.White
                                    )
                                }
                            ),
                            onClick = {
                                viewModel.updateShowCameraState(true)
                            }
                        )

                        PressButton(
                            style = PressButtonStyle(
                                normalColor = MaterialTheme.colorScheme.primary,
                                pressedColor = MaterialTheme.colorScheme.secondary,
                                horizontalPadding = 32.dp,
                                customIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_gallery),
                                        contentDescription = "Galeira Icon",
                                        tint = Color.White
                                    )
                                }
                            ),
                            onClick = {
                                pickMedia.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        )

                        PressButton(
                            style = PressButtonStyle(
                                normalColor = MaterialTheme.colorScheme.primary,
                                pressedColor = MaterialTheme.colorScheme.secondary,
                                horizontalPadding = 32.dp,
                                customIcon = {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = "Camera Icon",
                                        tint = Color.White
                                    )
                                }
                            ),
                            onClick = {
                                viewModel.requestAgain()
                            }
                        )

                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        PressButton(
                            text = "VERIFICAR",
                            modifier = Modifier.wrapContentSize(),
                            onClick = {
                                viewModel.checkImage()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.padding(32.dp))
                }
            }
        }

        if (state.showCamera) {
            val context = LocalContext.current
            CameraInitializer(
                onImageSaved = { image ->
                    viewModel.addNewItemImage(image)
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


        if (state.showBottomSheetResult) {
            BottomSheetResult(
                explanation = state.explanation,
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
