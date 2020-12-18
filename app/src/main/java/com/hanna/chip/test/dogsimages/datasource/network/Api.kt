package com.hanna.chip.test.dogsimages.datasource.network

import com.hanna.chip.test.dogsimages.entities.DogBreedsResponseList
import com.hanna.chip.test.dogsimages.entities.Response
import com.hanna.chip.test.dogsimages.entities.Response.Companion.DEFAULT_IMAGE_LIMIT
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("breeds/list/all")
    fun getAllBreeds(): Flow<ApiResponse<Response<DogBreedsResponseList>>>

    @GET("https://dog.ceo/api/breed/{type}/images/random/${DEFAULT_IMAGE_LIMIT}")
    fun getRandomImagesForBreed(@Path("type", encoded = true) type: String): Flow<ApiResponse<Response<List<String>>>>
}