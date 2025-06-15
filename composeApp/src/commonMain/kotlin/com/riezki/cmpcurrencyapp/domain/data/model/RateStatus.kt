package com.riezki.cmpcurrencyapp.domain.data.model

import androidx.compose.ui.graphics.Color
import com.riezki.cmpcurrencyapp.presenter.`ui/them`.theme.freshColor
import com.riezki.cmpcurrencyapp.presenter.`ui/them`.theme.staleColor

/**
 * @author riezky maisyar
 */

enum class RateStatus(
    val title: String,
    val color: Color
) {
    Idle(
        title = "Rates",
        color = Color.White
    ),
    Fresh(
        title = "Fresh Rate",
        color = freshColor
    ),
    Stale(
        title = "Rate are not fresh",
        color = staleColor
    )
}