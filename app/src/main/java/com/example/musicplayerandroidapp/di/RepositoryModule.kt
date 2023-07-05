package com.example.musicplayerandroidapp.di

import com.example.musicplayerandroidapp.data.MusicRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideMusicRepository(): MusicRepository {
        return MusicRepository()
    }

}