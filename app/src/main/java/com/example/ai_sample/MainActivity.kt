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
import androidx.navigation.toRoute
import com.example.ai_sample.core.AppEvent
import com.example.ai_sample.core.AppEventBus
import com.example.ai_sample.data.api.ApiService
import com.example.ai_sample.data.repository.ItemRepository
import com.example.ai_sample.ui.feature.detail.DetailRoute
import com.example.ai_sample.ui.feature.detail.ItemDetailRoute
import com.example.ai_sample.ui.feature.list.ItemListRoute
import com.example.ai_sample.ui.feature.list.ListRoute
import com.example.ai_sample.ui.theme.AISampleTheme
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val itemRepository = ItemRepository(apiService)

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
                                navController = navController,
                                repository = itemRepository
                            )
                        }
                        composable<DetailRoute> { backStackEntry ->
                            val detailRoute = backStackEntry.toRoute<DetailRoute>()
                            ItemDetailRoute(
                                route = detailRoute,
                                navController = navController,
                                repository = itemRepository
                            )
                        }
                    }
                }
            }
        }
    }
}
