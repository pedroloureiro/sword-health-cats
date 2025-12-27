package com.sword.cats.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sword.cats.presentation.models.CatUiModel

@Composable
fun CatGrid(
    cats: List<CatUiModel>,
    onFavoriteClick: (CatUiModel) -> Unit,
    onCatClick: (CatUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = cats,
            key = { breed -> breed.id }
        ) { catBreedItem ->
            CatGridItem(cat = catBreedItem, onFavoriteClick, onCatClick)
        }
    }
}