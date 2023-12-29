package com.example.persistancestoredata

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(
    name = "PreferenceDataStore"
)

class PreferenceHelper(context: Context) : IPreferenceDataStoreAPI {
    private val dataSource = context.dataStore

    override suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        dataSource.data.catch { exception ->
            // Log or print the exception for debugging purposes
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            it[key] ?: defaultValue
        }

    override suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        try {
            dataSource.edit {
                it[key] = value
            }
        } catch (e: Exception) {
            // Handle the exception (e.g., log it) based on your application's requirements
        }
    }

    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        try {
            dataSource.edit {
                it.remove(key)
            }
        } catch (e: Exception) {
            // Handle the exception (e.g., log it) based on your application's requirements
        }
    }

    override suspend fun <T> clearAllPreference() {
        try {
            dataSource.edit {
                it.clear()
            }
        } catch (e: Exception) {
            // Handle the exception (e.g., log it) based on your application's requirements
        }
    }
}
