package com.hanna.chip.test.dogsimages.di

import com.hanna.chip.test.dogsimages.repository.DogsRepository
import com.hanna.chip.test.dogsimages.repository.impl.DogsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@InstallIn(FragmentComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun dogsRepository(repository: DogsRepositoryImpl): DogsRepository
}