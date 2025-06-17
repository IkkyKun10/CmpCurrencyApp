package com.riezki.cmpcurrencyapp.presenter.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riezki.cmpcurrencyapp.data.remote.dto.Currency
import com.riezki.cmpcurrencyapp.domain.data.utils.DoubleConverter
import com.riezki.cmpcurrencyapp.domain.data.utils.GetBebasFontFamily
import com.riezki.cmpcurrencyapp.domain.data.utils.RequestState
import com.riezki.cmpcurrencyapp.domain.data.utils.calculateExchangeRate
import com.riezki.cmpcurrencyapp.domain.data.utils.convert
import com.riezki.cmpcurrencyapp.presenter.ui.theme.headerColor
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * @author riezky maisyar
 */

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    source: RequestState<Currency>,
    target: RequestState<Currency>,
    amount: Double,
) {
    var exchangeAmount by rememberSaveable { mutableStateOf(0.0) }
    val animateExchangeAmount by animateValueAsState(
        targetValue = exchangeAmount,
        animationSpec = tween(durationMillis = 300),
        typeConverter = DoubleConverter()
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${(animateExchangeAmount * 100).toLong() / 100.0}",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 60.sp,
                fontFamily = GetBebasFontFamily(),
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                textAlign = TextAlign.Center
            )

            AnimatedVisibility(
                visible = source.isSuccess() && target.isSuccess()
            ) {
                Column {
                    Text(
                        text = "1 ${source.getSuccessData().code} = " +
                                "${target.getSuccessData().value} " +
                                target.getSuccessData().code,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = if (isSystemInDarkTheme()) {
                            Color.White.copy(alpha = 0.5f)
                        } else Color.Black.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = "1 ${target.getSuccessData().code} = " +
                                "${source.getSuccessData().value} " +
                                source.getSuccessData().code,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = if (isSystemInDarkTheme()) {
                            Color.White.copy(alpha = 0.5f)
                        } else Color.Black.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(horizontal = 24.dp)
                .background(
                    color = Color.Unspecified,
                    shape = RoundedCornerShape(99.dp)
                ),
            onClick = {
                if (source.isSuccess() && target.isSuccess()) {
                    val exchangeRate = calculateExchangeRate(
                        source = source.getSuccessData().value,
                        target = target.getSuccessData().value
                    )
                    exchangeAmount = convert(
                        amount = amount,
                        exchangeRate = exchangeRate
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = headerColor,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Convert"
            )
        }
    }
}

@Preview
@Composable
fun HomeBodyPreview() {
    MaterialTheme {
        val currency = Currency(
            code = "USD",
            value = 100.0
        )
        HomeBody(
            source = RequestState.Success(currency),
            target = RequestState.Success(currency),
            amount = 100.0,
            modifier = Modifier
                .background(Color.White)
        )
    }
}