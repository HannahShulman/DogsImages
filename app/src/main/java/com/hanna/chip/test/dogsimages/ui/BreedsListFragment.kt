package com.hanna.chip.test.dogsimages.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.hanna.chip.test.dogsimages.R
import com.hanna.chip.test.dogsimages.datasource.network.Status
import com.hanna.chip.test.dogsimages.entities.DogBreed
import com.hanna.chip.test.dogsimages.extensions.provideViewModel
import com.hanna.chip.test.dogsimages.viewmodel.DogImagesViewModel
import com.hanna.chip.test.dogsimages.viewmodel.DogImagesViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breeds_list.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class BreedsListFragment : Fragment(R.layout.fragment_breeds_list) {

    @Inject
    lateinit var factory: DogImagesViewModelFactory

    val viewModel: DogImagesViewModel by provideViewModel { factory }

    private val adapter = DogBreedAdapter(::onDogBreedSelection)

    private val viewStates: ViewStates by lazy {
        ViewStates(requireView())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        breeds_list.let {
            it.adapter = adapter
            it.addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getBreedsList().collect { resource ->
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
                //set data
                resource.data?.let { data ->
                    adapter.submitList(data.sortedBy { it.breedTypeDescription })
                }
            }
        }
    }

    private fun onDogBreedSelection(breed: DogBreed) {
        parentFragmentManager.commit {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                0,
                R.anim.exit_to_right
            )
            addToBackStack(null)
            replace(
                R.id.fragment_container,
                DogBreedImagesFragment.newInstance(breed)
            )
        }
    }
}