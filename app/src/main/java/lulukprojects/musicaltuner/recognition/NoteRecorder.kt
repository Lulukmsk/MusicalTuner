package lulukprojects.musicaltuner.recognition

import android.media.AudioRecord

class NoteRecorder(
    private var samplingResults: MutableList<Short>,
    private var recorder: AudioRecord,
    private var bufferSize: Int,
    private var recordingConf: RecordingConfiguration
) {

    fun start() {
        val buffer = ShortArray(bufferSize / 4)
        recorder.startRecording()
        while (true) {
            if(samplingResults.size < this.recordingConf.samplingCut) {
                while (samplingResults.size < this.recordingConf.samplingCut) {
                    recorder.read(buffer, 0, bufferSize / 4)
                    samplingResults.addAll(buffer.toList())
                }
            }
            else {
                Thread.sleep(100)
            }
        }
    }

    fun stop() {
        recorder.stop()
    }
}