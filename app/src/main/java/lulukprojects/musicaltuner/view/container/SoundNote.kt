package lulukprojects.musicaltuner.view.container

import lulukprojects.musicaltuner.view.enums.NoteName

class SoundNote(noteName: NoteName, noteValue: Double) {

    private var _noteName : NoteName = noteName
    private var _noteValue : Double = noteValue

    var noteName : NoteName
        get() = _noteName
        set(value) {
            _noteName = value
        }

    var noteValue : Double
        get() = _noteValue
        set(value) {
            _noteValue = value
        }

}