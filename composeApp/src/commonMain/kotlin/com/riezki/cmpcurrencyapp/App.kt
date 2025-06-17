package com.riezki.cmpcurrencyapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.riezki.cmpcurrencyapp.data.di.initializeKoin
import com.riezki.cmpcurrencyapp.presenter.screen.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {

    MaterialTheme {
        Navigator(HomeScreen())
    }
}