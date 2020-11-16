package lulukprojects.musicaltuner.view.container

import lulukprojects.musicaltuner.view.enums.NoteName

class SoundNote {

    private lateinit var _noteName : NoteName
    private var _noteValue : Double = 0.0

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

    constructor(noteName: NoteName, noteValue: Double){
        _noteName = noteName
        _noteValue = noteValue
    }
}