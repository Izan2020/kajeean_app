package com.apple.glantrox.kajeean_app.ui.authorization

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ActivityLoginBinding
import com.apple.glantrox.kajeean_app.ui.MainActivity
import com.apple.glantrox.kajeean_app.ui.StartActivity
import com.blankj.utilcode.util.EncryptUtils
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    val binding: ActivityLoginBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // APIs
        val api = SupabaseAPI.create()

        // Additional Binding
        val backButton = binding.avLoginImageviewBack
        val refreshLayout = binding.swipeContainer
        val gotoRegister = binding.avLoginTextviewRegister


        // Back Button
        backButton.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        // Refresh Activity
        refreshLayout.setOnRefreshListener {
            recreate()
        }

        // Goto Register
        gotoRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }



        val btnLogin = binding.avLoginCardBtnLogin

        btnLogin.setOnClickListener {
            // Input Bindings
            val email = binding.signEmail.text.toString()
            val password = binding.signPassword.text.toString()

            // Encrypted Password
            val encryptedPassword = EncryptUtils.encryptMD5ToString(password)

            if(email.isEmpty()) {
                Toast.makeText(this@LoginActivity, "'Email' Kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "'Password' Kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

             try {
                 lifecycleScope.launch {
                     // API Login
                     refreshLayout.isRefreshing = true
                     val apiLogin = api.loginUser(
                         "*",
                         "eq.$email",
                         "eq.$encryptedPassword"
                     )
                     if(apiLogin.isEmpty()) {
                         refreshLayout.isRefreshing = false
                         Toast.makeText(this@LoginActivity, "Login Failed, Check your password or email!", Toast.LENGTH_SHORT).show()
                     } else {
                         refreshLayout.isRefreshing = false
                         // Saves Login ID to Preference Manager
                         val loginResponse = apiLogin[0].id
                         GeneralPreferences(this@LoginActivity).UserID = loginResponse
                         startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                         finish()
                     }
                 }
             } catch (e: Exception) {
                 Toast.makeText(this@LoginActivity, "${e.message}", Toast.LENGTH_SHORT).show()
             }



        }






    }
}