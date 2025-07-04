package com.riezki.cmpcurrencyapp.presenter.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cmpcurrencyapp.composeapp.generated.resources.Res
import cmpcurrencyapp.composeapp.generated.resources.exchange_illustration
import cmpcurrencyapp.composeapp.generated.resources.refresh_ic
import cmpcurrencyapp.composeapp.generated.resources.switch_ic
import com.riezki.cmpcurrencyapp.data.remote.dto.Currency
import com.riezki.cmpcurrencyapp.domain.data.model.CurrencyCode
import com.riezki.cmpcurrencyapp.domain.data.model.CurrencyType
import com.riezki.cmpcurrencyapp.domain.data.model.RateStatus
import com.riezki.cmpcurrencyapp.domain.data.utils.DisplayResult
import com.riezki.cmpcurrencyapp.domain.data.utils.RequestState
import com.riezki.cmpcurrencyapp.domain.data.utils.displayCurrentDateTime
import com.riezki.cmpcurrencyapp.getPlatform
import com.riezki.cmpcurrencyapp.presenter.ui.theme.headerColor
import com.riezki.cmpcurrencyapp.presenter.ui.theme.staleColor
import org.jetbrains.compose.resources.painterResource

/**
 * @author riezky maisyar
 */

@Composable
fun HomeHeader(
    status: RateStatus,
    source: RequestState<Currency>,
    target: RequestState<Currency>,
    amount: Double,
    onAmountChange: (Double) -> Unit,
    onRateRefresh: () -> Unit,
    onSwitchClick: () -> Unit,
    onCurrencyTypeSelect: (CurrencyType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
            .background(headerColor)
            .padding(top = if (getPlatform().name == "Android") 0.dp else 24.dp)
            .padding(all = 24.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        RatesStatus(
            status = status,
            onRateRefresh = onRateRefresh
        )
        Spacer(Modifier.height(24.dp))
        CurrencyInput(
            source = source,
            target = target,
            onSwitchClick = onSwitchClick,
            onCurrencyTypeSelect = onCurrencyTypeSelect
        )
        Spacer(Modifier.height(24.dp))
        AmountInput(
            amount = amount,
            onAmountChange = onAmountChange
        )
    }
}

@Composable
fun RatesStatus(
    status: RateStatus,
    onRateRefresh: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(Res.drawable.exchange_illustration),
                contentDescription = "Exchange rate illustration"
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = displayCurrentDateTime(),
                    color = Color.White
                )
                Text(
                    text = status.title,
                    color = status.color,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }
        }
        if (status == RateStatus.Stale) {
            IconButton(
                onClick = onRateRefresh
            ) {
                Icon(
                    painter = painterResource(Res.drawable.refresh_ic),
                    contentDescription = "Refresh icon",
                    tint = staleColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun RowScope.CurrencyView(
    placeholder: String,
    currency: RequestState<Currency>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
    ) {
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = placeholder,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = Color.White,
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.5f))
                .height(54.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            currency.DisplayResult(
                onSuccess = { data ->
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(
                            CurrencyCode.valueOf(data.code).flag
                        ),
                        tint = Color.Unspecified,
                        contentDescription = "Country flag"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = CurrencyCode.valueOf(data.code).name,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = Color.White
                    )
                }
            )
        }
    }
}

@Composable
fun CurrencyInput(
    modifier: Modifier = Modifier,
    source: RequestState<Currency>,
    target: RequestState<Currency>,
    onSwitchClick: () -> Unit,
    onCurrencyTypeSelect: (CurrencyType) -> Unit
) {
    var animationStarted by remember { mutableStateOf(false) }
    val animationRotation by animateFloatAsState(
        targetValue = if (animationStarted) 180f else 0f,
        animationSpec = tween(durationMillis = 500)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrencyView(
            placeholder = "from",
            currency = source,
            onClick = {
                if (source.isSuccess()) {
                    onCurrencyTypeSelect(
                        CurrencyType.Source(
                            currencyCode = CurrencyCode.valueOf(
                                source.getSuccessData().code
                            )
                        )
                    )
                }
            },
        )
        Spacer(modifier = Modifier.height(14.dp))
        IconButton(
            modifier = Modifier.padding(top = 24.dp)
                .graphicsLayer {
                    rotationY = animationRotation
                },
            onClick = {
                animationStarted = !animationStarted
                onSwitchClick()
            }
        ) {
            Icon(
                painter = painterResource(Res.drawable.switch_ic),
                contentDescription = "Switch Icon",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        CurrencyView(
            placeholder = "to",
            currency = target,
            onClick = {
                if (source.isSuccess()) {
                    onCurrencyTypeSelect(
                        CurrencyType.Target(
                            currencyCode = CurrencyCode.valueOf(
                                target.getSuccessData().code
                            )
                        )
                    )
                }
            },
        )
    }
}

@Composable
fun AmountInput(
    modifier: Modifier = Modifier,
    amount: Double,
    onAmountChange: (Double) -> Unit
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .animateContentSize()
            .height(54.dp),
        value = "$amount",
        onValueChange = {
            onAmountChange(it.toDouble())
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.05f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
            disabledContainerColor = Color.White.copy(alpha = 0.05f),
            errorContainerColor = Color.White.copy(alpha = 0.05f),
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White
        ),
        textStyle = TextStyle(
            color = Color.White,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}
