package com.roxasjearom.axieenergycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import com.roxasjearom.axieenergycalculator.counter.CounterScreen
import com.roxasjearom.axieenergycalculator.ui.theme.AxieEnergyCalculatorTheme

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AxieEnergyCalculatorTheme {
                Surface(color = MaterialTheme.colors.background) {
                    CounterScreen()
                }
            }
        }
    }
}
