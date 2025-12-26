package com.sword.cats.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sword.cats.domain.main.MainRepository
import com.sword.cats.presentation.models.CatUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainUIState {
    data object Idle : MainUIState()
    data class Loaded(val catList: List<CatUiModel>) : MainUIState()
    data object Loading : MainUIState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val isSearching = MutableStateFlow(false)
    val uiState: StateFlow<MainUIState> =
        combine(
            repository.observeCats(),
            searchQuery,
            isSearching
        ) { cats, query, loading ->
            if (loading) {
                return@combine MainUIState.Loading
            }
            val filteredCats =
                if (query.isBlank()) {
                    cats
                } else {
                    cats.filter { cat ->
                        cat.name.contains(query, ignoreCase = true)
                    }
                }

            if (filteredCats.isEmpty() && query.isBlank()) {
                MainUIState.Idle
            } else {
                MainUIState.Loaded(filteredCats)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainUIState.Loading
        )

    fun search(query: String) {
        searchQuery.value = query
        viewModelScope.launch {
            isSearching.value = true
            repository.search(query)
            isSearching.value = false
        }
    }

    fun onFavouriteClick(cat: CatUiModel) {
        viewModelScope.launch {
            repository.onFavouriteClick(cat)
        }
    }
}
