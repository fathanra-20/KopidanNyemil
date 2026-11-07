package lat.pam.kopidannyemil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etName = findViewById<EditText>(R.id.et_name)
        val etUsername = findViewById<EditText>(R.id.et_username)

        findViewById<Button>(R.id.btn_register)?.setOnClickListener {
            val name = etName.text.toString().trim()
            val username = etUsername.text.toString().trim()

            if (name.isEmpty()) {
                etName.error = "Nama lengkap wajib diisi"
                Toast.makeText(this, "Silakan isi nama lengkap", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            UserSession.saveUser(this, name, username)

            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
