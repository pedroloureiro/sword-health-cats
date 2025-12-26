package com.sword.cats.presentation.cats_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sword.cats.domain.cats_list.CatsListRepository
import com.sword.cats.presentation.models.CatUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CatsListUiState {
    data object Idle : CatsListUiState()
    data class Loaded(val catList: List<CatUiModel>) : CatsListUiState()
    data object Loading : CatsListUiState()
}

@HiltViewModel
class CatsListViewModel @Inject constructor(
    private val repository: CatsListRepository
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val isSearching = MutableStateFlow(false)
    val uiState: StateFlow<CatsListUiState> =
        combine(
            repository.observeCats(),
            searchQuery,
            isSearching
        ) { cats, query, loading ->
            if (loading) {
                return@combine CatsListUiState.Loading
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
                CatsListUiState.Idle
            } else {
                CatsListUiState.Loaded(filteredCats)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CatsListUiState.Loading
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