package com.example.coollib.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coollib.domain.usecase.TelemetryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenTelemetryViewModel @Inject constructor(
    private val telemetryUseCase: TelemetryUseCase
) : ViewModel() {
    fun trackScreen(name: String) {
        viewModelScope.launch {
            telemetryUseCase.trackScreenView(name)
        }
    }
}


@Composable
fun TrackScreenView(screenName: String) {

    @Suppress("DEPRECATION")
    val viewModel: ScreenTelemetryViewModel = hiltViewModel()

    LaunchedEffect(screenName) {
        viewModel.trackScreen(screenName)
    }
}