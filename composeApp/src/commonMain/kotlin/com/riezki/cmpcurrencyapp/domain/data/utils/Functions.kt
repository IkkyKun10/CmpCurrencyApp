package com.riezki.cmpcurrencyapp.domain.data.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import cmpcurrencyapp.composeapp.generated.resources.Res
import cmpcurrencyapp.composeapp.generated.resources.bebas_nue_regular
import com.riezki.cmpcurrencyapp.data.local.model.CurrencyEntity
import com.riezki.cmpcurrencyapp.data.remote.dto.Currency
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

/**
 * @author riezky maisyar
 */

fun displayCurrentDateTime() : String {
    val currentTimeStamp = Clock.System.now()
    val date = currentTimeStamp.toLocalDateTime(TimeZone.currentSystemDefault())

    val dayOfMount = date.dayOfMonth
    val mount = date.month.toString()
        .lowercase()
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    val year = date.year

    val suffix = when {
        dayOfMount in 11..13 -> "th"
        dayOfMount % 10 == 1 -> "st"
        dayOfMount % 10 == 2 -> "nd"
        dayOfMount % 10 == 3 -> "rd"
        else -> "th"
    }

    return "$dayOfMount$suffix $mount, $year."
}

fun convert(amount: Double, exchangeRate: Double) : Double {
    return amount * exchangeRate
}

fun calculateExchangeRate(source: Double, target: Double) : Double {
    return target/source
}

fun Currency.toCurrencyEntity() : CurrencyEntity {
    return CurrencyEntity().apply {
        code = this@toCurrencyEntity.code
        value = this@toCurrencyEntity.value
    }
}

fun CurrencyEntity.toCurrency() : Currency {
    return Currency(
        code = this.code,
        value = this.value
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GetBebasFontFamily() = FontFamily(Font(Res.font.bebas_nue_regular))