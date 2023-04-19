package com.apple.glantrox.kajeean_app.ui.authorization

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ActivityRegisterBinding
import com.apple.glantrox.kajeean_app.models.Register
import com.apple.glantrox.kajeean_app.ui.MainActivity
import com.apple.glantrox.kajeean_app.ui.StartActivity
import com.blankj.utilcode.util.EncryptUtils
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {

    val binding: ActivityRegisterBinding by viewBinding()
    private var role: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Bindings : Additional
        val backButton = binding.avRegisterImageviewBack
        val gotoLogin = binding.avRegisterTextviewLogin
        val refreshLayout = binding.swipeContainer

        // APIs
        val api = SupabaseAPI.create()

        // Swipe Refresh
        refreshLayout.setOnRefreshListener {
            recreate()
            refreshLayout.isRefreshing = false
        }

        // Back Button
        backButton.setOnClickListener {
            super.onBackPressed()
            finish()
        }
        // Goto Login
        gotoLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        binding.checkRegisterAsUstadz.setOnCheckedChangeListener { compoundButton, isChecked ->
            if(isChecked) {

            } else {
                binding.checkRegisterAsUstadz.isChecked = false
            }

        }



        // Register Button
        val registerBtn = binding.avRegisterCardBtnRegister

       registerBtn.setOnClickListener {
           // Insert Data From Edit Text Input
           val city = binding.iptAddress.text.toString()
           val email = binding.iptEmail.text.toString()
           val fullname = binding.iptFullname.text.toString()
           val password = binding.iptPassword.text.toString()
           val confirmPassword = binding.iptConfirmpassword.text.toString()
           val phoneNumber = binding.iptPhonenumber.text.toString()
           val radioButtonUstadz = binding.checkRegisterAsUstadz

           // Encrypted Password
           val encryptedPassword = EncryptUtils.encryptMD5ToString(password)

           // Get Role for Ustadz or Normal User
           val role = if(radioButtonUstadz.isChecked) { "ustadz" } else { "user" }


           if (fullname.isEmpty()) {
               Toast.makeText(this@RegisterActivity, " 'Fullname' Kosong!", Toast.LENGTH_SHORT).show()
               return@setOnClickListener
           }
           if (email.isEmpty()) {
               Toast.makeText(this@RegisterActivity, " 'Email' Kosong!", Toast.LENGTH_SHORT).show()
               return@setOnClickListener
           }
           if (phoneNumber.isEmpty()) {
               Toast.makeText(this@RegisterActivity, "'Phone Number' Kosong!", Toast.LENGTH_SHORT).show()
               return@setOnClickListener
           }
           if (city.isEmpty()) {
               Toast.makeText(this@RegisterActivity, " 'City' Kosong!", Toast.LENGTH_SHORT).show()
               return@setOnClickListener
           }
           if (password.isEmpty()) {
               Toast.makeText(this@RegisterActivity, " 'Password' Kosong!", Toast.LENGTH_SHORT).show()
               return@setOnClickListener
           }
           if (confirmPassword.isEmpty()) {
               Toast.makeText(this@RegisterActivity, "Samakan Password!", Toast.LENGTH_SHORT).show()
               return@setOnClickListener
           }
           if(confirmPassword != password) {
               Toast.makeText(this@RegisterActivity, "Password Tidak Sama!", Toast.LENGTH_SHORT).show()
               return@setOnClickListener
           }

               try {
                   // API : Register User
                   lifecycleScope.launch {
                       val register = Register(
                           city,
                           email,
                           fullname,
                           encryptedPassword,
                           phoneNumber,
                           role,
                       )
                       refreshLayout.isRefreshing = true
                       api.registerUser(register)
                       refreshLayout.isRefreshing = false
                       startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                       finish()
                   }
               } catch (e: Exception) {
                   Toast.makeText(this@RegisterActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                   startActivity(Intent(this@RegisterActivity, StartActivity::class.java))
               }




       }










    }
}