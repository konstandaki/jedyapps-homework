package com.konstandaki.jedyapps.di

import com.konstandaki.jedyapps.domain.interactor.MoviesSearchInteractor
import com.konstandaki.jedyapps.domain.interactor.MoviesSearchInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    @Singleton
    abstract fun bindMovieSearchInteractor(impl: MoviesSearchInteractorImpl): MoviesSearchInteractor
}