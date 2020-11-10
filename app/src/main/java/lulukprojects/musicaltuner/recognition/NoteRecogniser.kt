package lulukprojects.musicaltuner.recognition

import kotlin.math.PI
import kotlin.math.cos
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import kotlin.math.pow

class NoteRecogniser {
    private val powerSpectrumNoise = 10000000

    private var samplingRate : Int
    private var samplingResults : MutableList<Short>

    constructor(samplingResults  : MutableList<Short>, samplingRate : Int) {
        this.samplingResults = samplingResults
        this.samplingRate = samplingRate
    }

    fun getNote() : Double {
        var ret : Double
        while (true) {
            if(samplingResults.size >= samplingRate) {
                val data =  samplingResults.toTypedArray().clone()
                samplingResults.clear()
                var audioPowerSpectrum = getSignalPowerSpectrum(data)
                val detectedSignal = getNoteSignalRawExistence(audioPowerSpectrum)
                if (detectedSignal != 0) {
                    ret = getNoteFrequency(audioPowerSpectrum, detectedSignal).toDouble()
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
        var ret = DoubleArray(samplingRate / 2)
        val fftTransformer = FastFourierTransformer(DftNormalization.STANDARD)
        if (samplingRate < audioSamplingDataResults.size) {
            var data = audioSamplingDataResults.copyOfRange(0, samplingRate).mapIndexedNotNull{ index, x -> x.toDouble() * getHannWindow(
                index,
                samplingRate
            )}.toDoubleArray()
            val fftResults = fftTransformer.transform(data, TransformType.FORWARD)
            for (i in 0 until samplingRate/2) {
                ret[i] += fftResults[i].argument.pow(2) + fftResults[i].imaginary.pow(2)
            }
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

            var searchValue : Double = if(powerSpectrum[i] > powerSpectrumNoise) powerSpectrum[i] else 0.0
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

    private fun getNoteFrequency(powerSpectrum: DoubleArray, signalIndex : Int) : Int
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
        val max = searchSubset.max()

        return searchSubset.indexOfFirst { x -> x == max } + minSearchIndex
    }

    private fun getHannWindow(n: Int, N: Int) : Double {
        val a0 = 0.53836
        val a1 = 0.46164
        return a0 - a1 * cos(2 * PI * (n / N))
    }
}