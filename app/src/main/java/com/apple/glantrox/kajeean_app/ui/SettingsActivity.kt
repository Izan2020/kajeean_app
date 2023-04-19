package com.apple.glantrox.kajeean_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ActivityMainBinding
import com.apple.glantrox.kajeean_app.databinding.ActivitySettingsBinding
import com.apple.glantrox.kajeean_app.ui.authorization.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    val binding: ActivitySettingsBinding by viewBinding()
    var finishCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Status Bar Color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //APIs
        val api = SupabaseAPI.create()

        // ID that been saved in PreferenceManager
        val userID = GeneralPreferences(this@SettingsActivity).UserID
        val kajianID = GeneralPreferences(this@SettingsActivity).KajianID

        // Back Button
        binding.avSettingsImgBtnBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        binding.cardLogout.setOnClickListener {
            MaterialAlertDialogBuilder(this@SettingsActivity)
                .setTitle("Log-out?")
                .setMessage("Are you sure want to log-out from this Account?")
                .setNegativeButton("No") {_,_ ->}
                .setPositiveButton("Yes") {_,_ ->
                    GeneralPreferences(this@SettingsActivity).UserID = 0
                    Toast.makeText(this@SettingsActivity, "You are Logged-Out!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SettingsActivity, StartActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()

                }
                .create()
                .show()

        }



        // Bindings to Preview User details
        lifecycleScope.launch {

            binding.swipeContainer.isRefreshing = true // Refreshing
            val preview = api.getCurrentUser("*","eq.$userID")
            binding.swipeContainer.isRefreshing = false // Remove Refreshing Animation

            binding.avSettingsTvNameAccount.text = preview[0].fullName
            binding.avSettingsTvEmailAccount.text = preview[0].email
            binding.avSettingsTvPhonenumberAccount.text = preview[0].phoneNumber
            binding.imgUser.load(preview[0].profilePicture)

        }

        //Goto Edit Sends data From Main Activity
        binding.avSettingsCardGotoEdit.setOnClickListener {
            startActivity(Intent(this@SettingsActivity, UpdateUserActivity::class.java))
        }



    }
}