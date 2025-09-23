package com.konstandaki.jedyapps.data.di

import com.konstandaki.jedyapps.data.repo.MoviesRepositoryImpl
import com.konstandaki.jedyapps.domain.repo.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMovieRepository(impl: MoviesRepositoryImpl): MoviesRepository
}