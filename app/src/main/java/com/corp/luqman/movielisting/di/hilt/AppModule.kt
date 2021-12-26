package com.corp.luqman.movielisting.di.hilt

import com.corp.luqman.movielisting.data.database.MovieDatabase
import com.corp.luqman.movielisting.data.remote.ApiService
import com.corp.luqman.movielisting.data.repository.MoviesRepository
import com.corp.luqman.movielisting.utils.rx.AppSchedulerProvider
import com.corp.luqman.movielisting.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppSchedulerProvider(): SchedulerProvider{
        return AppSchedulerProvider() as SchedulerProvider
    }

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService,
                          movieDatabase: MovieDatabase): MoviesRepository{
        return MoviesRepository(apiService, movieDatabase)
    }
}