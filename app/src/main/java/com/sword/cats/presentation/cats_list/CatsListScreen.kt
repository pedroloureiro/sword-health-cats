package com.sword.cats.presentation.cats_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.sword.cats.presentation.CatDetails
import com.sword.cats.presentation.components.CatGrid
import com.sword.cats.presentation.components.SearchBar

@Composable
fun CatsListScreen(navController: NavHostController) {
    val viewModel: CatsListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val modifier = Modifier

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Cats List",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(8.dp))
            SearchBar(viewModel::search)
            Spacer(Modifier.height(16.dp))

            when (val state = uiState) {
                is CatsListUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is CatsListUiState.Loaded -> {
                    if (state.catList.isEmpty()) {
                        Text(text = "No cat breeds found.")
                    } else {
                        CatGrid(
                            cats = state.catList, viewModel::onFavouriteClick,
                            onCatClick = { cat ->
                                navController.navigate(CatDetails.createRoute(cat.id))
                            })
                    }
                }

                is CatsListUiState.Idle -> {
                    Text(
                        text = "Use the search bar to search for cat breeds!",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}