package com.riezki.cmpcurrencyapp.domain.data.utils

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.riezki.cmpcurrencyapp.domain.data.utils.RequestState.Idle
import com.riezki.cmpcurrencyapp.domain.data.utils.RequestState.Loading
import com.riezki.cmpcurrencyapp.domain.data.utils.RequestState.Success

/**
 * @author riezky maisyar
 */

sealed interface RequestState<out T> {
    data object Idle: RequestState<Nothing>
    data object Loading: RequestState<Nothing>
    data class Success<out T>(val data: T): RequestState<T>
    data class Error(val message: String): RequestState<Nothing>

    fun isLoading(): Boolean = this is Loading
    fun isError(): Boolean = this is Error
    fun isSuccess(): Boolean = this is Success

    fun getSuccessData() = (this as Success).data
    fun getErrorMsg() : String = (this as Error).message
}

@Composable
fun <T> RequestState<T>.DisplayResult(
    onIdle: (@Composable () -> Unit)? = null,
    onLoading: (@Composable () -> Unit)? = null,
    onSuccess: (@Composable (T) -> Unit),
    onError: (@Composable (String) -> Unit)? = null,
    transitionSpec: ContentTransform = scaleIn(tween(durationMillis = 400))
            + fadeIn(tween(durationMillis = 800)) togetherWith scaleOut(tween(durationMillis = 400)) +
            fadeOut(tween(durationMillis = 800))
) {
    AnimatedContent(
        targetState = this,
        transitionSpec = { transitionSpec },
        label = "Content Animation"
    ) { state ->
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            when (state) {
                is RequestState.Error -> onError?.invoke(state.getErrorMsg())
                Idle -> onLoading?.invoke()
                Loading -> onLoading?.invoke()
                is Success -> onSuccess.invoke(state.data)
            }
        }
    }
}