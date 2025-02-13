package com.alura.sabia.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    object PreferencesKey {
        val selectLanguage = stringPreferencesKey("select_language")
        val selectTheme = stringPreferencesKey("select_theme")
        val consecutiveDays = intPreferencesKey("consecutive_days")
        val lastStudyDayDate = longPreferencesKey("last_study_day_date")
    }

    suspend fun saveSelectLanguage(language: String) {
        dataStore.edit { edit ->
            edit[PreferencesKey.selectLanguage] = language
        }
    }

    fun getSelectLanguage(): Flow<String> {
        return dataStore.data.map {
            it[PreferencesKey.selectLanguage].toString()
        }
    }

    suspend fun saveSelectTheme(theme: String) {
        dataStore.edit { edit ->
            edit[PreferencesKey.selectTheme] = theme
        }
    }

    fun getSelectTheme(): Flow<String> {
        return dataStore.data.map {
            it[PreferencesKey.selectTheme].toString()
        }
    }

    suspend fun saveLastStudyDayDate(date: Long) {
        dataStore.edit { edit ->
            edit[PreferencesKey.lastStudyDayDate] = date
        }
    }

    fun getLastStudyDayDate(): Flow<Long?> {
        return dataStore.data.map {
            it[PreferencesKey.lastStudyDayDate]
        }
    }

    fun getConsecutiveDays(): Flow<Int> {
        return dataStore.data.map {
            it[PreferencesKey.consecutiveDays] ?: 0
        }
    }

    suspend fun saveConsecutiveDays(daysConnectives: Int) {
        dataStore.edit { edit ->
            edit[PreferencesKey.consecutiveDays] = daysConnectives
        }
    }
}