package ch.heigvd.iict.sym.labo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ch.heigvd.iict.sym.labo3.LoginActivity
import ch.heigvd.iict.sym.labo3.R

class MainActivity : AppCompatActivity() {

    private lateinit var barCode_btn: Button
    private lateinit var beacon_btn : Button
    private lateinit var nfc_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        barCode_btn = findViewById(R.id.main_barCode_bt)
        beacon_btn = findViewById(R.id.main_beacon_bt)
        nfc_btn = findViewById(R.id.main_nfc_bt)

        nfc_btn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        barCode_btn.setOnClickListener {
            val intent = Intent(this, BarCodeActivity::class.java)
            startActivity(intent)
        }

        beacon_btn.setOnClickListener{
            startActivity(Intent(this, BeaconActivity::class.java))
        }
    }
}