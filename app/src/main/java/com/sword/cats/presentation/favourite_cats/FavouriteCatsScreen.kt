package com.sword.cats.presentation.favourite_cats

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sword.cats.presentation.components.CatGrid
import com.sword.cats.presentation.components.Title

@Composable
fun FavouriteCatsScreen(onCatClick: (String) -> Unit) {
    val viewModel: FavouriteCatsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val modifier = Modifier

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {

            Title("Favourites")

            Spacer(Modifier.height(16.dp))

            when (val state = uiState) {
                is FavouriteCatsUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is FavouriteCatsUiState.Loaded -> {
                    val catsList = state.model.catsList
                    if (catsList.isEmpty()) {
                        Text(text = "You don't have any favourites.")
                    } else {
                        CatGrid(
                            cats = catsList,
                            onFavoriteClick = viewModel::onFavouriteClick,
                            onCatClick = onCatClick,
                            modifier = Modifier.weight(1f)
                        )

                        val averageLifeExpectancy = state.model.averageLifeExpectancy
                        if(averageLifeExpectancy != null) {
                            LifeExpectancyInfoBanner(averageLifeExpectancy)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LifeExpectancyInfoBanner(averageLifeExpectancy: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Information",
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "The average life expectancy is $averageLifeExpectancy years.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}