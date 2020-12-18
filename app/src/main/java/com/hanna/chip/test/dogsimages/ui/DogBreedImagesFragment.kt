package com.hanna.chip.test.dogsimages.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hanna.chip.test.dogsimages.R
import com.hanna.chip.test.dogsimages.datasource.network.Status
import com.hanna.chip.test.dogsimages.entities.DogBreed
import com.hanna.chip.test.dogsimages.viewmodel.DogImagesViewModel
import com.hanna.chip.test.dogsimages.viewmodel.DogImagesViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.random_images_layout.*
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DogBreedImagesFragment : Fragment(R.layout.random_images_layout) {

    @Inject
    lateinit var factory: DogImagesViewModelFactory

    private val viewModel: DogImagesViewModel by viewModels { factory }


    val imageAdapter = ImagesAdapter()

    private val dogType: DogBreed by lazy {
        arguments?.getSerializable(DOG_BREED_TYPE_KEY) as? DogBreed
            ?: throw Throwable("$this must be instantiated using the newInstance method, for type access purposes")
    }

    private val viewStates: ViewStates by lazy {
        ViewStates(requireView())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title.text = dogType.breedTypeDescription.capitalize(Locale.getDefault())
        image_list.adapter = imageAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.fetchImagesForBreed(dogType).collect { resource ->
                //set views state
                val state = when (resource.status) {
                    Status.LOADING -> ViewStates.ViewState.LOADING.takeIf { resource.data.isNullOrEmpty() }
                        ?: ViewStates.ViewState.MAIN
                    Status.SUCCESS -> ViewStates.ViewState.MAIN
                    Status.ERROR -> ViewStates.ViewState.ERROR.also {
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                viewStates.setState(state)

                resource.data?.let { data ->
                    imageAdapter.submitList(data)
                }
            }
        }
    }

    companion object {

        const val DOG_BREED_TYPE_KEY = "dog_breed_type_key"

        fun newInstance(type: DogBreed): DogBreedImagesFragment {
            return DogBreedImagesFragment().apply {
                arguments = bundleOf(DOG_BREED_TYPE_KEY to type)
            }
        }
    }
}