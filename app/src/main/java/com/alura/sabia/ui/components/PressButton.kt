package com.alura.sabia.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PressButton(
    text: String? = null,
    onClick: () -> Unit,
    style: PressButtonStyle = PressButtonStyle(),
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            )
            .drawWithCache {
                onDrawBehind {
                    drawRoundRect(
                        color = style.pressedColor,
                        cornerRadius = CornerRadius(
                            style.cornerRadius.toPx(),
                            style.cornerRadius.toPx()
                        ),
                        size = Size(
                            size.width,
                            size.height
                        )
                    )
                    if (!isPressed) {
                        drawRoundRect(
                            color = style.normalColor,
                            cornerRadius = CornerRadius(
                                style.cornerRadius.toPx(),
                                style.cornerRadius.toPx()
                            ),
                            size = Size(
                                size.width,
                                size.height - style.offset.toPx()
                            )
                        )
                    }
                }
            },

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    top = style.verticalPadding,
                    bottom = style.verticalPadding,
                    start = style.horizontalPadding,
                    end = style.horizontalPadding
                )
                .align(Alignment.Center)
                .offset(
                    x = 0.dp,
                    y = if (isPressed) 0.dp else (-style.offset / 2)
                )
        ) {
            style.customIcon?.let { icon ->
                icon()
                Spacer(modifier = Modifier.width( if (text != null) 8.dp else 0.dp))
            }

            text?.let {
                Text(
                    text = text,
                    fontSize = style.fontSize,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Black,
                    color = style.textColor
                )
            }
        }
    }
}

data class PressButtonStyle(
    val normalColor: Color = Color(0xFF57CA02),
    val pressedColor: Color = Color(0xFF359703),
    val textColor: Color = Color.White,
    val fontSize: TextUnit = 16.sp,
    val cornerRadius: Dp = 16.dp,
    val offset: Dp = 6.dp,
    val horizontalPadding: Dp = 40.dp,
    val verticalPadding: Dp = 16.dp,
    val customIcon: @Composable (() -> Unit)? = null
)