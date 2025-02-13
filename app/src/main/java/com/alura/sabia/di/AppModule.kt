package com.alura.sabia.di

import com.alura.sabia.data.LocalDataSourceSample
import com.alura.sabia.data.ThemeRepository
import com.alura.sabia.data.ThemeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideThemeRepository(): ThemeRepository {
        return ThemeRepositoryImpl(LocalDataSourceSample())
    }

}
