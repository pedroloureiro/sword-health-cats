package com.sword.cats.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Title(title: String, modifier: Modifier = Modifier, textAlign: TextAlign = TextAlign.Start) {
    val underlineColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge,
        textAlign = textAlign,
        modifier = modifier
            .drawBehind {
                val strokeWidth = 3.dp.toPx()
                val y = size.height - strokeWidth / 2

                drawLine(
                    color = underlineColor,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
    )
}