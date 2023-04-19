package com.apple.glantrox.kajeean_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.databinding.ActivityStartBinding
import com.apple.glantrox.kajeean_app.ui.authorization.LoginActivity
import com.apple.glantrox.kajeean_app.ui.authorization.RegisterActivity

class StartActivity : AppCompatActivity() {

    val binding: ActivityStartBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Automatically Go to MainActivity when User Already Log-in
        val loginStatus = GeneralPreferences(this@StartActivity).UserID
        if (loginStatus >= 1) {
            startActivity(Intent(this@StartActivity, MainActivity::class.java))
            finish()
        }


        binding.avStartCardSignin.setOnClickListener {
            startActivity(Intent(this@StartActivity, LoginActivity::class.java))
        }
        binding.avStartCardSignup.setOnClickListener {
            startActivity(Intent(this@StartActivity, RegisterActivity::class.java ))
        }



    }
}