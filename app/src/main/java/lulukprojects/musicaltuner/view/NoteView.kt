package lulukprojects.musicaltuner.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import lulukprojects.musicaltuner.view.container.BarCounters
import lulukprojects.musicaltuner.view.container.DrawingDescriptor
import lulukprojects.musicaltuner.view.container.SoundNote
import lulukprojects.musicaltuner.view.enums.DrawBars
import lulukprojects.musicaltuner.view.enums.NoteState
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

    @SuppressLint("DrawAllocation")
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

        val closestNote = NotesHelper.instance.getClosestNote(noteValue)
        var descriptor : DrawingDescriptor? = null
        if(closestNote != null) {
            descriptor = DrawingDescriptor(closestNote, noteValue, middleY, nextNoteDistance)
            drawFromNote(
                canvas,
                contentHeight.toDouble(),
                contentWidth.toDouble(),
                descriptor
            )
        }

        // Draw middle line
        if(descriptor != null)
        {
            drawMainValue(canvas, descriptor, middleY, contentWidth, paddingLeft)
        }
    }

    private fun drawFromNote(canvas : Canvas, maxHeight : Double, maxWidth : Double, descriptor : DrawingDescriptor) {
        // Draw Note Line
        drawNote(canvas, maxWidth, descriptor.note, descriptor.noteY)

        // Draw lines and bars that are lower then current note
        val barCounter = BarCounters(30)
        barCounter.note = descriptor.note
        barCounter.nextBarY = descriptor.noteY + barCounter.stepDistance
        barCounter.stepDistance = descriptor.nextNoteDistance/barCounter.steps

        while (barCounter.nextBarY < maxHeight){
            drawNoteBars(canvas, maxWidth, barCounter, descriptor.drawBars == DrawBars.NoLower, false)
        }

        // Draw lines and bars that are higher then current note
        barCounter.note = descriptor.note
        barCounter.nextBarY = descriptor.noteY - barCounter.stepDistance
        barCounter.stepDistance = -barCounter.stepDistance
        barCounter.smallBarsCounter = 0

        while (barCounter.nextBarY > 0){
            drawNoteBars(canvas, maxWidth, barCounter, descriptor.drawBars == DrawBars.NoUpper, true)
        }
    }

    private fun drawNoteBars(canvas: Canvas, maxWidth: Double, barCounter: BarCounters, noNoteBars: Boolean, takeNextNote: Boolean){
        barCounter.smallBarsCounter += 1
        if (barCounter.smallBarsCounter == barCounter.steps + 1 && barCounter.note != null && !noNoteBars) {
            val tmpNote : SoundNote? = if (takeNextNote) NotesHelper.instance.getNextNote(barCounter.note!!) else NotesHelper.instance.getPreviousNote(barCounter.note!!)
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

    private fun drawMainValue(canvas: Canvas, descriptor : DrawingDescriptor, middleY : Double, contentWidth : Int, paddingLeft : Int) {
        changeMainPaintToMatchState(descriptor.noteState)
        canvas.drawLine(
            paddingLeft.toFloat(),
            middleY.toFloat(),
            contentWidth.toFloat(),
            middleY.toFloat(),
            mainPaint
        )
    }

    private fun changeMainPaintToMatchState(noteState : NoteState) {
        when(noteState){
            NoteState.CloseToPerfect -> { mainPaint.color = Color.GREEN }
            NoteState.LittleLow, NoteState.LittleHigh -> { mainPaint.color = Color.YELLOW }
            NoteState.Low, NoteState.High-> { mainPaint.color = Color.argb(255,255, 165, 0) }
            NoteState.TooLow, NoteState.TooHigh -> { mainPaint.color = Color.RED }
        }
    }

    private fun setTextSize(paint : Paint, desiredWidth : Float, text : String) {
        paint.textSize = 48f
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        paint.textSize = paint.textSize * desiredWidth / bounds.width()
    }

    private fun drawBar(canvas : Canvas, maxWidth : Double, barY : Double){
        canvas.drawLine(
            paddingLeft.toFloat(),
            barY.toFloat(),
            (maxWidth / 20).toFloat(),
            barY.toFloat(),
            itemsPaint
        )
    }
}