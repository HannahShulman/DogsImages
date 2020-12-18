package com.hanna.chip.test.dogsimages.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hanna.chip.test.dogsimages.OpenForTesting
import com.hanna.chip.test.dogsimages.entities.DogBreed
import com.hanna.chip.test.dogsimages.repository.DogsRepository
import javax.inject.Inject

@OpenForTesting
class DogImagesViewModel(private val repository: DogsRepository) : ViewModel() {

    fun getBreedsList() = repository.getBreedsList()

    fun fetchImagesForBreed(breed: DogBreed) =  repository.fetchImagesForBreed(breed)
}

class DogImagesViewModelFactory @Inject constructor(private val repository: DogsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DogImagesViewModel(repository) as T
    }
}