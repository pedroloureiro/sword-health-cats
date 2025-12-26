package com.sword.cats.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sword.cats.domain.main.MainRepository
import com.sword.cats.presentation.models.CatUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainUIState {
    data object Idle : MainUIState()
    data class Loaded(val catList: List<CatUiModel>): MainUIState()
    data object Loading: MainUIState()
}

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<MainUIState>(MainUIState.Idle)
    val uiState: StateFlow<MainUIState> =
        repository.observeCats().map { cats ->
            if (cats.isEmpty()) {
                MainUIState.Idle
            } else {
                MainUIState.Loaded(cats)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainUIState.Loading
        )

    fun search() {
        viewModelScope.launch {
            _uiState.value = MainUIState.Loading
            repository.search()
        }
    }

    fun onFavouriteClick(cat: CatUiModel) {
        viewModelScope.launch {
            repository.onFavouriteClick(cat)
        }
    }
}