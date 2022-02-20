package com.roxasjearom.axieenergycalculator

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var roundCount by mutableStateOf(savedStateHandle.get(ROUND_COUNT) ?: 1)

    var energyCount by mutableStateOf(savedStateHandle.get(ENERGY_COUNT) ?: 3)


    fun increaseEnergy() {
        incrementEnergy(1)
    }

    fun decreaseEnergy() {
        decrementEnergy(1)
    }

    fun incrementRound() {
        roundCount++
        savedStateHandle[ROUND_COUNT] = roundCount
    }

    fun increaseEnergyAfterRound() {
        incrementEnergy(2)
    }

    fun resetState() {
        roundCount = 1
        energyCount = 3

        savedStateHandle[ROUND_COUNT] = roundCount
        savedStateHandle[ENERGY_COUNT] = energyCount
    }

    private fun incrementEnergy(increment: Int) {
        energyCount += increment
        if(energyCount > 10) {
            energyCount = 10
        }
        savedStateHandle[ENERGY_COUNT] = energyCount
    }

    private fun decrementEnergy(decrement: Int) {
        energyCount -= decrement
        if (energyCount < 0) {
            energyCount = 0
        }
        savedStateHandle[ENERGY_COUNT] = energyCount
    }

    companion object {
        const val ENERGY_COUNT = "energy_count"
        const val ROUND_COUNT = "round_count"
    }
}
