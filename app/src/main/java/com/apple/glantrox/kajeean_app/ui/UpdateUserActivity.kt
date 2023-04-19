package com.apple.glantrox.kajeean_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.SharedPrerences.UrlPreferences
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ActivityUpdateUserBinding
import com.apple.glantrox.kajeean_app.models.Update
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import koleton.api.loadSkeleton
import kotlinx.coroutines.launch

class UpdateUserActivity : AppCompatActivity() {

    val binding: ActivityUpdateUserBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)



        // Status Bar Color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        //APIs
        val api = SupabaseAPI.create()

        // ID that been saved in PreferenceManager
        val userID = GeneralPreferences(this@UpdateUserActivity).UserID
        val kajianID = GeneralPreferences(this@UpdateUserActivity).KajianID

        // Back Button
        binding.avSettingsImgBtnBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }


        lifecycleScope.launch {


            binding.swipeContainer.isRefreshing = true
            val getUser = api.getCurrentUser("*","eq.$userID")
            binding.swipeContainer.isRefreshing = false


            binding.updateName.setText(getUser[0].fullName)
            binding.updateEmail.setText(getUser[0].email)
            binding.updatePhonenumber.setText(getUser[0].phoneNumber)
            binding.updateCity.setText(getUser[0].city)
            binding.previewImage.load(getUser[0].profilePicture)


        }

        // Change Profile Picture
        binding.cardImage.setOnClickListener {
            openAlbum()
        }

        // Updates User Data
        binding.avSettingsImgBtnSave.setOnClickListener {
            lifecycleScope.launch {
             val update = Update(
                 binding.updateCity.text.toString(),
                 binding.updateEmail.text.toString(),
                 binding.updateName.text.toString(),
                 binding.updatePhonenumber.text.toString(),
                 UrlPreferences(this@UpdateUserActivity).pfpUrl,
                 binding.updateDescription.text.toString()
             )

             binding.swipeContainer.isRefreshing = true
             val apiUpdate = api.updateUserProfile("eq.$userID", update)
             binding.swipeContainer.isRefreshing = false

                UrlPreferences(this@UpdateUserActivity).pfpUrl = ""
                Toast.makeText(this@UpdateUserActivity, "Profile has been Updated!", Toast.LENGTH_SHORT).show()
             super.onBackPressed()
             finish()

            }
        }




    }
    // Image Uploader
    val storage = Firebase.storage
    fun uploadFile(file: String) {
        val userID = GeneralPreferences(this).UserID
        storage.getReference("imageposter")
            .child("$userID")
            .child(file)
            .putFile(file.toUri())
            .addOnCompleteListener {
                if (!it.isSuccessful){
                    Toast.makeText(this@UpdateUserActivity, "Gagal upload file", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                it.result.storage.downloadUrl.addOnSuccessListener {
                    binding.swipeContainer.isRefreshing = true
                    val imageUrl = it.toString()
                    UrlPreferences(this).pfpUrl = imageUrl
                    binding.swipeContainer.isRefreshing = false
                    binding.previewImage.load(imageUrl)
                }
            }

    }
    // Open Album
    fun openAlbum(){
        PictureSelector.create(this@UpdateUserActivity)
            .openSystemGallery(SelectMimeType.ofImage())
            .setSelectMaxFileSize(1)
            .forSystemResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    val media = result?.first() ?: return
                    binding.previewImage.load(media.realPath)
                    uploadFile(media.path)
                }
                override fun onCancel() {}
            })
    }
}