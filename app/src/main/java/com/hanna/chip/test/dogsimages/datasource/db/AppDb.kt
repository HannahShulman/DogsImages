package com.hanna.chip.test.dogsimages.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hanna.chip.test.dogsimages.entities.DogBreed

@Database(entities = [DogBreed::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun dao(): DogsDao
}