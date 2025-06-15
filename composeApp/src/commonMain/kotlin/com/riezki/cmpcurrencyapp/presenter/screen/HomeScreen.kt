package com.riezki.cmpcurrencyapp.presenter.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import com.riezki.cmpcurrencyapp.domain.data.model.CurrencyType
import com.riezki.cmpcurrencyapp.presenter.component.HomeHeader
import com.riezki.cmpcurrencyapp.presenter.event.UiEvent
import com.riezki.cmpcurrencyapp.presenter.`ui/them`.theme.surfaceColor

/**
 * @author riezky maisyar
 */

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<HomeViewModel>()
        val rateStatus by viewModel.rateStatus
        val allCurrencies = viewModel.allCurrencies
        val sourceCurrency by viewModel.sourceCurrency
        val targetCurrency by viewModel.targetCurrency

        var amount by rememberSaveable { mutableDoubleStateOf(0.0) }
        var selectedCurrencyType: CurrencyType by remember {
            mutableStateOf(CurrencyType.None)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceColor)
        ) {
            HomeHeader(
                status = rateStatus,
                source = sourceCurrency,
                target = targetCurrency,
                amount = amount,
                onAmountChange = {
                    amount = it
                },
                onSwitchClick = {
                    viewModel.sendEvent(
                        UiEvent.SwitchCurrencies
                    )
                },
                onRateRefresh = {
                    viewModel.sendEvent(
                        UiEvent.RefreshRate
                    )
                },
                onCurrencyTypeSelect = {

                }
            )
        }
    }
}