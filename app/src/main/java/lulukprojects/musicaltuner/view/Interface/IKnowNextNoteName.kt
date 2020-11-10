package lulukprojects.musicaltuner.view.Interface

import lulukprojects.musicaltuner.view.enums.NoteNames

interface IKnowNextNoteName {
    fun getNextNoteName() : NoteNames
}