package ch.heigvd.iict.sym.labo3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var loginInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var connectBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginInput = findViewById(R.id.loginInput)
        passwordInput = findViewById(R.id.passwordInput)
        connectBtn = findViewById(R.id.loginBtn)

        connectBtn.setOnClickListener {
            val username = loginInput.text?.toString()
            val password = passwordInput.text?.toString()

            //hardcoded credentials
            if (username == "user" && password == "password") {
                val intent = Intent(this, NFCActivity::class.java)
                startActivity(intent)
            } else {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setMessage("Invalid credentials!")
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
                return@setOnClickListener
            }
        }
    }
}