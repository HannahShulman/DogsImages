package com.hanna.chip.test.dogsimages.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

typealias DogBreedsResponseList = HashMap<String, List<String>>

@Entity
data class DogBreed(@PrimaryKey val breedType: String, val parentBreedType: String?) :
    Serializable {

    val breedTypeDescription: String
        get() {
            return "${
                (parentBreedType + " ").takeIf { parentBreedType != null }.orEmpty()
            }$breedType".capitalize(Locale.getDefault())
        }

    val subUrl: String
        get() {
            return "${
                (parentBreedType + "/").takeIf { parentBreedType != null }.orEmpty()
            }$breedType"
        }
}

data class Response<T>(val message: T, val status: String) {

    companion object {
        const val DEFAULT_IMAGE_LIMIT = 10
    }
}