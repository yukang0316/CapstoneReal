package com.example.ccc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)

        val loginButton: Button = findViewById(R.id.login_button)
        val signUpButton: Button = findViewById(R.id.sign_up_button)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (isValidCredentials(email, password)) {
                navigateToMainActivity()
            } else {
                Toast.makeText(this, "잘못된 이메일 또는 비밀번호입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        signUpButton.setOnClickListener {
            navigateToSignUpActivity()
        }
    }

    private fun isValidCredentials(email: String, password: String): Boolean {
        // 서버에서 이메일과 비밀번호를 검증하는 코드 (가상)
        return email == "root" && password == "root"
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}