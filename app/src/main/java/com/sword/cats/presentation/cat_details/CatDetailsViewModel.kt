package com.sword.cats.presentation.cat_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sword.cats.domain.cat_details.CatDetailsRepository
import com.sword.cats.presentation.models.CatDetailsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CatDetailsUiState {
    data class Loaded(val cat: CatDetailsUiModel) : CatDetailsUiState()
    data object Loading : CatDetailsUiState()
}

@HiltViewModel
class CatDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CatDetailsRepository
) : ViewModel() {

    private val catId: String =
        checkNotNull(savedStateHandle["catId"]) {
            "catId is required"
        }

    val uiState: StateFlow<CatDetailsUiState> =
        repository.observeCat(catId)
            .map { CatDetailsUiState.Loaded(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CatDetailsUiState.Loading
            )

    fun onFavouriteClick(cat: CatDetailsUiModel) {
        viewModelScope.launch {
            repository.onFavouriteClick(cat)
        }
    }
}