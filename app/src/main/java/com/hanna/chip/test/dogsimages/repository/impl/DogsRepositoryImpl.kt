package com.hanna.chip.test.dogsimages.repository.impl

import android.text.format.DateUtils
import com.hanna.chip.test.dogsimages.datasource.SharedPreferencesContract
import com.hanna.chip.test.dogsimages.datasource.db.DogsDao
import com.hanna.chip.test.dogsimages.datasource.network.*
import com.hanna.chip.test.dogsimages.entities.DogBreed
import com.hanna.chip.test.dogsimages.entities.DogBreedsResponseList
import com.hanna.chip.test.dogsimages.entities.Response
import com.hanna.chip.test.dogsimages.repository.DogsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class DogsRepositoryImpl @Inject constructor(
    val spContract: SharedPreferencesContract,
    val api: Api,
    val dao: DogsDao
) : DogsRepository {
    override fun getBreedsList(): Flow<Resource<List<DogBreed>>> {
        return object :
            FlowNetworkBoundResource<List<DogBreed>, Response<DogBreedsResponseList>>() {
            override suspend fun saveNetworkResult(item: Response<DogBreedsResponseList>) {
                withContext(Dispatchers.Default) {
                    item.message.entries.map { mutableEntry ->
                        mutableEntry.value.map { type -> DogBreed(type, mutableEntry.key) }
                            .toMutableList().apply {
                                add(DogBreed(mutableEntry.key, null))
                            }.also {
                                dao.insertDogBreed(it)
                            }
                    }
                }
            }

            override fun shouldFetch(): Boolean {//fetch this data once a day.
                return Date().time.minus(spContract.breedsLastRequestTime) > DateUtils.DAY_IN_MILLIS
            }

            override fun loadFromDb(): Flow<List<DogBreed>> {
                return dao.getDogBreedList()
            }

            override suspend fun fetchFromNetwork(): Flow<ApiResponse<Response<DogBreedsResponseList>>> {
                return api.getAllBreeds()
            }

        }.asFlow()
    }

    override fun fetchImagesForBreed(breed: DogBreed): Flow<Resource<List<String>>> {
        return object : FlowNetworkOnlyResource<List<String>, Response<List<String>>>() {
            override fun processResponse(response: Response<List<String>>): List<String> {
                return response.message
            }

            override suspend fun saveNetworkResult(item: Response<List<String>>) {
            }

            override suspend fun fetchFromNetwork(): Flow<ApiResponse<Response<List<String>>>> {
                return api.getRandomImagesForBreed(breed.subUrl)
            }
        }.asFlow()
    }
}