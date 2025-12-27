package com.sword.cats.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sword.cats.R
import com.sword.cats.presentation.models.CatUiModel

@Composable
fun CatGridItem(
    cat: CatUiModel,
    onFavoriteClick: (CatUiModel) -> Unit,
    onCatClick: (CatUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { onCatClick(cat) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = cat.imageUrl,
                contentDescription = "Photo of ${cat.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = { onFavoriteClick(cat) },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(if (cat.isFavourite) R.drawable.ic_favourite else R.drawable.ic_favourite_border),
                    contentDescription = "Favourite Button",
                    tint = Color.Magenta,
                    modifier = Modifier.size(25.dp)
                )
            }

            Text(
                text = cat.name,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(vertical = 4.dp)
            )
        }
    }
}