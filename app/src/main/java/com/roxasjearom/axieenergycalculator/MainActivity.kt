package com.roxasjearom.axieenergycalculator

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roxasjearom.axieenergycalculator.ui.theme.AxieEnergyCalculatorTheme

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AxieEnergyCalculatorTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        viewModel = viewModel,
                        onEnergyIncreased = {
                            viewModel.increaseEnergy()
                            Log.e("ENERGY INC", "ENERGY: ${viewModel.energyCount}")
                        },
                        onEnergyDecreased = {
                            viewModel.decreaseEnergy()
                            Log.e("ENERGY DEC", "ENERGY: ${viewModel.energyCount}")
                        },
                         onReset = {
                             viewModel.resetState()
                             Log.e("RESET", "ENERGY: ${viewModel.energyCount}")
                         },
                        onNextRound = {
                            viewModel.increaseEnergyAfterRound()
                            viewModel.incrementRound()
                            Log.e("NEXT ROUND", "ENERGY: ${viewModel.energyCount}")
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onEnergyIncreased: () -> Unit,
    onEnergyDecreased: () -> Unit,
    onNextRound: () -> Unit,
    onReset: () -> Unit,
) {
    var myDragAmount by remember { mutableStateOf(0f) }
    val context = LocalContext.current

    Box {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (myDragAmount > 1) { //SWIPE RIGHT
                                onEnergyIncreased()
                                Log.e("SWIPE RIGHT", "RIGHT")
                            } else if (myDragAmount < -1) { //SWIPE LEFT
                                onEnergyDecreased()
                                Log.e("SWIPE LEFT", "LEFT")
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            myDragAmount = dragAmount
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            vibrate(context)
                            onNextRound()
                        }
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RoundDisplay(roundCount = viewModel.roundCount)

            EnergyDisplay(energyCount = viewModel.energyCount)
        }

        ResetButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 64.dp, end = 64.dp)
                .align(BottomCenter)
        ) {
            onReset()
        }
    }
}

@Composable
fun RoundDisplay(
    modifier: Modifier = Modifier,
    roundCount: Int,
) {
    Text(
        modifier = modifier,
        text = "ROUND $roundCount",
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun EnergyDisplay(
    modifier: Modifier = Modifier,
    energyCount: Int,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = energyCount.toString(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 132.sp,
        )
        Text(
            text = "ENERGY",
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun ResetButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = "Reset",
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "RESET",
            modifier = Modifier.padding(8.dp)
        )
    }
}

fun vibrate(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(100)
    }
}
