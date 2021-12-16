package ch.heigvd.iict.sym.labo3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var barCode_btn: Button
    private lateinit var beacon_btn : Button
    private lateinit var nfc_btn: Button

    private var permissionGranted: Boolean = false
    // activity which should launch after camera permission is granted
    private lateinit var activityClassCameraLauncher: Class<*>

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i("Permission: ", "Granted")
            val intent = Intent(this, activityClassCameraLauncher)
            startActivity(intent)
            permissionGranted = true
        } else {
            Log.i("Permission: ", "Denied")
            Toast.makeText(
                this.applicationContext,
                "This action need camera permission",
                Toast.LENGTH_LONG).show()
            permissionGranted = false
        }
    }

    // start an Activity which need camera permission
    fun startActivityWithCamera(activityClass: Class<*>){

        if (!permissionGranted) {
            Log.i("Permission","Ask permission")
            activityClassCameraLauncher = activityClass
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        if(permissionGranted){
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        barCode_btn = findViewById(R.id.main_barCode_bt)
        beacon_btn = findViewById(R.id.main_beacon_bt)
        nfc_btn = findViewById(R.id.main_nfc_bt)

        barCode_btn.setOnClickListener {
            startActivityWithCamera(BarCodeActivity::class.java)
        }

        //beacon_btn.setOnClickListener{
        //    startActivityWithCamera(BeaconActivity::class.java)
        //}


    }
}