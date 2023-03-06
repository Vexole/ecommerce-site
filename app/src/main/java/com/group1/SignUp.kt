package com.group1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignUp : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val btnSignUp : Button = findViewById(R.id.btnSignUp)
        val txtAlreadyAccount : TextView = findViewById(R.id.txtAlreadyAccount)

        firebaseAuth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(this)

        btnSignUp.setOnClickListener {
            val email : EditText = findViewById(R.id.signUpEmail)
            val strEmail = email.text.toString()

            val password : EditText = findViewById(R.id.signUpPassword)
            val strPassword = password.text.toString()

            val confirmPassword : EditText = findViewById(R.id.signUpConfirmPassword)
            val strConfirmPassword = confirmPassword.text.toString()

            if(strEmail.isNotEmpty() && strPassword.isNotEmpty() && strConfirmPassword.isNotEmpty()) {
                if(strPassword == strConfirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener{
                        if(it.isSuccessful) {
                            var intent = Intent(this, Login::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    firebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d("TAG", "createUserWithEmail:success")
                                val user = firebaseAuth.currentUser
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }

            txtAlreadyAccount.setOnClickListener {
                var intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }
    }
}