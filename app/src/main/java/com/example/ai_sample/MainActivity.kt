package com.example.ai_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ai_sample.ui.feature.detail.DetailRoute
import com.example.ai_sample.ui.feature.detail.ItemDetailRoute
import com.example.ai_sample.ui.feature.list.ItemListRoute
import com.example.ai_sample.ui.feature.list.ListRoute
import com.example.ai_sample.ui.global.AppEvent
import com.example.ai_sample.ui.global.AppEventBus
import com.example.ai_sample.ui.theme.AISampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AISampleTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) {
                    AppEventBus.events.collect { event ->
                        when (event) {
                            is AppEvent.ShowSnackbar -> {
                                snackbarHostState.showSnackbar(event.message)
                            }

                            is AppEvent.Unauthorized -> {
                                // Handle unauthorized (e.g., navigate to login)
                            }
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = ListRoute,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<ListRoute> {
                            ItemListRoute(
                                navController = navController
                            )
                        }
                        composable<DetailRoute> {
                            ItemDetailRoute(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}
