package com.sword.cats.presentation.cat_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.sword.cats.R

@Composable
fun CatDetailsScreen(
    onBack: () -> Unit,
    viewModel: CatDetailsViewModel = hiltViewModel()
) {
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
                    .padding(16.dp)
            ) {

                /* ---------- Top row ---------- */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = "Back"
                        )
                    }

                    Text(
                        text = cat.name,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = { viewModel.onFavouriteClick(cat) }) {
                        Icon(
                            painter = painterResource(
                                if (cat.isFavourite)
                                    R.drawable.ic_favourite
                                else
                                    R.drawable.ic_favourite_border
                            ),
                            contentDescription = "Favourite",
                            tint = Color.Magenta
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                /* ---------- Image ---------- */
                AsyncImage(
                    model = cat.imageUrl,
                    contentDescription = cat.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(24.dp))

                /* ---------- Details ---------- */
                DetailItem("Origin", "cat.origin")
                DetailItem("Temperament", "cat.temperament")
                DetailItem("Description", "cat.description")
            }
        }
    }
}


@Composable
fun DetailItem(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
