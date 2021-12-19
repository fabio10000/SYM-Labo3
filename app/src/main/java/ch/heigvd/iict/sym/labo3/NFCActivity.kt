package ch.heigvd.iict.sym.labo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView

class NFCActivity : AppCompatActivity() {
    private lateinit var levelText: TextView
    // todo: changer en low
    private var currentAuthlvl = AUTHENTICATE_MAX
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

        setAuthLvlMessage(currentAuthlvl)
        timer.start()
    }

    private fun setAuthLvlMessage(authLvl: Int) {
        Log.d("securityLvl", authLvl.toString())
        val message = when {
            authLvl > AUTHENTICATE_MEDIUM -> { "Max" }
            authLvl > AUTHENTICATE_LOW -> { "Medium" }
            else -> { "Low" }
        }

        levelText.text = message
    }

    companion object {
        const val AUTHENTICATE_MAX    = 15
        const val AUTHENTICATE_MEDIUM = 7
        const val AUTHENTICATE_LOW    = 3
    }
}