package com.hanna.chip.test.dogsimages.repository

import com.hanna.chip.test.dogsimages.datasource.network.Resource
import com.hanna.chip.test.dogsimages.entities.DogBreed
import kotlinx.coroutines.flow.Flow

interface DogsRepository {
    fun getBreedsList(): Flow<Resource<List<DogBreed>>>
    fun fetchImagesForBreed(breed: DogBreed): Flow<Resource<List<String>>>
}