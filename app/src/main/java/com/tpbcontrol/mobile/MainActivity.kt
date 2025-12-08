package com.tpbcontrol.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.tpbcontrol.mobile.domain.AppContainer
import com.tpbcontrol.mobile.ui.TPBTheme
import com.tpbcontrol.mobile.ui.screens.TPBApp
import com.tpbcontrol.mobile.ui.screens.LocalAppContainer

class MainActivity : ComponentActivity() {

    private val appContainer by lazy { AppContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalAppContainer provides appContainer) {
                TPBTheme {
                    TPBApp(appContainer = appContainer)
                }
            }
        }
    }
}
