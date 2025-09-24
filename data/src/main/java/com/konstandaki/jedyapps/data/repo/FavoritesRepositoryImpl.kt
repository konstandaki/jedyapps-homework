package com.konstandaki.jedyapps.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.konstandaki.jedyapps.domain.repo.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : FavoritesRepository {

    private val key = stringSetPreferencesKey("favorites_ids")

    override val favoritesIds: Flow<Set<String>> =
        dataStore.data.map { prefs -> prefs[key] ?: emptySet() }

    override suspend fun add(id: String) {
        dataStore.edit { it[key] = (it[key] ?: emptySet()) + id }
    }

    override suspend fun remove(id: String) {
        dataStore.edit { it[key] = (it[key] ?: emptySet()) - id }
    }

    override suspend fun toggle(id: String) {
        dataStore.edit { prefs ->
            val curr = prefs[key] ?: emptySet()
            prefs[key] = if (id in curr) curr - id else curr + id
        }
    }
}