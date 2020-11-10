package lulukprojects.musicaltuner.view.container

import lulukprojects.musicaltuner.view.enums.NoteNames

class SoundNote {

    private lateinit var _noteName : NoteNames
    private var _noteValue : Double = 0.0

    var noteName : NoteNames
        get() = _noteName
        set(value) {
            _noteName = value
        }

    var noteValue : Double
        get() = _noteValue
        set(value) {
            _noteValue = value
        }

    constructor(noteName: NoteNames, noteValue: Double){
        _noteName = noteName
        _noteValue = noteValue
    }
}