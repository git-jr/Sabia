package com.alura.sabia.ui.endgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alura.sabia.dataStore.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EndGameViewModel @Inject constructor(
    private val dataStore: UserPreferencesDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(EndGameUiState())
    var uiState = _uiState.asStateFlow()

    init {
        loadDaysConnectives()
    }

    private fun loadDaysConnectives() {
        viewModelScope.launch {
            val currentDate = System.currentTimeMillis()
            val lastStudyDayDate = dataStore.getLastStudyDayDate().first()
            val daysConnectives = dataStore.getConsecutiveDays().first()

            val daysDifference = lastStudyDayDate?.let {
                ((currentDate - it) / (1000 * 60 * 60 * 24)).toInt()
            }

            val updatedDaysConnectives = when (daysDifference) {
                1 -> daysConnectives + 1
                else -> 1
            }

            _uiState.value = _uiState.value.copy(consecutiveDays = updatedDaysConnectives)
            dataStore.saveConsecutiveDays(updatedDaysConnectives)
            dataStore.saveLastStudyDayDate(currentDate)
        }
    }
}