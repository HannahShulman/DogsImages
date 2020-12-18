package com.hanna.chip.test.dogsimages.datasource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.hanna.chip.test.dogsimages.entities.DogBreed
import kotlinx.coroutines.flow.Flow

@Dao
interface DogsDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertDogBreed(data: List<DogBreed>)

    @Query("SELECT * FROM DogBreed")
    fun getDogBreedList(): Flow<List<DogBreed>>
}