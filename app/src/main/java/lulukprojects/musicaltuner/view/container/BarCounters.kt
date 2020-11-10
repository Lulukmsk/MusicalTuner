package lulukprojects.musicaltuner.view.container

data class BarCounters(val steps: Int) {
    var smallBarsCounter : Int = 0
    var nextBarY : Double = 0.0
    var note : SoundNote? = null
    var stepDistance : Double = 0.0
}