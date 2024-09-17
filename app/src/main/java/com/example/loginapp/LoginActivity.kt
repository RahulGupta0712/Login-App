package com.example.loginapp

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.shashank.sony.fancytoastlib.FancyToast

class LoginActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()



        binding.loginButton2.setOnClickListener{
            val email = binding.emailEditText2.text.toString()
            val password = binding.passwordEditText2.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                FancyToast.makeText(this, "Incomplete credentials", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
            }
            else{
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val user = auth.currentUser!!
                        if(user.isEmailVerified){
                            FancyToast.makeText(this, "Login Successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                            startActivity(Intent(this, MainScreen::class.java))
                            finishAffinity()
                        }
                        else{
                            FancyToast.makeText(this, "Email not verified", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
                            auth.signOut()
                        }

                    }
                    .addOnFailureListener{ e ->
                        FancyToast.makeText(this, "Error : ${e.message}", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                    }
            }
        }

        // showing/hiding password in editText
        binding.viewPasswordButton2.setOnClickListener {
            // Toggle the password visibility based on the current state
            if (binding.passwordEditText2.transformationMethod is PasswordTransformationMethod) {
                // Password is currently hidden, make it visible
                binding.passwordEditText2.transformationMethod = null
                binding.viewPasswordButton2.setImageResource(R.drawable.hide_password) // Replace with your hide password icon
            } else {
                // Password is currently visible, make it hidden
                binding.passwordEditText2.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.viewPasswordButton2.setImageResource(R.drawable.show_password) // Replace with your show password icon
            }

            // Force the EditText to redraw to reflect the change
            binding.passwordEditText2.setSelection(binding.passwordEditText2.length())
        }

        binding.registerButton2.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.forgotPasswordButton.setOnClickListener{
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}