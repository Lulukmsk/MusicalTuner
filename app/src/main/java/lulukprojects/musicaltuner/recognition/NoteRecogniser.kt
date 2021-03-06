package lulukprojects.musicaltuner.recognition

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator
import kotlin.math.PI
import kotlin.math.cos
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import kotlin.math.pow

class NoteRecogniser(
    private var samplingResults: MutableList<Short>,
    private var recordingConf: RecordingConfiguration
) {

    private var interpolator : UnivariateInterpolator =  SplineInterpolator()

    fun getNote() : Double {
        var ret = 0.0
        while (!Thread.currentThread().isInterrupted) {
            if(samplingResults.size >= recordingConf.samplingCut) {
                val data =  samplingResults.toTypedArray().clone()
                samplingResults.clear()
                val audioPowerSpectrum = getSignalPowerSpectrum(data)
                val detectedSignal = getNoteSignalRawExistence(audioPowerSpectrum)
                if (detectedSignal != 0) {
                    ret = getNoteFrequency(audioPowerSpectrum, detectedSignal)
                    break
                }
            }
            else {
                Thread.sleep(200)
            }
        }
        return ret
    }

    private fun getSignalPowerSpectrum(audioSamplingDataResults: Array<Short>) : DoubleArray {
        val ret = DoubleArray(recordingConf.samplingRate / 2)
        val fftTransformer = FastFourierTransformer(DftNormalization.STANDARD)

        val tmpData = audioSamplingDataResults.copyOfRange(0, recordingConf.samplingCut).mapIndexed { index, x -> x.toDouble() * getHannWindow(index, recordingConf.samplingCut) }.toMutableList()
        tmpData.addAll(DoubleArray(recordingConf.samplingRate - recordingConf.samplingCut).toTypedArray())

        val fftResults = fftTransformer.transform(tmpData.toDoubleArray(), TransformType.FORWARD)
        for (i in 0 until recordingConf.samplingRate / 2) {
            ret[i] += fftResults[i].argument.pow(2) + fftResults[i].imaginary.pow(2)
        }

        return ret
    }

    // Return index where to note signal detected
    private fun getNoteSignalRawExistence(powerSpectrum: DoubleArray) : Int {
        var minSearchIndex = 0
        var maxSearchIndex = powerSpectrum.size
        if (powerSpectrum.size > 40) {
            minSearchIndex = 40
        }
        if (powerSpectrum.size > 600) {
            maxSearchIndex = 600
        }

        var maxValue = 0.0
        for (i in minSearchIndex until maxSearchIndex){

            val searchValue : Double = if(powerSpectrum[i] > recordingConf.powerSpectrumNoise) powerSpectrum[i] else 0.0
            if(searchValue != 0.0) {
                if(maxValue == 0.0) {
                    maxValue = searchValue
                }
                else {
                    if (maxValue * recordingConf.powerSpectrumSearchPower < searchValue) {
                        return i
                    } else if (maxValue < searchValue) {
                        maxValue = searchValue
                    }
                }
            }
        }
        return 0
    }

    private fun getNoteFrequency(powerSpectrum: DoubleArray, signalIndex : Int) : Double {
        var minSearchIndex = signalIndex
        var maxSearchIndex = signalIndex
        if(signalIndex > recordingConf.powerSpectrumSearchWidth) {
            minSearchIndex = signalIndex - recordingConf.powerSpectrumSearchWidth
        }
        if (powerSpectrum.size > signalIndex + recordingConf.powerSpectrumSearchWidth) {
            maxSearchIndex = signalIndex + recordingConf.powerSpectrumSearchWidth
        }

        val searchSubset = powerSpectrum.copyOfRange(minSearchIndex,maxSearchIndex)

        //Interpolation to get max value
        val interpolationFunction = interpolator.interpolate(searchSubset.indices.map { i -> i.toDouble() }.toDoubleArray(), searchSubset)

        var maxFrequency = 0.0
        var maxValue = 0.0
        val step = 0.25

        for (i in 0..((searchSubset.size - 1) * (1/step).toInt())) {
            val frequency = i.toDouble() * step
            val value = interpolationFunction.value(frequency)
            if(value > maxValue){
                maxValue = value
                maxFrequency = frequency
            }
        }
        return maxFrequency + minSearchIndex
    }

    private fun getHannWindow(n: Int, N: Int) : Double {
        val a0 = 0.53836
        val a1 = 0.46164
        return a0 - a1 * cos(2 * PI * (n / N))
    }
}