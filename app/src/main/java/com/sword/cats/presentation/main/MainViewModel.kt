package com.sword.cats.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sword.cats.domain.main.MainInteractor
import com.sword.cats.presentation.models.CatUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainUIState {
    data object Idle : MainUIState()
    data class Loaded(val catList: List<CatUiModel>): MainUIState()
    data object Loading: MainUIState()
}

@HiltViewModel
class MainViewModel @Inject constructor(private val interactor: MainInteractor) : ViewModel() {
    private val _uiState = MutableStateFlow<MainUIState>(MainUIState.Idle)
    val uiState: StateFlow<MainUIState> = _uiState.asStateFlow()

    fun search() {
        viewModelScope.launch {
            _uiState.value = MainUIState.Loading
            interactor.search().getOrNull()?.let { catList ->
                _uiState.value = MainUIState.Loaded(catList)
            }
        }
    }

    fun onFavoriteClick(breedId: String) {
        val currentState = _uiState.value
        if (currentState is MainUIState.Loaded) {
            val updatedList = currentState.catList.map { breed ->
                if (breed.id == breedId) {
                    breed.copy(favorite = !breed.favorite)
                } else {
                    breed
                }
            }

            _uiState.value = MainUIState.Loaded(updatedList)
        }
    }
}