package com.hanna.chip.test.dogsimages.ui

import android.os.Build
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.hanna.chip.test.dogsimages.R
import com.hanna.chip.test.dogsimages.datasource.network.Resource
import com.hanna.chip.test.dogsimages.entities.DogBreed
import com.hanna.chip.test.dogsimages.extensions.replace
import com.hanna.chip.test.dogsimages.viewmodel.DogImagesViewModel
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class BreedsListFragmentTest {

    lateinit var scenario: FragmentScenario<BreedsListFragment>
    lateinit var factory: BreedsListFragmentTestFactory
    private val initDogBreedListResource = Resource.success<List<DogBreed>>(
        listOf(DogBreed("first", "firstParent"))
    )
    private val listValue = MutableStateFlow(initDogBreedListResource)
    private val viewModelMock: DogImagesViewModel = mock {
        onBlocking { getBreedsList() } doReturn listValue
    }

    @Before
    fun setupScenario() {
        factory = BreedsListFragmentTestFactory(viewModelMock)
        scenario = FragmentScenario.launchInContainer(
            BreedsListFragment::class.java,
            null, R.style.Theme_AppCompat_NoActionBar, factory
        )
    }

    @Test
    fun `when beer list resource is successful, list adapter item count, is equals to breed list`() {
        listValue.value = initDogBreedListResource
        scenario.onFragment {
            val rv = it.view!!.findViewById<RecyclerView>(R.id.breeds_list)
            assertThat(rv!!.adapter?.itemCount).isEqualTo(1)
        }
    }

    @Test
    fun `when beer list resource is successful, ViewStates_MAIN resource id is visible`() {
        listValue.value = initDogBreedListResource
        scenario.onFragment {
            val mainView = it.view!!.findViewById<View>(R.id.main_view)
            assertThat(mainView.isVisible).isTrue()
        }
    }

    @Test
    fun `when beer list resource is loading with data, ViewStates_MAIN resource id is visible`() {
        listValue.value = Resource.loading(initDogBreedListResource.data)
        scenario.onFragment {
            val mainView = it.view!!.findViewById<View>(R.id.main_view)
            assertThat(mainView.isVisible).isTrue()
        }
    }

    @Test
    fun `when beer list resource is loading with no data, ViewStates_LOADING resource id is visible`() {
        listValue.value = Resource.loading(null)
        scenario.onFragment {
            val mainView = it.view!!.findViewById<View>(R.id.loading_view)
            assertThat(mainView.isVisible).isTrue()
        }
    }

    @Test
    fun `when beer list resource is error with no data, ViewStates_LOADING resource id is visible`() {
        listValue.value = Resource.error("An error has occurred", null)
        scenario.onFragment {
            assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("An error has occurred")
        }
    }

}

class BreedsListFragmentTestFactory constructor(
    var viewModelMock: DogImagesViewModel
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        BreedsListFragment().apply {
            replace(BreedsListFragment::viewModel, viewModelMock)
        }
}