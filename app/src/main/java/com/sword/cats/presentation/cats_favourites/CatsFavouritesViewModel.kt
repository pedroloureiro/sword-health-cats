package com.sword.cats.presentation.cats_favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sword.cats.domain.cats_list.CatsListRepository
import com.sword.cats.presentation.models.CatUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CatsFavouritesUiState {
    data class Loaded(val catList: List<CatUiModel>) : CatsFavouritesUiState()
    data object Loading : CatsFavouritesUiState()
}

@HiltViewModel
class CatsFavouritesViewModel @Inject constructor(
    private val repository: CatsListRepository
) : ViewModel() {
    val uiState: StateFlow<CatsFavouritesUiState> = repository.observeCats().map { cats ->
        val filteredCats = cats.filter { cat -> cat.isFavourite }
        CatsFavouritesUiState.Loaded(filteredCats)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CatsFavouritesUiState.Loading
    )

    fun onFavouriteClick(cat: CatUiModel) {
        viewModelScope.launch {
            repository.onFavouriteClick(cat)
        }
    }
}