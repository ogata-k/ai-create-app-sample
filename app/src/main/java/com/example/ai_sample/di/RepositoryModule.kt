package com.example.ai_sample.di

import com.example.ai_sample.data.api.ApiService
import com.example.ai_sample.data.repository.ItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideItemRepository(apiService: ApiService): ItemRepository {
        return ItemRepository(apiService)
    }
}
