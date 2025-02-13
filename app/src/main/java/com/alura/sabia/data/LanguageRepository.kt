package com.alura.sabia.data

interface ThemeRepository {
    suspend fun getThemes(): List<String>
    suspend fun getLanguages(): List<String>
}

class ThemeRepositoryImpl(
    private val localDataSource: LocalDataSourceSample
) : ThemeRepository {

    override suspend fun getThemes(): List<String> {
        return localDataSource.getThemes()
    }

    override suspend fun getLanguages(): List<String> {
        return localDataSource.getLanguages()
    }
}
