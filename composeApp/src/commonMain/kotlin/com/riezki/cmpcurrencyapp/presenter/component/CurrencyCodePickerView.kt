package com.riezki.cmpcurrencyapp.presenter.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.riezki.cmpcurrencyapp.domain.data.model.CurrencyCode
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.riezki.cmpcurrencyapp.presenter.ui.theme.primaryColor
import com.riezki.cmpcurrencyapp.presenter.ui.theme.surfaceColor
import com.riezki.cmpcurrencyapp.presenter.ui.theme.textColor
import org.jetbrains.compose.resources.painterResource

/**
 * @author riezky maisyar
 */

@Composable
fun CurrencyCodePickerView(
    modifier: Modifier = Modifier,
    code: CurrencyCode,
    isSelected: Boolean,
    onSelect: (CurrencyCode) -> Unit
) {
    var saturation = remember { Animatable(if (isSelected) 1f else 0f) }

    LaunchedEffect(isSelected) {
        saturation.animateTo(if (isSelected) 1f else 0f)
    }

    val colorMatrix = remember(saturation.value) {
        ColorMatrix().apply {
            setToSaturation(saturation.value)
        }
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.5f,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelect(code) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(code.flag),
                contentDescription = "Country flag",
                colorFilter = ColorFilter.colorMatrix(colorMatrix)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = code.name,
                modifier = Modifier.alpha(animatedAlpha),
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        }
        CurrencyCodeSelector(isSelected = isSelected)
    }
}

@Composable
fun CurrencyCodeSelector(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) primaryColor else textColor.copy(alpha = 0.1f),
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(animatedColor),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = Icons.Default.Check,
                contentDescription = "Checkmark Icon",
                tint = surfaceColor
            )
        }
    }
}