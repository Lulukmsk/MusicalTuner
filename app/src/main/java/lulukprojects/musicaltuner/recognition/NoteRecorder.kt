package lulukprojects.musicaltuner.recognition

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import java.util.*

class NoteRecorder {

    private val samplingRate = 32768
    private var samplingResults : MutableList<Short>
    private var recorderInternal : AudioRecord
    private var bufferSize : Int

    constructor(samplingResults  : MutableList<Short>, recorder : AudioRecord, bufferSize : Int) {
        this.bufferSize = bufferSize
        recorderInternal = recorder
        this.samplingResults = samplingResults
    }

    fun start() {
        var buffer = ShortArray(bufferSize / 4)
        recorderInternal.startRecording()
        while (true) {
            if(samplingResults.size < samplingRate) {
                while (samplingResults.size < samplingRate) {
                    recorderInternal.read(buffer, 0, bufferSize / 4)
                    samplingResults.addAll(buffer.toList())
                }
            }
            else {
                Thread.sleep(100)
            }
        }
    }

    fun stop() {
        recorderInternal.stop()
    }
}