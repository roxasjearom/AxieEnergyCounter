package com.roxasjearom.axieenergycalculator.counter

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roxasjearom.axieenergycalculator.R
import com.roxasjearom.axieenergycalculator.ui.theme.AxieEnergyCalculatorTheme
import com.roxasjearom.axieenergycalculator.utils.vibrate

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    val context = LocalContext.current
    GestureContainer(
        roundSlot = { RoundDisplay(roundCount = viewModel.mainDisplayUiState.collectAsState().value.roundCount) },
        energySlot = { EnergyDisplay(energyCount = viewModel.mainDisplayUiState.collectAsState().value.energyCount) },
        onSwipeRight = { viewModel.increaseEnergy() },
        onSwipeLeft = { viewModel.decreaseEnergy() },
        onDoubleTap = {
            viewModel.increaseEnergyAfterRound()
            viewModel.incrementRound()
            vibrate(context)
        },
        onResetClicked = { viewModel.resetState() }
    )
}

@Composable
fun GestureContainer(
    roundSlot: @Composable () -> Unit,
    energySlot: @Composable () -> Unit,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit,
    onDoubleTap: () -> Unit,
    onResetClicked: () -> Unit,
    ) {
    var horizontalDragAmount by remember { mutableStateOf(0f) }

    Box {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (horizontalDragAmount > 1) { //SWIPE RIGHT
                                onSwipeRight()
                            } else if (horizontalDragAmount < -1) { //SWIPE LEFT
                                onSwipeLeft()
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            horizontalDragAmount = dragAmount
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            onDoubleTap()
                        }
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            roundSlot()
            energySlot()
        }
        ResetButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 64.dp, end = 64.dp)
                .align(Alignment.BottomCenter)
        ) {
            onResetClicked()
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
        text = stringResource(id = R.string.round_format, roundCount).uppercase(),
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
            text = stringResource(R.string.label_energy).uppercase(),
            style = MaterialTheme.typography.h6,
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
            contentDescription = stringResource(R.string.label_reset),
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = stringResource(R.string.label_reset).uppercase(),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoundDisplayPreview() {
    RoundDisplay(roundCount = 1)
}

@Preview(showBackground = true)
@Composable
fun EnergyDisplayPreview() {
    EnergyDisplay(energyCount = 3)
}

@Preview(showBackground = true)
@Composable
fun ResetButtonPreview() {
    AxieEnergyCalculatorTheme {
        ResetButton(onClick = {})
    }
}
