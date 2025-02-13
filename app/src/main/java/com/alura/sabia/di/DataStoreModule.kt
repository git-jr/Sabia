package com.alura.sabia.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.alura.sabia.dataStore.UserPreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val SABIA_DATASTORE = "sabia_datastore"

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStoreUserPreferences(@ApplicationContext context: Context): UserPreferencesDataStore {
        val dataStore = PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(
                    SABIA_DATASTORE
                )
            }
        )
        return UserPreferencesDataStore(dataStore)
    }
}