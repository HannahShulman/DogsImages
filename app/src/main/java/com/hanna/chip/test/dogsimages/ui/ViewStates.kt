package com.hanna.chip.test.dogsimages.ui

import android.view.View
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.hanna.chip.test.dogsimages.R

class ViewStates(private val view: View) {
    fun setState(state: ViewState) {
        ViewState.values().onEach { viewState ->
            view.findViewById<View?>(viewState.viewId)?.isVisible = state === viewState
        }

        playLoader(state == ViewState.LOADING)
    }

    private fun playLoader(play: Boolean) {
        view.findViewById<LottieAnimationView>(R.id.loader).run {
            isVisible = play
            playAnimation().takeIf { play } ?: cancelAnimation()
        }
    }

    enum class ViewState(val viewId: Int) {
        LOADING(R.id.loading_view),
        MAIN(R.id.main_view),
        ERROR(0);
    }
}