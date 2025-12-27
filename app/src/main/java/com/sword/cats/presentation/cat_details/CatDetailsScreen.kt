package com.sword.cats.presentation.cat_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.sword.cats.presentation.components.CatDetailItem
import com.sword.cats.presentation.components.FavouriteIconButton
import com.sword.cats.presentation.components.Title

@Composable
fun CatDetailsScreen(
    onBack: () -> Unit,
    viewModel: CatDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        CatDetailsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is CatDetailsUiState.Loaded -> {
            val cat = (uiState as CatDetailsUiState.Loaded).cat
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    Title(cat.name, textAlign = TextAlign.Center)

                    Spacer(Modifier.weight(1f))

                    FavouriteIconButton(
                        catIsFavourite = cat.isFavourite,
                        onClick = { viewModel.onFavouriteClick(cat) }
                    )
                }

                Spacer(Modifier.height(16.dp))

                AsyncImage(
                    model = cat.imageUrl,
                    contentDescription = cat.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Fit
                )

                Spacer(Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.medium
                        )
                        .verticalScroll(scrollState)
                ) {
                    CatDetailItem("Origin", cat.origin)
                    Spacer(Modifier.height(8.dp))
                    CatDetailItem("Temperament", cat.temperament)
                    Spacer(Modifier.height(8.dp))
                    CatDetailItem("Description", cat.description)
                }
            }
        }
    }
}