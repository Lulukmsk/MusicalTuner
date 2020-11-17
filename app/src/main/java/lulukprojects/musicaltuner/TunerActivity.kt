package lulukprojects.musicaltuner

import android.Manifest
import android.media.AudioManager
import android.media.AudioRecord
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tuner.*
import lulukprojects.musicaltuner.recognition.NoteRecogniser
import lulukprojects.musicaltuner.recognition.NoteRecorder
import lulukprojects.musicaltuner.recognition.RecordingConfiguration
import lulukprojects.musicaltuner.requester.PermissionRequester
import java.util.*

class TunerActivity : AppCompatActivity() {

    //Sound permissions
    private val permissions = listOf(Manifest.permission.RECORD_AUDIO)
    private val PERMISSIONS_REQUEST_CODE = 1
    private lateinit var permissionRequester: PermissionRequester

    //Sound containers
    private var audioManager: AudioManager? = null
    private var recordingResults : MutableList<Short> = Collections.synchronizedList(mutableListOf<Short>())

    //Recogniser and recorder
    private var recorder : NoteRecorder? = null
    private var recogniser : NoteRecogniser? = null

    //Recognition and recording threads
    private var mAudioRecognitionThread: Thread? = null
    private var mAudioRecordThread: Thread? = null

    // Rate of the recording
    private val recordingRate = 32768

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuner)

        permissionRequester = PermissionRequester(this, permissions, PERMISSIONS_REQUEST_CODE)
        permissionRequester.requestPermissions()

        if(permissionRequester.isPermissionGranted()){
            audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            audioManager?.getProperty(AudioManager.PROPERTY_SUPPORT_AUDIO_SOURCE_UNPROCESSED)

            // Create recorder
            val recordingConfig = RecordingConfiguration(recordingRate)
            val bufferSize = AudioRecord.getMinBufferSize(recordingConfig.samplingRate, recordingConfig.channelConfig, recordingConfig.audioFormat)
            val audioRecorder = AudioRecord(recordingConfig.audioSource, recordingConfig.samplingRate, recordingConfig.channelConfig, recordingConfig.audioFormat, bufferSize)
            recorder = NoteRecorder(recordingResults, audioRecorder, bufferSize, recordingConfig)
            recogniser = NoteRecogniser(recordingResults, recordingConfig)

            mAudioRecordThread = Thread { recorder?.start() }
            mAudioRecognitionThread = Thread { doRecognition() }
        }
    }

    private fun doRecognition() {
        while(true) {
            val note =  recogniser?.getNote()
            if(note != null) {
                noteView.noteValue = note
                noteView.invalidate()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        mAudioRecordThread?.start()
        mAudioRecognitionThread?.start()
    }

    override fun onPause() {
        super.onPause()
        mAudioRecordThread?.stop()
        recorder?.stop()
        mAudioRecognitionThread?.stop()
    }
}