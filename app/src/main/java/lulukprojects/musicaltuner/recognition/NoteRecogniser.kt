package lulukprojects.musicaltuner.recognition

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator
import kotlin.math.PI
import kotlin.math.cos
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import kotlin.math.pow

class NoteRecogniser {

    private var recordingConf: RecordingConfiguration
    private var samplingResults : MutableList<Short>
    private var interpolator : UnivariateInterpolator =  SplineInterpolator()
    constructor(samplingResults  : MutableList<Short>, recordingConf: RecordingConfiguration) {
        this.samplingResults = samplingResults
        this.recordingConf = recordingConf
    }

    fun getNote() : Double {
        var ret : Double
        while (true) {
            if(samplingResults.size >= recordingConf.samplingCut) {
                val data =  samplingResults.toTypedArray().clone()
                samplingResults.clear()
                var audioPowerSpectrum = getSignalPowerSpectrum(data)
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

    private fun getSignalPowerSpectrum(audioSamplingDataResults: Array<Short>) : DoubleArray
    {
        var ret = DoubleArray(recordingConf.samplingRate / 2)
        val fftTransformer = FastFourierTransformer(DftNormalization.STANDARD)

        var tmpdata : MutableList<Short>
        if (recordingConf.samplingRate < audioSamplingDataResults.size) {
            tmpdata = audioSamplingDataResults.copyOfRange(0, recordingConf.samplingRate).toMutableList()
        }
        else{
            tmpdata = audioSamplingDataResults.clone().toMutableList()
            tmpdata.addAll(ShortArray(recordingConf.samplingRate - audioSamplingDataResults.size).toTypedArray())
        }

        var data = tmpdata.mapIndexedNotNull{ index, x -> x.toDouble() * getHannWindow(
            index,
            recordingConf.samplingRate
        )}.toDoubleArray()

        val fftResults = fftTransformer.transform(data, TransformType.FORWARD)
        for (i in 0 until recordingConf.samplingRate / 2) {
            ret[i] += fftResults[i].argument.pow(2) + fftResults[i].imaginary.pow(2)
        }

        return ret
    }

    // Return index where to note signal detected
    private fun getNoteSignalRawExistence(powerSpectrum: DoubleArray) : Int
    {
        var minSearchIndex = 0
        var maxSearchIndex = powerSpectrum.size
        if (powerSpectrum.size > 40) {
            minSearchIndex = 40
        }
        if (powerSpectrum.size > 600) {
            maxSearchIndex = 600
        }

        var maxValue = 0.0
        val searchPower = 10
        for (i in minSearchIndex until maxSearchIndex){

            var searchValue : Double = if(powerSpectrum[i] > recordingConf.powerSpectrumNoise) powerSpectrum[i] else 0.0
            if(searchValue != 0.0) {
                if(maxValue == 0.0) {
                    maxValue = searchValue
                }
                else {
                    if (maxValue * searchPower < searchValue) {
                        return i
                    } else if (maxValue < searchValue) {
                        maxValue = searchValue
                    }
                }
            }
        }
        return 0
    }

    private fun getNoteFrequency(powerSpectrum: DoubleArray, signalIndex : Int) : Double
    {
        var minSearchIndex = signalIndex
        var maxSearchIndex = signalIndex
        if(signalIndex > 10) {
            minSearchIndex = signalIndex - 10
        }
        if (powerSpectrum.size > signalIndex + 10) {
            maxSearchIndex = signalIndex + 10
        }

        val searchSubset = powerSpectrum.copyOfRange(minSearchIndex,maxSearchIndex)


        //Interpolation to get max value
        var interpolationFunction = interpolator.interpolate(searchSubset.indices.map { i -> i.toDouble() }.toDoubleArray(), searchSubset)

        var maxFrequency : Double = 0.0
        var maxValue : Double = 0.0
        val step = 0.1

        for (i in 0..((searchSubset.size - 1) * (1/step).toInt())) {
            var frequency = i.toDouble() * step
            var value = interpolationFunction.value(frequency)
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