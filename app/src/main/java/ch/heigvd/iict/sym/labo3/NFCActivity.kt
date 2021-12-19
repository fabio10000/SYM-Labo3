package ch.heigvd.iict.sym.labo3

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView

class NFCActivity : AppCompatActivity() {
    private lateinit var levelText: TextView
    private var currentAuthlvl = AUTHENTICATE_LOW
    private lateinit var mNfcAdapter: NfcAdapter

    private val timer = object: CountDownTimer((AUTHENTICATE_MAX * 1000).toLong(), 1000) {
        override fun onTick(p0: Long) {
            currentAuthlvl = (p0 / 1000).toInt()

            if (currentAuthlvl == AUTHENTICATE_MEDIUM || currentAuthlvl == AUTHENTICATE_LOW) {
                setAuthLvlMessage(currentAuthlvl)
            }
        }

        override fun onFinish() { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)

        levelText = findViewById(R.id.securityLevelTxt)

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        setupForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        stopForegroundDispatch()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent != null) {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                    val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                    var authorized = false
                    for (message in messages) {
                        for (record in message.records) {
                            if (ndefRecordToString(record) == AUTHORIZED_TAG) {
                                timer.cancel()
                                currentAuthlvl = AUTHENTICATE_MAX
                                setAuthLvlMessage(currentAuthlvl)
                                timer.start()
                                authorized = true
                                break
                            }
                        }
                        if (authorized) break
                    }
                }
            }
        }
    }

    private fun ndefRecordToString(ndefRecord: NdefRecord): String {
        return ndefRecord.payload.decodeToString(3)
    }

    // called in onResume()
    private fun setupForegroundDispatch() {
        val intent = Intent(this, this.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        // On souhaite être notifié uniquement pour les TAG au format NDEF
        filters[0] = IntentFilter()
        filters[0]!!.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]!!.addCategory(Intent.CATEGORY_DEFAULT)

        try {
            filters[0]!!.addDataType("text/plain")
        } catch (e: IntentFilter.MalformedMimeTypeException) { }

        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList)
    }

    // called in onPause()
    private fun stopForegroundDispatch() {
        mNfcAdapter.disableForegroundDispatch(this)
    }

    private fun setAuthLvlMessage(authLvl: Int) {
        Log.d("securityLvl", authLvl.toString())
        val message = when {
            authLvl > AUTHENTICATE_MEDIUM -> { getString(R.string.high_security_txt) }
            authLvl > AUTHENTICATE_LOW -> { getString(R.string.medium_security_txt) }
            else -> { getString(R.string.low_security_txt) }
        }

        levelText.text = message
    }

    companion object {
        const val AUTHENTICATE_MAX      = 15
        const val AUTHENTICATE_MEDIUM   = 7
        const val AUTHENTICATE_LOW      = 3
        const val AUTHORIZED_TAG        = "test"
//        const val AUTHORIZED_TAG        = "1 2 3 4"
//        const val AUTHORIZED_TAG        = "é è ê ë ē ė ę ě ĕ ə"
//        const val AUTHORIZED_TAG        = "♤ ♡ ♢ ♧"
    }
}