package lulukprojects.musicaltuner.recognition

import android.media.AudioFormat
import android.media.MediaRecorder

data class RecrordingConfiguration(val samplingRate : Int) {
    val audioSource : Int = MediaRecorder.AudioSource.MIC
    val channelConfig: Int = AudioFormat.CHANNEL_IN_DEFAULT
    val audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT

    val samplingCutter: Double = samplingRate.toDouble() / 2
    val samplingMultiplier: Double = samplingRate.toDouble() * 16
    val powerSpectrumNoise = 10000000
}