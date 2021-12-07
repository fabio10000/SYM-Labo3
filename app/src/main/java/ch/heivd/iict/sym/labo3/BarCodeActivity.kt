package ch.heivd.iict.sym.labo3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanIntentResult

import android.graphics.BitmapFactory

class BarCodeActivity : AppCompatActivity(){

    private lateinit var scan_btn: Button
    private lateinit var barCode_img: ImageView
    private lateinit var content_txt: TextView


    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(ScanContract()) {
        result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this@BarCodeActivity, "Scanned cancelled", Toast.LENGTH_LONG).show()
        } else {

            // display the image of bar code and content of what was decoded
            scan_btn.setText("Scan again")
            barCode_img.setImageBitmap(BitmapFactory.decodeFile(result.barcodeImagePath))
            content_txt.setText("Contenu décodé : \n\n" + result.contents) // TODO add label?

            //"Scanned :" + result.contents + "\n" + "ImagePath : " + result.barcodeImagePath,
            Toast.makeText(this@BarCodeActivity, "Scanned successfully", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_code)

        scan_btn = findViewById(R.id.bar_code_scan_bt)
        barCode_img = findViewById(R.id.bar_code_image)
        content_txt = findViewById(R.id.bar_code_txt_content)


        scan_btn.setOnClickListener {

            // customize scan option
            val scanOptions = ScanOptions()
            scanOptions.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            scanOptions.setPrompt("Scan anything")
            scanOptions.setOrientationLocked(false)
            scanOptions.setBarcodeImageEnabled(true)

            // launch scanner
            barcodeLauncher.launch(scanOptions)
        }
    }
}