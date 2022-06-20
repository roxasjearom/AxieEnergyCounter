package com.roxasjearom.axieenergycalculator.counter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

class CounterViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _mainDisplayUiState = MutableStateFlow(
        MainDisplayUiState(
            energyCount = savedStateHandle.get(ENERGY_COUNT) ?: 3,
            roundCount = savedStateHandle.get(ROUND_COUNT) ?: 1,
        )
    )
    val mainDisplayUiState: StateFlow<MainDisplayUiState> = _mainDisplayUiState
        .stateIn(
        initialValue = _mainDisplayUiState.value,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )


    fun increaseEnergy() {
        incrementEnergy(1)
    }

    fun increaseEnergyAfterRound() {
        incrementEnergy(2)
    }

    fun decreaseEnergy() {
        decrementEnergy(1)
    }

    fun incrementRound() {
        _mainDisplayUiState.update {
            it.copy(roundCount = it.roundCount.inc())
        }
        savedStateHandle[ROUND_COUNT] = _mainDisplayUiState.value.roundCount
    }

    fun resetState() {
        _mainDisplayUiState.update {
            MainDisplayUiState()
        }
        savedStateHandle[ROUND_COUNT] = _mainDisplayUiState.value.roundCount
        savedStateHandle[ENERGY_COUNT] = _mainDisplayUiState.value.energyCount
    }

    private fun incrementEnergy(increment: Int) {
        _mainDisplayUiState.update {
            var energy = it.energyCount + increment
            if (energy > 10) energy = 10
            savedStateHandle[ENERGY_COUNT] = energy
            it.copy(energyCount = energy)
        }
    }

    private fun decrementEnergy(decrement: Int) {
        _mainDisplayUiState.update {
            var energy = it.energyCount - decrement
            if (energy < 0) energy = 0
            savedStateHandle[ENERGY_COUNT] = energy
            it.copy(energyCount = energy)
        }
    }

    companion object {
        const val ENERGY_COUNT = "energy_count"
        const val ROUND_COUNT = "round_count"
    }
}

data class MainDisplayUiState(
    val energyCount: Int = 3,
    val roundCount: Int = 1,
)
