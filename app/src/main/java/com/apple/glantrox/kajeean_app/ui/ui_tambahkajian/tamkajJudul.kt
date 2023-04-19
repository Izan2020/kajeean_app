package com.apple.glantrox.kajeean_app.ui.ui_tambahkajian

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.adevinta.leku.*
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.SharedPrerences.InputKajianPreferences
import com.apple.glantrox.kajeean_app.databinding.ActivityTamkajJudulBinding
import com.apple.glantrox.kajeean_app.ui.fragments.MapsFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import java.sql.Date
import java.sql.Time




class tamkajJudul : AppCompatActivity() {

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this@tamkajJudul)
            .setTitle("Unsaved Changes")
            .setMessage("Perubahan yang telah anda buat tidak Akan Tersimpan, Apakah anda yakin Ingin Keluar?")
            .setNegativeButton("Tidak") {_,_ ->}
            .setPositiveButton("Iya") {_,_ ->
                super.onBackPressed()
                finish()
            }
            .create()
            .show()
    }



    private var defaultDate: Long = 0
    private var waktu: String = ""



    val binding: ActivityTamkajJudulBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tamkaj_judul)

        // Status Bar Color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        // ID that saved in PreferenceManager
        val userID = GeneralPreferences(this@tamkajJudul).UserID
        val kajianID = GeneralPreferences(this@tamkajJudul).KajianID




        // Execute OpenAlbum Function
        binding.inputposter.setOnClickListener {
            openAlbum()
        }

        // Button Back
        binding.avAttendImgBtnBack.setOnClickListener {
            MaterialAlertDialogBuilder(this@tamkajJudul)
                .setTitle("UNSAVED CHANGES")
                .setMessage("Perubahan yang telah anda buat tidak Akan Tersimpan, Apakah anda yakin Ingin Keluar?")
                .setNegativeButton("Tidak") {_,_ ->}
                .setPositiveButton("Iya") {_,_ ->
                    super.onBackPressed()
                    finish()
                }
                .create()
                .show()
        }
        





        // Input Time
        binding.inputTime.setOnClickListener {
            CardDatePickerDialog.builder(this)
                .setTitle("Set Kajian Date")
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

                    binding.inputTime.text = "$date $time"
                    waktu = timestamp


                }.build().show()


        }

        // Input Drafts
        if(binding.inputDetailAlamat.text!!.isNotEmpty()) {
            InputKajianPreferences(this).alamatDetail = binding.inputDetailAlamat.text.toString() }

       if(binding.inputCity.text!!.isNotEmpty()) {
           InputKajianPreferences(this).cityKajian = binding.inputCity.text.toString() }

        if(binding.inputlinklive.text!!.isNotEmpty()) {
            InputKajianPreferences(this).linkLive = binding.inputlinklive.text.toString() }

        if(binding.inputjudul.text!!.isNotEmpty()) {
            InputKajianPreferences(this@tamkajJudul).judulKajian = binding.inputlinklive.text.toString() }

        if(binding.inputdeskripsi.text!!.isNotEmpty()) {
            InputKajianPreferences(this@tamkajJudul).deskripsiKajian = binding.inputdeskripsi.text.toString() }



        // Leku Location Picker
        binding.inputLongLat.setOnClickListener {
            val bottomsheetLocation = MapsFragment().apply {  }.show(supportFragmentManager,"maps")
        }

        // Button go Next
        binding.btnNext.setOnClickListener {
            // Input Conditions
            val inputJudul = binding.inputjudul.text.toString()
            val inputDeskripsi = binding.inputdeskripsi.text.toString()
            val inputPoster = InputKajianPreferences(this).linkPoster
            val inputLinkLive = binding.inputlinklive.text?.toString()
            val inputWaktu = binding.inputTime.text.toString()


            val inputCity = binding.inputCity.text.toString()
            val inputAlamat = binding.inputDetailAlamat.text.toString()

            if (inputJudul.isEmpty()) {
                Toast.makeText(this@tamkajJudul, "Judul Kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener }

            if (inputDeskripsi.isEmpty()) {
                Toast.makeText(this@tamkajJudul, "Deskripsi Kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener }

            if(InputKajianPreferences(this).linkPoster.equals("")) {
                Toast.makeText(this@tamkajJudul, "Poster Kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener }

            if(binding.inputTime.text.equals("Add Date and Time")) {
                Toast.makeText(this@tamkajJudul, "Masukkan Waktu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener }

            if(inputCity.isEmpty()) {
                Toast.makeText(this@tamkajJudul, "Masukkan Kota Daerah!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener }

            if(inputAlamat.isEmpty()) {
                Toast.makeText(this@tamkajJudul, "Detail Alamat Kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener }



            // Sends Data to Final Activity
            val intentData = Intent(this@tamkajJudul, tamkajWaktuLokasi::class.java)
            intentData.putExtra("judul", inputJudul)
            intentData.putExtra("deskripsi",inputDeskripsi)
            intentData.putExtra("livelink",inputLinkLive)
            intentData.putExtra("posterlink", inputPoster)
            intentData.putExtra("waktu", inputWaktu)
            intentData.putExtra("city", inputCity)
            intentData.putExtra("alamat",inputAlamat)
            Log.d("cekdata1", inputJudul)
            Log.d("cekdata1", inputDeskripsi)
            Log.d("cekdata1", inputLinkLive.toString())
            Log.d("cekdata1", inputPoster.toString())
            Log.d("cekdata1", inputWaktu)
            Log.d("cekdata1", inputCity)
            Log.d("cekdata1", inputAlamat)
            startActivity(intentData)
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
                    Toast.makeText(this@tamkajJudul, "Gagal upload file", Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                it.result.storage.downloadUrl.addOnSuccessListener {

                    binding.swipeContainer.isRefreshing = true
                    val imageUrl = it.toString()
                    InputKajianPreferences(this).linkPoster = imageUrl
                    binding.swipeContainer.isRefreshing = false
                    binding.imagePreview.load(imageUrl)
                }
            }

    }
    // Open Album
    fun openAlbum(){
        PictureSelector.create(this@tamkajJudul)
            .openSystemGallery(SelectMimeType.ofImage())
            .setSelectMaxFileSize(1)
            .forSystemResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    val media = result?.first() ?: return
//                    val file = File(media.realPath)
                    binding.imagePreview.load(media.path)
                    uploadFile(media.path)
                }
                override fun onCancel() {}
            })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RESULT****", "OK")
            if (requestCode == 1) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", latitude.toString())
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())
                val postalcode = data.getStringExtra(ZIPCODE)
                Log.d("POSTALCODE****", postalcode.toString())
                val bundle = data.getBundleExtra(TRANSITION_BUNDLE)
                Log.d("BUNDLE TEXT****", bundle?.getString("test").toString())
                val fullAddress = data.getParcelableExtra<Address>(ADDRESS)
                if (fullAddress != null) {
                    Log.d("FULL ADDRESS****", fullAddress.toString())
                }

            } else if (requestCode == 2) {
                val latitude = data.getDoubleExtra(LATITUDE, 0.0)
                Log.d("LATITUDE****", latitude.toString())
                val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
                Log.d("LONGITUDE****", longitude.toString())
                val address = data.getStringExtra(LOCATION_ADDRESS)
                Log.d("ADDRESS****", address.toString())
                val lekuPoi = data.getParcelableExtra<LekuPoi>(LEKU_POI)
                Log.d("LekuPoi****", lekuPoi.toString())
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED")
        }
    }
}
