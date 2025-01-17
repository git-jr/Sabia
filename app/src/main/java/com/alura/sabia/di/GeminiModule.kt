package com.alura.sabia.di

import com.alura.sabia.apiKey
import com.alura.sabia.gemini.Gemini
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class GeminiModule {
    @Provides
    fun provideGemini(): Gemini {
        return Gemini(apiKey)
    }
}