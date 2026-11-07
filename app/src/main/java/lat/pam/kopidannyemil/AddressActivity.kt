package lat.pam.kopidannyemil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_address)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etName = findViewById<EditText>(R.id.et_name)
        val currentName = UserSession.getUserName(this)
        if (currentName != "Nama") {
            etName?.setText(currentName)
        }

        val totalPrice = intent.getIntExtra("TOTAL_PRICE", 0)
        val btnOrderSend = findViewById<Button>(R.id.btn_order_send)
        if (totalPrice > 0) {
            btnOrderSend.text = getString(
                R.string.order_dan_kirim_total,
                OrderAdapter.formatRupiah(totalPrice),
            )
        }

        btnOrderSend?.setOnClickListener {
            val enteredName = etName?.text?.toString()?.trim().orEmpty()
            if (enteredName.isNotEmpty()) {
                UserSession.saveUser(this, enteredName)
            }
            startActivity(Intent(this, ConfirmActivity::class.java))
            finish()
        }
    }
}
