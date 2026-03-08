package com.example.ai_sample.di

import com.example.ai_sample.data.repository.ItemRepository
import com.example.ai_sample.domain.usecase.GetItemUseCase
import com.example.ai_sample.domain.usecase.GetItemsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetItemsUseCase(repository: ItemRepository): GetItemsUseCase {
        return GetItemsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetItemUseCase(repository: ItemRepository): GetItemUseCase {
        return GetItemUseCase(repository)
    }
}
