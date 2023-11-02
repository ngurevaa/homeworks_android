package ru.kpfu.itis.gureva.homeworks_android

import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet

class DetailsTransition() : TransitionSet() {
    init {
        init()
    }

    private fun init() {
        ordering = ORDERING_TOGETHER
        addTransition(ChangeBounds()).addTransition(ChangeTransform()).addTransition(
            ChangeImageTransform()
        )
    }
}

