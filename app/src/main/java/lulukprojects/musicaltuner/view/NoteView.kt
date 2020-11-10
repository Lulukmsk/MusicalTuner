package lulukprojects.musicaltuner.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import lulukprojects.musicaltuner.view.container.BarCounters
import lulukprojects.musicaltuner.view.container.SoundNote
import lulukprojects.musicaltuner.view.enums.DrawBarsStyle
import lulukprojects.musicaltuner.view.helper.NotesHelper


class NoteView : View {

    private var itemsPaint = Paint()
    private var directionSlowPaint = Paint()
    private var mainPaint = Paint()
    private var _selectedNoteValue: Double  = 0.0

    var noteValue: Double
        get() = _selectedNoteValue
        set(value) {
            _selectedNoteValue = value
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        NotesHelper()
        setBackgroundColor(Color.BLACK)

        itemsPaint.color = Color.WHITE
        mainPaint.color = Color.RED
        directionSlowPaint.color = Color.BLUE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        val middleY = (paddingTop + contentHeight.toDouble()/2)
        val nextNoteDistance = contentHeight.toDouble()/3

        var closestNote = NotesHelper.instance.getClosestNote(noteValue)

        if(closestNote != null) {
            var goDown: Boolean = false;
            var upperNote: SoundNote? = null
            var lowerNote: SoundNote? = null
            if (closestNote.noteValue >= noteValue) {
                upperNote = closestNote
                lowerNote = NotesHelper.instance.getPreviousNote(closestNote)
            }
            else {
                goDown = true
                lowerNote = closestNote
                upperNote = NotesHelper.instance.getNextNote(closestNote)
            }

            if(upperNote != null && lowerNote != null) {
                val noteDistance = upperNote.noteValue - lowerNote.noteValue
                val distanceToUpper = upperNote.noteValue - noteValue
                val upperY = middleY - nextNoteDistance * distanceToUpper / noteDistance

                drawFromNote(
                    canvas,
                    contentHeight.toDouble(),
                    contentWidth.toDouble(),
                    upperNote,
                    upperY,
                    nextNoteDistance,
                    DrawBarsStyle.Normal
                )
            }
            else if(upperNote != null && lowerNote == null) {
                val noteDistance = upperNote.noteValue
                val distanceToUpper = upperNote.noteValue - noteValue
                val upperY = middleY - nextNoteDistance * distanceToUpper / noteDistance

                drawFromNote(
                    canvas,
                    contentHeight.toDouble(),
                    contentWidth.toDouble(),
                    upperNote,
                    upperY,
                    nextNoteDistance,
                    DrawBarsStyle.NoLower
                )
            }
            else if(upperNote == null && lowerNote != null) {
                val noteDistance = lowerNote.noteValue - (NotesHelper.instance.getPreviousNote(
                    lowerNote
                )?.noteValue
                    ?: lowerNote.noteValue - 16)
                var distanceToLower = noteValue - lowerNote.noteValue
                var lowerY = middleY + nextNoteDistance * distanceToLower / noteDistance

                drawFromNote(
                    canvas,
                    contentHeight.toDouble(),
                    contentWidth.toDouble(),
                    lowerNote,
                    lowerY,
                    nextNoteDistance,
                    DrawBarsStyle.NoUpper
                )
            }
        }

        // Draw middle line
        canvas.drawLine(
            paddingLeft.toFloat(),
            middleY.toFloat(),
            contentWidth.toFloat(),
            middleY.toFloat(),
            mainPaint
        )
    }

    private fun drawFromNote(canvas: Canvas, maxHeight: Double, maxWidth: Double, note: SoundNote?, noteY: Double, nextNoteDistance: Double, style: DrawBarsStyle) {
        // Draw Note Line
        drawNote(canvas, maxWidth, note, noteY)

        // Draw lines and bars that are lower then current note
        var barCounter = BarCounters(30)
        barCounter.note = note
        barCounter.nextBarY = noteY + barCounter.stepDistance
        barCounter.stepDistance = nextNoteDistance/barCounter.steps

        while (barCounter.nextBarY < maxHeight){
            drawNoteBars(canvas, maxWidth, barCounter, style == DrawBarsStyle.NoLower, false)
        }

        // Draw lines and bars that are higher then current note
        barCounter.note = note
        barCounter.nextBarY = noteY - barCounter.stepDistance
        barCounter.stepDistance = -barCounter.stepDistance
        barCounter.smallBarsCounter = 0

        while (barCounter.nextBarY > 0){
            drawNoteBars(canvas, maxWidth, barCounter, style == DrawBarsStyle.NoUpper, true)
        }
    }

    private fun drawNoteBars(canvas: Canvas, maxWidth: Double, barCounter: BarCounters, noNoteBars: Boolean, takeNextNote: Boolean){
        barCounter.smallBarsCounter += 1
        if (barCounter.smallBarsCounter == barCounter.steps + 1 && barCounter.note != null && !noNoteBars) {
            var tmpNote : SoundNote? = if (takeNextNote) NotesHelper.instance.getNextNote(barCounter.note!!) else NotesHelper.instance.getPreviousNote(barCounter.note!!)
            if (tmpNote != null) {
                barCounter.note = tmpNote
                drawNote(canvas, maxWidth, barCounter.note, barCounter.nextBarY)
            }
            else
                drawBar(canvas, maxWidth, barCounter.nextBarY)
            barCounter.smallBarsCounter = 0
        }
        else {
            drawBar(canvas, maxWidth, barCounter.nextBarY)
        }
        barCounter.nextBarY += barCounter.stepDistance
    }

    private fun drawNote(canvas: Canvas, maxWidth: Double, note: SoundNote?, noteY: Double) {

        var linePadding = 50
        var textPadding = 10
        var textWidth = 70f

        if(NotesHelper.instance.isSharpNote(note)){
            linePadding = 80
            textPadding = 10
            textWidth = 140f
        }

        canvas.drawLine(
            paddingLeft.toFloat(),
            noteY.toFloat(),
            (maxWidth / 2 - linePadding).toFloat(),
            noteY.toFloat(),
            itemsPaint
        )

        setTextSize(itemsPaint, textWidth, note?.noteName.toString())

        canvas.drawText(
            note?.noteName.toString(),
            (maxWidth / 2 - (linePadding - textPadding)).toFloat(),
            noteY.toFloat(),
            itemsPaint
        )

        canvas.drawLine(
            (maxWidth / 2 + linePadding).toFloat(),
            noteY.toFloat(),
            maxWidth.toFloat(),
            noteY.toFloat(),
            itemsPaint)
    }

    private fun setTextSize(paint: Paint, desiredWidth: Float, text: String) {
        paint.textSize = 48f
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        paint.textSize = paint.textSize * desiredWidth / bounds.width()
    }

    private fun drawBar(canvas: Canvas, maxWidth: Double, barY: Double){
        canvas.drawLine(
            paddingLeft.toFloat(),
            barY.toFloat(),
            (maxWidth / 20).toFloat(),
            barY.toFloat(),
            itemsPaint
        )
    }
}