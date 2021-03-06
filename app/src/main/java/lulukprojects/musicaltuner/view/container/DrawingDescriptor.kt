package lulukprojects.musicaltuner.view.container

import lulukprojects.musicaltuner.view.enums.DrawBars
import lulukprojects.musicaltuner.view.enums.NoteState
import lulukprojects.musicaltuner.view.helper.NotesHelper
import kotlin.math.abs

class DrawingDescriptor(closestNote: SoundNote, noteValue: Double, middleY: Double, var nextNoteDistance: Double) {
    var drawBars : DrawBars = DrawBars.Normal
    var noteState : NoteState
    var note : SoundNote = closestNote
    var noteY : Double

    init {
        //Get lower and upper notes and flow direction
        var closestNoteIsDown = false
        val upperNote: SoundNote?
        val lowerNote: SoundNote?
        if (closestNote.noteValue >= noteValue) {
            upperNote = closestNote
            lowerNote = NotesHelper.instance.getPreviousNote(closestNote)
        }
        else {
            lowerNote = closestNote
            upperNote = NotesHelper.instance.getNextNote(closestNote)

            closestNoteIsDown = true
        }

        //Get noteY and distances
        var noteDistance = 1.0
        var distanceToNote = 1.0
        if(upperNote != null && lowerNote != null) {
            this.note = upperNote
            noteDistance = upperNote.noteValue - lowerNote.noteValue
            distanceToNote = upperNote.noteValue - noteValue
        }
        else if(upperNote != null && lowerNote == null) {
            this.note = upperNote
            noteDistance = upperNote.noteValue
            distanceToNote = upperNote.noteValue - noteValue
            drawBars = DrawBars.NoLower
        }
        else if(upperNote == null && lowerNote != null) {
            this.note = lowerNote
            noteDistance = lowerNote.noteValue - (NotesHelper.instance.getPreviousNote(lowerNote)?.noteValue ?: lowerNote.noteValue - 16)
            distanceToNote = lowerNote.noteValue - noteValue
            drawBars = DrawBars.NoUpper
        }
        noteY = middleY - nextNoteDistance * distanceToNote / noteDistance
        //Get note state
        noteState = getNoteState(closestNoteIsDown, abs( closestNote.noteValue - noteValue), noteDistance / 2.0)
    }

    private fun getNoteState(closestNoteIsDown : Boolean, targetDistance : Double, noteDistance : Double) : NoteState {
        val rate = targetDistance / noteDistance * 100.0
        return if(rate < 5)
            NoteState.CloseToPerfect
        else if (rate < 25)
            if(closestNoteIsDown) NoteState.LittleHigh else NoteState.LittleLow
        else if (rate < 50)
            if(closestNoteIsDown) NoteState.High else NoteState.Low
        else
            if(closestNoteIsDown) NoteState.TooHigh else NoteState.TooLow
    }
}