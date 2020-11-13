package lulukprojects.musicaltuner.recognition

import android.media.AudioFormat
import android.media.MediaRecorder

data class RecordingConfiguration(val samplingRate : Int) {
    val audioSource : Int = MediaRecorder.AudioSource.MIC
    val channelConfig: Int = AudioFormat.CHANNEL_IN_DEFAULT
    val audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT

    val samplingCut: Int = samplingRate / 2

    val powerSpectrumNoise = 100000000
}