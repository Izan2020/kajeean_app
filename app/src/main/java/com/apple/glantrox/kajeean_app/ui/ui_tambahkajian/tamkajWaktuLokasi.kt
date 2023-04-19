package com.apple.glantrox.kajeean_app.ui.ui_tambahkajian

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.models.PostKajian
import com.apple.glantrox.kajeean_app.ui.MainActivity
import com.apple.glantrox.kajeean_app.ui.fragments.BottomSheetKajian
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class tamkajWaktuLokasi : AppCompatActivity() {

    val binding: com.apple.glantrox.kajeean_app.databinding.ActivityTamkajWaktuLokasiBinding by viewBinding()

    private var funHour: String = ""
    private var funDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tamkaj_waktu_lokasi)

        //APIs
        val api = SupabaseAPI.create()



        // ID that saved in PreferenceManager
        val userID = GeneralPreferences(this@tamkajWaktuLokasi).UserID
        val kajianID = GeneralPreferences(this@tamkajWaktuLokasi).KajianID

        // Intent Datas From Previous Activity
        val judulIntent = intent.getStringExtra("judul")
        val deskripsiIntent = intent.getStringExtra("deskripsi")
        val livelinkIntent = intent.getStringExtra("livelink")
        val posterlinkIntent = intent.getStringExtra("posterlink")
        val waktuIntent = intent.getStringExtra("waktu")
        val cityIntent = intent.getStringExtra("city")
        val alamatIntent = intent.getStringExtra("alamat")
        Log.d("cekdata", judulIntent.toString())
        Log.d("cekdata", deskripsiIntent.toString())
        Log.d("cekdata", livelinkIntent.toString())
        Log.d("cekdata", posterlinkIntent.toString())
        Log.d("cekdata", waktuIntent.toString())
        Log.d("cekdata", cityIntent.toString())
        Log.d("cekdata", alamatIntent.toString())




        // Formatter
        sdfDateMonthYear(waktuIntent.toString())
        val formattedDate = funDate
        sdfHourMinute(waktuIntent.toString())
        val formattedHour = funHour

        // Appearance
        binding.prvImage.load(posterlinkIntent)
        binding.prvJudul.text = judulIntent
        binding.prvCity.text = cityIntent
        binding.prvDate.text = formattedDate
        binding.prvHour.text = formattedHour
        binding.prvAlamat.text = alamatIntent
        binding.prvDeskripsi.text = deskripsiIntent

        // Youtube Player Preview ID

        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = livelinkIntent.toString().removePrefix("https://www.youtube.com/watch?v=")
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
        if(livelinkIntent.isNullOrEmpty()) {
            binding.dividerYt.isGone = true
            binding.relativeYt.isGone = true
            binding.cardYt.isGone = true
        }

        // Send Data to Supabase API
        binding.buttonPost.setOnClickListener {
            try {

                lifecycleScope.launch {
                val kajian = PostKajian(
                    alamatIntent.toString(),
                    userID.toString(),
                    cityIntent.toString(),
                    deskripsiIntent.toString(),
                    posterlinkIntent.toString(),
                    waktuIntent.toString(),
                    judulIntent.toString(),
                    livelinkIntent.toString()
                )
                    Log.d("datapost",kajian.toString())
                    binding.swipeContainer.isRefreshing = true
                val apiPost = api.postKajian(kajian)
                    binding.swipeContainer.isRefreshing = false



                    startActivity(Intent(this@tamkajWaktuLokasi, MainActivity::class.java))
                    finish()

                }

            }catch (e:Exception){
                Toast.makeText(this@tamkajWaktuLokasi, e.message.toString() , Toast.LENGTH_SHORT).show()
            }

        }




    }
    private fun sdfDateMonthYear(dmf: String) {
        val from = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val to = SimpleDateFormat("MMMM dd, yyyy")
        val result = to.format(from.parse(dmf))
        funDate = result.toString()


    }
    private fun sdfHourMinute(hm: String) {
        val from = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val to = SimpleDateFormat("HH:mm")
        val result = to.format(from.parse(hm))
        funHour = result.toString()


    }
    fun getDomainName(url: String?): String? {
        val uri = URI(url)
        val domain: String? = uri.host
        return domain?.removePrefix("https://www.youtube.com/watch?v=")
    }

    fun ytVidParser(url: String): String? {
        var vId: String? = null
        val pattern: Pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher: Matcher = pattern.matcher(url)
        if (matcher.matches()) {
            vId = matcher.group(1)
        }
        Log.d("parsedurl", vId.toString())
        return vId
    }


}