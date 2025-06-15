package com.riezki.cmpcurrencyapp.presenter.event

/**
 * @author riezky maisyar
 */

sealed class UiEvent {
    data object RefreshRate : UiEvent()
    data object SwitchCurrencies: UiEvent()
    data class SaveSourceCurrencyCode(val code: String): UiEvent()
    data class SaveTargetCurrencyCode(val code: String): UiEvent()
}