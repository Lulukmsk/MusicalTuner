package lulukprojects.musicaltuner.view.helper

import lulukprojects.musicaltuner.view.enums.NoteName
import lulukprojects.musicaltuner.view.container.SoundNote
import lulukprojects.musicaltuner.view.enums.NoteState
import kotlin.math.abs

class NotesHelper {

    companion object {
        lateinit var instance: NotesHelper
    }

    init {
        instance = this
    }

    private val chromaticScale = listOf<Pair<Double, NoteName>>(16.351 to NoteName.C, 17.324 to NoteName.CSharp,
        18.354 to NoteName.D, 19.445 to NoteName.DSharp, 20.601 to NoteName.E,
        21.827 to NoteName.F, 23.124 to NoteName.FSharp, 24.499 to NoteName.G,
        25.956 to NoteName.GSharp, 27.5 to NoteName.A, 29.135 to NoteName.ASharp,
        30.868 to NoteName.B, 32.703 to NoteName.C, 34.648 to NoteName.CSharp,
        36.708 to NoteName.D, 38.891 to NoteName.DSharp, 41.203 to NoteName.E,
        43.654 to NoteName.F, 46.249 to NoteName.FSharp, 48.999 to NoteName.G,
        51.913 to NoteName.GSharp, 55.0 to NoteName.A, 58.27 to NoteName.ASharp,
        61.735 to NoteName.B, 65.406 to NoteName.C, 69.296 to NoteName.CSharp,
        73.416 to NoteName.D, 77.782 to NoteName.DSharp, 82.407 to NoteName.E,
        87.307 to NoteName.F, 92.499 to NoteName.FSharp, 97.999 to NoteName.G,
        103.826 to NoteName.GSharp, 110.0 to NoteName.A, 116.541 to NoteName.ASharp,
        123.471 to NoteName.B, 130.813 to NoteName.C, 138.591 to NoteName.CSharp,
        146.832 to NoteName.D, 155.563 to NoteName.DSharp, 164.814 to NoteName.E,
        174.614 to NoteName.F, 184.997 to NoteName.FSharp, 195.998 to NoteName.G,
        207.652 to NoteName.GSharp, 220.0 to NoteName.A, 233.082 to NoteName.ASharp,
        246.942 to NoteName.B, 261.626 to NoteName.C, 277.183 to NoteName.CSharp,
        293.665 to NoteName.D, 311.127 to NoteName.DSharp, 329.628 to NoteName.E,
        349.228 to NoteName.F, 369.994 to NoteName.FSharp, 391.995 to NoteName.G,
        415.305 to NoteName.GSharp, 440.0 to NoteName.A, 466.164 to NoteName.ASharp,
        493.883 to NoteName.B, 523.251 to NoteName.C, 554.365 to NoteName.CSharp,
        587.33 to NoteName.D, 622.254 to NoteName.DSharp, 659.255 to NoteName.E,
        698.456 to NoteName.F, 739.989 to NoteName.FSharp, 783.991 to NoteName.G,
        830.609 to NoteName.GSharp, 880.0 to NoteName.A, 932.328 to NoteName.ASharp,
        987.767 to NoteName.B, 1046.502 to NoteName.C, 1108.731 to NoteName.CSharp,
        1174.659 to NoteName.D, 1244.508 to NoteName.DSharp, 1318.51 to NoteName.E,
        1396.913 to NoteName.F, 1479.978 to NoteName.FSharp, 1567.982 to NoteName.G,
        1661.219 to NoteName.GSharp, 1760.0 to NoteName.A, 1864.655 to NoteName.ASharp,
        1975.533 to NoteName.B, 2093.005 to NoteName.C, 2217.461 to NoteName.CSharp,
        2349.318 to NoteName.D, 2489.016 to NoteName.DSharp, 2637.021 to NoteName.E,
        2793.826 to NoteName.F, 2959.955 to NoteName.FSharp, 3135.964 to NoteName.G,
        3322.438 to NoteName.GSharp, 3520.0 to NoteName.A, 3729.31 to NoteName.ASharp,
        3951.066 to NoteName.B, 4186.009 to NoteName.C, 4434.922 to NoteName.CSharp,
        4698.636 to NoteName.D, 4978.032 to NoteName.DSharp, 5274.042 to NoteName.E,
        5587.652 to NoteName.F, 5919.91 to NoteName.FSharp, 6271.928 to NoteName.G,
        6644.876 to NoteName.GSharp, 7040.0 to NoteName.A, 7458.62 to NoteName.ASharp,
        7902.132 to NoteName.B, 8372.018 to NoteName.C, 8869.844 to NoteName.CSharp,
        9397.272 to NoteName.D, 9956.064 to NoteName.DSharp, 10548.084 to NoteName.E,
        11175.304 to NoteName.F, 11839.82 to NoteName.FSharp, 12543.856 to NoteName.G,
        13289.752 to NoteName.GSharp, 14080.0 to NoteName.A, 14917.24 to NoteName.ASharp,
        15804.264 to NoteName.B)

    private val chromaticScaleValues = chromaticScale.map { it -> it.first }.sorted()

    fun getClosestNote(noteValue : Double) : SoundNote?
    {
        val min = chromaticScale.minByOrNull { it -> abs(noteValue - it.first) }
        return if(min != null) SoundNote(min.second, min.first) else null
    }

    fun getNextNote(note : SoundNote) : SoundNote?
    {
        var next : SoundNote? = null
        val current = chromaticScaleValues.binarySearch(note.noteValue)
        if (current >= 0 && chromaticScaleValues.count() > current + 1) {
            next = SoundNote(note.noteName.getNextNoteName(), chromaticScaleValues[current + 1])
        }
        return next
    }

    fun getPreviousNote(note : SoundNote?) : SoundNote?
    {
        var next : SoundNote? = null
        if(note != null) {
            val current = chromaticScaleValues.binarySearch(note.noteValue)
            if (current > 0) {
                next = SoundNote(note.noteName.getPreviousNoteName(), chromaticScaleValues[current - 1])
            }
        }
        return next
    }

    fun isSharpNote(note : SoundNote?) : Boolean
    {
        return when(note?.noteName) {
            NoteName.ASharp,
            NoteName.GSharp,
            NoteName.FSharp,
            NoteName.DSharp,
            NoteName.CSharp -> true
            else -> false
        }
    }
}