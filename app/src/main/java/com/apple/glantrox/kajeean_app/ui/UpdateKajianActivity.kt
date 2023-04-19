package com.apple.glantrox.kajeean_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.SharedPrerences.InputKajianPreferences
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ActivityUpdateKajianBinding
import com.apple.glantrox.kajeean_app.models.UpdateKajian
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat


class UpdateKajianActivity : AppCompatActivity() {

    val binding: ActivityUpdateKajianBinding by viewBinding()

    private var defaultDate: Long = 0
    private var waktu: String = ""
    private var posterUrl: String = ""

    override fun onBackPressed() {
        super.onBackPressed()
        MaterialAlertDialogBuilder(this@UpdateKajianActivity)
            .setTitle("Yakin ingin kembali?")
            .setMessage("Perubahan yang anda buat tidak akan tersimpan!")
            .setNegativeButton("Tidak") {_,_ ->}
            .setPositiveButton("Ya") {_,_ ->
                super.onBackPressed()
                finish()
            }
            .show().create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_kajian)

        val idKajian = intent.getIntExtra("id",0)
        val api = SupabaseAPI.create()

        binding.avAttendImgBtnBack.setOnClickListener {
            MaterialAlertDialogBuilder(this@UpdateKajianActivity)
                .setTitle("Yakin ingin kembali?")
                .setMessage("Perubahan yang anda buat tidak akan tersimpan!")
                .setNegativeButton("Tidak") {_,_ ->}
                .setPositiveButton("Ya") {_,_ ->
                    super.onBackPressed()
                    finish()
                }
                .show().create()
        }

        binding.cardImage.setOnClickListener {
            openAlbum()
        }



        lifecycleScope.launch {
            val response = api.getKajianDetails("*","eq.${idKajian}")
            binding.prvImage.load(response[0].posterUrl)
            binding.prvJudul.setText(response[0].title)
            binding.prvAlamat.setText(response[0].addressDetail)
            binding.prvCity.setText(response[0].city)
            binding.prvDeskripsi.setText(response[0].description)
            binding.prvYtlink.setText(response[0].livestreamingLink)
            binding.imageLinkPreview.setText(response[0].posterUrl)
            binding.prvTimestamp.setText(response[0].startTime)

            val from = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val toDate = SimpleDateFormat("dd MMMM, yyyy")
            val toHour = SimpleDateFormat("HH:mm")

            val jam = toHour.format(from.parse(response[0].startTime))
            val date = toDate.format(from.parse(response[0].startTime))

            if(response[0].livestreamingLink.isNullOrEmpty()) {
                binding.layoutVideoyt.isGone = true
            } else {
                binding.layoutVideoyt.isVisible = true
                val urlParsedtoId = binding.prvYtlink.text.toString().removePrefix("https://www.youtube.com/watch?v=")
                binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        val videoId = urlParsedtoId.toString()
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })
            }

        }

        binding.prvDate.setOnClickListener {
            CardDatePickerDialog.builder(this)
                .setTitle("Update Kajian Date")
                .setDefaultTime(defaultDate)
                .setLabelText("Y","M","D","H","M","S")
                .setChooseDateModel(DateTimeConfig.GLOBAL_US)
                .setOnCancel("Cancel") {}
                .setThemeColor(R.color.mainColor)
                .showFocusDateInfo(false)
                .setOnChoose("Save") {


                    val dateLong = it
                    val date = Date(dateLong).toString()
                    val time = Time(dateLong).toString()

                    val timestamp = "$date $time"

                    val from = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val toDate = SimpleDateFormat("dd MMMM, yyyy")
                    val toHour = SimpleDateFormat("HH:mm")

                    val hourResult = toHour.format(from.parse(timestamp))
                    val dateResult = toDate.format(from.parse(timestamp))

                    binding.prvDate.setText(dateResult)
                    binding.prvHour.setText(hourResult)

                    binding.prvTimestamp.setText(timestamp)


                }.build().show()

        }

        binding.buttonPost.setOnClickListener {
            lifecycleScope.launch {
             val update = UpdateKajian(
                 binding.prvAlamat.text.toString(),
                 binding.prvCity.text.toString(),
                 binding.prvDeskripsi.text.toString(),
                 binding.prvYtlink.text.toString(),
                 binding.imageLinkPreview.text.toString(),
                 binding.prvTimestamp.text.toString(),
                 binding.prvJudul.text.toString(),
             )
             Log.d("cekupdatekajian",update.toString())
             val response = api.updateKajian("eq.$idKajian",update)

                Toast.makeText(this@UpdateKajianActivity, "Updated!", Toast.LENGTH_SHORT).show()
                super.onBackPressed()
                finish()


            }
        }



    }

    // Image Uploader
    private val storage = Firebase.storage
    private fun uploadFile(file: String) {
        val userID = GeneralPreferences(this).UserID
        storage.getReference("imageposter")
            .child("$userID")
            .child(file)
            .putFile(file.toUri())
            .addOnCompleteListener {
                if (!it.isSuccessful){
                    Toast.makeText(this@UpdateKajianActivity, "Gagal upload file", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                it.result.storage.downloadUrl.addOnSuccessListener {

                    binding.swipeContainer.isRefreshing = true
                    val imageUrl = it.toString()
                    binding.imageLinkPreview.setText(imageUrl)
                    InputKajianPreferences(this).linkPoster = imageUrl
                    binding.swipeContainer.isRefreshing = false
                    binding.prvImage.load(imageUrl)
                }
            }

    }
    // Open Album
    fun openAlbum(){
        PictureSelector.create(this@UpdateKajianActivity)
            .openSystemGallery(SelectMimeType.ofImage())
            .setSelectMaxFileSize(1)
            .forSystemResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    val media = result?.first() ?: return
//                    val file = File(media.realPath)
                    binding.prvImage.load(media.path)
                    uploadFile(media.path)
                }
                override fun onCancel() {}
            })
    }
}