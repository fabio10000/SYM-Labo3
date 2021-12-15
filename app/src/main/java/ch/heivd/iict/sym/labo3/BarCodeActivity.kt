package ch.heivd.iict.sym.labo3

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.google.zxing.BarcodeFormat
import java.util.*

class BarCodeActivity : AppCompatActivity(){

    private lateinit var barcode_view: DecoratedBarcodeView // the view for scanning barcodes
    private lateinit var beep_manager: BeepManager // manage sound after successfully scanned barcodes
    private lateinit var content_txt: TextView // used to display decoded data
    private lateinit var barCode_img: ImageView // used to display scanned barcode

    // called if a barcode is scanned
    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {

            // prevent duplicate scans
            if(result.text == null || result.text.equals(content_txt.text)) {
                return
            }

            beep_manager.playBeepSoundAndVibrate()
            barCode_img.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW))
            content_txt.text = result.text
        }
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beep_manager = BeepManager(this)

        // map elements with layout
        setContentView(R.layout.activity_bar_code)
        barcode_view = findViewById(R.id.barcode_scanner)
        content_txt = findViewById(R.id.bar_code_txt_content)
        barCode_img = findViewById(R.id.bar_code_image)

        // set barcode formats to 2D (QR_CODE) and 1D (CODE_39)
        val formats: Collection<BarcodeFormat> =
            Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcode_view.barcodeView.decoderFactory = DefaultDecoderFactory(formats)

        barcode_view.initializeFromIntent(intent)

        barcode_view.decodeContinuous(callback)

    }

    override fun onResume() {
        super.onResume()
        barcode_view.resume()
    }

    override fun onPause() {
        super.onPause()
        barcode_view.pause()
    }
}