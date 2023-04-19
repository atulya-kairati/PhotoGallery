package com.atulya.photogallery.core.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PreferenceRepository private constructor(
    private val dataStore: DataStore<Preferences>
) {

    val storedQuery: Flow<String> = dataStore.data.map {
        it[SEARCH_QUERY] ?: ""
    }.distinctUntilChanged()

    suspend fun setStoredQuery(query: String) {
        dataStore.edit { pref ->
            pref[SEARCH_QUERY] = query
        }
    }

    val lastResultId: Flow<String> = dataStore.data.map {
        it[PREF_LAST_RESULT_ID] ?: ""
    }.distinctUntilChanged()

    suspend fun setLastResultId(lastResultId: String) {
        Log.d("#> ${this::class.simpleName}", "lastResultId: $lastResultId")
        dataStore.edit { pref ->
            pref[PREF_LAST_RESULT_ID] = lastResultId
        }
    }

    val isPolling: Flow<Boolean> = dataStore.data.map {
        it[PREF_IS_POLLING] ?: true
    }.distinctUntilChanged()

    suspend fun setIsPolling(isPolling: Boolean) {
        dataStore.edit { pref ->
            pref[PREF_IS_POLLING] = isPolling
        }
    }

    companion object {
        private val SEARCH_QUERY = stringPreferencesKey("search_query")
        private val PREF_LAST_RESULT_ID = stringPreferencesKey("lastResultId")
        private val PREF_IS_POLLING = booleanPreferencesKey("isPolling")

        private var INSTANCE: PreferenceRepository? = null

        fun init(context: Context) {

            if (INSTANCE != null) return

            val dataStore = PreferenceDataStoreFactory.create {
                context.preferencesDataStoreFile("settings")
            }

            INSTANCE = PreferenceRepository(dataStore)
        }

        fun get() = checkNotNull(INSTANCE) {
            "Initialise PreferenceRepository first by calling PreferenceRepository.init()"
        }
    }
}