package lulukprojects.musicaltuner.recognition

import android.media.AudioRecord

class NoteRecorder {
    private var samplingResults : MutableList<Short>
    private var recorderInternal : AudioRecord
    private var bufferSize : Int
    private var recordingConf : RecordingConfiguration

    constructor(samplingResults  : MutableList<Short>, recorder : AudioRecord, bufferSize : Int, recordingConf: RecordingConfiguration) {
        this.bufferSize = bufferSize
        recorderInternal = recorder
        this.samplingResults = samplingResults
        this.recordingConf = recordingConf
    }

    fun start() {
        var buffer = ShortArray(bufferSize / 4)
        recorderInternal.startRecording()
        while (true) {
            if(samplingResults.size < this.recordingConf.samplingCut) {
                while (samplingResults.size < this.recordingConf.samplingCut) {
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