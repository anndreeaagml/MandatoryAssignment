
package com.example.mandatoryassignment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
//import kotlinx.android.syntetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    private var user: TextInputEditText? = null
    private var password: TextInputEditText? = null
    private var repeatpassword: TextInputEditText? = null
    private var errorMessage: TextView? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regiser)
        user = findViewById(R.id.registerUsername)
        password = findViewById(R.id.registerPassword)
        repeatpassword = findViewById(R.id.repeatPassword)
        errorMessage = findViewById(R.id.errorText)
        mAuth = FirebaseAuth.getInstance()
    }

    @SuppressLint("SetTextI18n")
    fun Register(v: View?) {
        if (password!!.text.toString() == repeatpassword!!.text.toString()) {
            mAuth!!.createUserWithEmailAndPassword(user!!.text.toString(), password!!.text.toString())
                    .addOnSuccessListener(this) { authResult: AuthResult ->
                        Log.d("banana", "createUserWIthEmail : success")
                        Toast.makeText(this@RegisterActivity, "Successfully Created User.", Toast.LENGTH_SHORT).show()
                        val u = authResult.user
                        goToLogInPage()
                    }.addOnFailureListener(this) { e: Exception ->
                        Log.w("banana", "createUserWithEmail : failure", e)
                        Toast.makeText(this@RegisterActivity, "Registration Failed.", Toast.LENGTH_SHORT).show()
                        errorMessage!!.text = """Registration error: ${e.message}""".trimIndent()
                        errorMessage!!.visibility = View.VISIBLE
                    }
        } else {
            errorMessage!!.text = "Passwords do not mach"
            errorMessage!!.visibility = View.VISIBLE
        }
    }

    fun goToLogInPage() {
        val gotoLogIn = Intent(this, MainActivity::class.java)
        startActivity(gotoLogIn)
    }
}