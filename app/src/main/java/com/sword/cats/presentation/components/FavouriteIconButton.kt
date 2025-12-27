package com.sword.cats.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sword.cats.R

@Composable
fun FavouriteIconButton(
    catIsFavourite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier.size(32.dp)
    ) {
        Icon(
            painter = painterResource(if (catIsFavourite) R.drawable.ic_favourite else R.drawable.ic_favourite_border),
            contentDescription = "Favourite Button",
            tint = Color.Magenta,
            modifier = Modifier.fillMaxSize()
        )
    }
}