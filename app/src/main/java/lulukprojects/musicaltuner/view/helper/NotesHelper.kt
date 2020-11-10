package lulukprojects.musicaltuner.view.helper

import lulukprojects.musicaltuner.view.enums.NoteNames
import lulukprojects.musicaltuner.view.container.SoundNote
import kotlin.math.abs

class NotesHelper {

    companion object {
        lateinit var instance: NotesHelper
    }

    init {
        instance = this
    }

    private val chromaticScale = listOf<Pair<Double, NoteNames>>(16.351 to NoteNames.C, 17.324 to NoteNames.CSharp,
        18.354 to NoteNames.D, 19.445 to NoteNames.DSharp, 20.601 to NoteNames.E,
        21.827 to NoteNames.F, 23.124 to NoteNames.FSharp, 24.499 to NoteNames.G,
        25.956 to NoteNames.GSharp, 27.5 to NoteNames.A, 29.135 to NoteNames.ASharp,
        30.868 to NoteNames.B, 32.703 to NoteNames.C, 34.648 to NoteNames.CSharp,
        36.708 to NoteNames.D, 38.891 to NoteNames.DSharp, 41.203 to NoteNames.E,
        43.654 to NoteNames.F, 46.249 to NoteNames.FSharp, 48.999 to NoteNames.G,
        51.913 to NoteNames.GSharp, 55.0 to NoteNames.A, 58.27 to NoteNames.ASharp,
        61.735 to NoteNames.B, 65.406 to NoteNames.C, 69.296 to NoteNames.CSharp,
        73.416 to NoteNames.D, 77.782 to NoteNames.DSharp, 82.407 to NoteNames.E,
        87.307 to NoteNames.F, 92.499 to NoteNames.FSharp, 97.999 to NoteNames.G,
        103.826 to NoteNames.GSharp, 110.0 to NoteNames.A, 116.541 to NoteNames.ASharp,
        123.471 to NoteNames.B, 130.813 to NoteNames.C, 138.591 to NoteNames.CSharp,
        146.832 to NoteNames.D, 155.563 to NoteNames.DSharp, 164.814 to NoteNames.E,
        174.614 to NoteNames.F, 184.997 to NoteNames.FSharp, 195.998 to NoteNames.G,
        207.652 to NoteNames.GSharp, 220.0 to NoteNames.A, 233.082 to NoteNames.ASharp,
        246.942 to NoteNames.B, 261.626 to NoteNames.C, 277.183 to NoteNames.CSharp,
        293.665 to NoteNames.D, 311.127 to NoteNames.DSharp, 329.628 to NoteNames.E,
        349.228 to NoteNames.F, 369.994 to NoteNames.FSharp, 391.995 to NoteNames.G,
        415.305 to NoteNames.GSharp, 440.0 to NoteNames.A, 466.164 to NoteNames.ASharp,
        493.883 to NoteNames.B, 523.251 to NoteNames.C, 554.365 to NoteNames.CSharp,
        587.33 to NoteNames.D, 622.254 to NoteNames.DSharp, 659.255 to NoteNames.E,
        698.456 to NoteNames.F, 739.989 to NoteNames.FSharp, 783.991 to NoteNames.G,
        830.609 to NoteNames.GSharp, 880.0 to NoteNames.A, 932.328 to NoteNames.ASharp,
        987.767 to NoteNames.B, 1046.502 to NoteNames.C, 1108.731 to NoteNames.CSharp,
        1174.659 to NoteNames.D, 1244.508 to NoteNames.DSharp, 1318.51 to NoteNames.E,
        1396.913 to NoteNames.F, 1479.978 to NoteNames.FSharp, 1567.982 to NoteNames.G,
        1661.219 to NoteNames.GSharp, 1760.0 to NoteNames.A, 1864.655 to NoteNames.ASharp,
        1975.533 to NoteNames.B, 2093.005 to NoteNames.C, 2217.461 to NoteNames.CSharp,
        2349.318 to NoteNames.D, 2489.016 to NoteNames.DSharp, 2637.021 to NoteNames.E,
        2793.826 to NoteNames.F, 2959.955 to NoteNames.FSharp, 3135.964 to NoteNames.G,
        3322.438 to NoteNames.GSharp, 3520.0 to NoteNames.A, 3729.31 to NoteNames.ASharp,
        3951.066 to NoteNames.B, 4186.009 to NoteNames.C, 4434.922 to NoteNames.CSharp,
        4698.636 to NoteNames.D, 4978.032 to NoteNames.DSharp, 5274.042 to NoteNames.E,
        5587.652 to NoteNames.F, 5919.91 to NoteNames.FSharp, 6271.928 to NoteNames.G,
        6644.876 to NoteNames.GSharp, 7040.0 to NoteNames.A, 7458.62 to NoteNames.ASharp,
        7902.132 to NoteNames.B, 8372.018 to NoteNames.C, 8869.844 to NoteNames.CSharp,
        9397.272 to NoteNames.D, 9956.064 to NoteNames.DSharp, 10548.084 to NoteNames.E,
        11175.304 to NoteNames.F, 11839.82 to NoteNames.FSharp, 12543.856 to NoteNames.G,
        13289.752 to NoteNames.GSharp, 14080.0 to NoteNames.A, 14917.24 to NoteNames.ASharp,
        15804.264 to NoteNames.B)

    private val chromaticScaleValues = chromaticScale.map { it -> it.first }.sorted()

    fun getClosestNote(noteValue : Double) : SoundNote?
    {
        var min = chromaticScale.minByOrNull { it -> abs(noteValue - it.first) }
        return if(min != null) SoundNote(min.second, min.first) else null
    }

    fun getNextNote(note : SoundNote) : SoundNote?
    {
        var next : SoundNote? = null
        var current = chromaticScaleValues.binarySearch(note.noteValue)
        if (current >= 0 && chromaticScaleValues.count() > current + 1) {
            next = SoundNote(note.noteName.getNextNoteName(), chromaticScaleValues[current + 1])
        }
        return next
    }

    fun getPreviousNote(note : SoundNote?) : SoundNote?
    {
        var next : SoundNote? = null
        if(note != null) {
            var current = chromaticScaleValues.binarySearch(note.noteValue)
            if (current > 0) {
                next = SoundNote(note.noteName.getPreviousNoteName(), chromaticScaleValues[current - 1])
            }
        }
        return next
    }

    fun isSharpNote(note : SoundNote?) : Boolean
    {
        return when(note?.noteName) {
            NoteNames.ASharp,
            NoteNames.GSharp,
            NoteNames.FSharp,
            NoteNames.DSharp,
            NoteNames.CSharp -> true
            else -> false
        }
    }
}