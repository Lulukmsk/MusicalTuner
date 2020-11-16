package lulukprojects.musicaltuner.view.Interface

import lulukprojects.musicaltuner.view.enums.NoteName

interface IKnowPreviousNoteName {
    fun getPreviousNoteName() : NoteName
}