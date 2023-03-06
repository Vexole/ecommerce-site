package com.group1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        firebaseAuth = FirebaseAuth.getInstance()
         val txtNewAccount : TextView = findViewById(R.id.txtNewAccount)
        val btnSignIn : Button = findViewById(R.id.btnSignIn)

        txtNewAccount.setOnClickListener {
            var intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnSignIn.setOnClickListener {
            val email : EditText = findViewById(R.id.signInEmail)
            val strEmail = email.text.toString()

            val password : EditText = findViewById(R.id.signInPassword)
            val strPassword = password.text.toString()

            if(strEmail.isNotEmpty() && strPassword.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener{
                    if(it.isSuccessful) {
                        var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}