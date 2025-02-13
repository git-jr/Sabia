package com.alura.sabia.ui.gamemodes.selecttheme

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.alura.sabia.R
import com.alura.sabia.ui.camera.CameraInitializer
import com.alura.sabia.ui.components.DropDownCustom
import com.alura.sabia.ui.components.PressButton
import com.alura.sabia.ui.components.PressButtonStyle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectThemeScreen(
    modifier: Modifier = Modifier,
    onGoToNextScreen: () -> Unit
) {
    val viewModel = hiltViewModel<SelectThemeViewModel>()
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

    val sheetState = rememberModalBottomSheetState(true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.goToNextScreen) {
        if (state.goToNextScreen) {
            onGoToNextScreen()
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
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "Escolha um idioma e um dos temas abaixo, ou adicione uma foto para o seu estudo de hoje.",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(8.dp))
                state.selectedLanguage?.let { selectedLanguage ->
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append("Idioma selecionado: ")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append(selectedLanguage)
                            }
                        }
                    )
                }

                state.selectedTheme?.let { selectedImage ->
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Gray)) {
                                append("Tema selecionado: ")
                            }

                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append(selectedImage)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.padding(16.dp))

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

                    if (state.load) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            val scrollState = rememberScrollState()

            Column(
                modifier = modifier
                    .height(600.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DropDownCustom(
                    selectedText = state.selectedLanguage ?: "Selecione o idioma",
                    listItems = state.languages,
                    onSelectedItem = {
                        viewModel.selectLanguage(it)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                DropDownCustom(
                    selectedText = state.selectedTheme ?: "Selecione o tema",
                    listItems = state.themes,
                    onSelectedItem = {
                        viewModel.selectTheme(it)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PressButton(
                    text = "GERAR TEMA COM IMAGEM",
                    style = PressButtonStyle(
                        horizontalPadding = 48.dp,
                        normalColor = MaterialTheme.colorScheme.primary,
                        pressedColor = MaterialTheme.colorScheme.secondary
                    ),
                    onClick = {
                        viewModel.updateShowBottomSheet(true)
                    }
                )

                Spacer(modifier = Modifier.height(48.dp))

                PressButton(
                    text = "INICIAR",
                    modifier = Modifier.wrapContentSize(),
                    style = PressButtonStyle(
                        horizontalPadding = 100.dp,
                    ),
                    onClick = {
                        viewModel.start()
                    }
                )
            }
        }

        if (state.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    viewModel.updateShowBottomSheet(false)
                },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                containerColor = Color.White,
                tonalElevation = 16.dp,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .width(50.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Selecione uma imagem para gerar o tema",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.LightGray)
                            .height(200.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "Image Icon",
                            tint = Color.Gray,
                            modifier = Modifier
                                .clickable {
                                    viewModel.updateShowCameraState(true)
                                    viewModel.updateShowBottomSheet(false)
                                }
                                .size(50.dp)
                        )
                        var scaleState by remember { mutableStateOf(ContentScale.Crop) }

                        state.selectedImage?.let { selectedImage ->
                            AsyncImage(
                                selectedImage,
                                contentDescription = "Imagem selecionada",
                                contentScale = scaleState,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .fillMaxSize()
                                    .clickable {
                                        scaleState =
                                            if (scaleState == ContentScale.Crop) ContentScale.FillHeight else ContentScale.Crop
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        PressButton(
                            style = PressButtonStyle(
                                normalColor = MaterialTheme.colorScheme.primary,
                                pressedColor = MaterialTheme.colorScheme.secondary,
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
                                viewModel.updateShowBottomSheet(false)
                            }
                        )

                        PressButton(
                            style = PressButtonStyle(
                                normalColor = MaterialTheme.colorScheme.primary,
                                pressedColor = MaterialTheme.colorScheme.secondary,
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
                                customIcon = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Delete Icon",
                                        tint = Color.White
                                    )
                                }
                            ),
                            onClick = {
                                coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        viewModel.removeSelectedImage()
                                    }
                                }
                            }
                        )


                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    PressButton(
                        text = "USAR ESSA",
                        style = PressButtonStyle(
                            horizontalPadding = 48.dp
                        ),
                        onClick = {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    viewModel.updateShowBottomSheet(false)
                                    viewModel.generateThemByImage()
                                }
                            }
                        }
                    )
                }
            }
        }

        if (state.showCamera) {
            val context = LocalContext.current
            CameraInitializer(
                onImageSaved = { image ->
                    viewModel.addNewItemImage(image)
                    viewModel.updateShowCameraState(false)
                    viewModel.updateShowBottomSheet(true)
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
