package com.atulya.photogallery.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PreferenceRepository private  constructor(
    private val dataStore: DataStore<Preferences>
) {

    val storedQuery: Flow<String> = dataStore.data.map {
        it[SEARCH_QUERY] ?: ""
    }.distinctUntilChanged()

    suspend fun setStoredQuery(query: String){
        dataStore.edit { pref ->
            pref[SEARCH_QUERY] = query
        }
    }

    companion object{
        private val SEARCH_QUERY = stringPreferencesKey("search_query")

        private var INSTANCE: PreferenceRepository? = null

        fun init(context: Context) {

            if(INSTANCE != null) return

            val dataStore = PreferenceDataStoreFactory.create {
                context.preferencesDataStoreFile("settings")
            }

            INSTANCE = PreferenceRepository(dataStore)
        }

        fun get() = checkNotNull(INSTANCE){
            "Initialise PreferenceRepository first by calling PreferenceRepository.init()"
        }
    }
}