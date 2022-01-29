package com.roxasjearom.axieenergycalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var roundCount by mutableStateOf(1)

    var energyCount by mutableStateOf(3)


    fun increaseEnergy() {
        incrementEnergy(1)
    }

    fun decreaseEnergy() {
        decrementEnergy(1)
    }

    fun incrementRound() {
        roundCount++
    }

    fun increaseEnergyAfterRound() {
        incrementEnergy(2)
    }

    fun resetState() {
        roundCount = 1
        energyCount = 3
    }

    private fun incrementEnergy(increment: Int) {
        energyCount += increment
        if(energyCount > 10) {
            energyCount = 10
        }
    }

    private fun decrementEnergy(decrement: Int) {
        energyCount -= decrement
        if (energyCount < 0) {
            energyCount = 0
        }
    }
}
