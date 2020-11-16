package lulukprojects.musicaltuner.view.Interface

import lulukprojects.musicaltuner.view.enums.NoteName

interface IKnowNextNoteName {
    fun getNextNoteName() : NoteName
}