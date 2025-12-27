package com.sword.cats.presentation.favourite_cats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sword.cats.domain.favourite_cats.FavouriteCatsRepository
import com.sword.cats.presentation.models.CatUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class FavouriteCatsUiState {
    data class Loaded(val catList: List<CatUiModel>) : FavouriteCatsUiState()
    data object Loading : FavouriteCatsUiState()
}

@HiltViewModel
class FavouriteCatsViewModel @Inject constructor(
    private val repository: FavouriteCatsRepository
) : ViewModel() {
    val uiState: StateFlow<FavouriteCatsUiState> = repository.observeCats().map { cats ->
        val filteredCats = cats.filter { cat -> cat.isFavourite }
        FavouriteCatsUiState.Loaded(filteredCats)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FavouriteCatsUiState.Loading
    )

    fun onFavouriteClick(cat: CatUiModel) {
        viewModelScope.launch {
            repository.onFavouriteClick(cat)
        }
    }
}