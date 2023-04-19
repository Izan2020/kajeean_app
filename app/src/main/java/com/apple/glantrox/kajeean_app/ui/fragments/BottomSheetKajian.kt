package com.apple.glantrox.kajeean_app.ui.fragments

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ActivityBottomSheetKajianBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.coroutines.launch

class BottomSheetKajian : BottomSheetDialogFragment() {

    val binding: ActivityBottomSheetKajianBinding by viewBinding()
    var CallbackKajian: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
    View? = inflater.inflate(R.layout.activity_bottom_sheet_kajian, container, true)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // IDs
        val kajianID = GeneralPreferences(requireContext()).KajianID
        val userID = GeneralPreferences(requireContext()).UserID

        // APIs
        val api = SupabaseAPI.create()

        binding.tvJudul.loadSkeleton()
        binding.tvTanggal.loadSkeleton()
        binding.tvPenjelasan.loadSkeleton()
        binding.tvNamaustadz.loadSkeleton()
        binding.tvFollowers.loadSkeleton()
        binding.tvCountuserskajian.loadSkeleton()


        lifecycleScope.launch {
            val apiData = api.getKajianDetails("*","eq.$kajianID")
            val apiAuthor = api.getCurrentUser("*","eq.${apiData[0].authorId}")
            val apiCountFollowers = api.countFollowers("count","eq.${apiData[0].authorId}")
            val apiCount = api.countUsersKajian("count","eq.${kajianID}")

            binding.tvJudul.hideSkeleton()
            binding.tvTanggal.hideSkeleton()
            binding.tvPenjelasan.hideSkeleton()
            binding.tvNamaustadz.hideSkeleton()
            binding.tvFollowers.hideSkeleton()
            binding.tvCountuserskajian.hideSkeleton()

            // Time Parser
            val from = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val tanggal = java.text.SimpleDateFormat("MMMM dd, yyyy")
            val jam = java.text.SimpleDateFormat("HH:mm")

            val date = tanggal.format(from.parse(apiData[0].startTime))
            val hour = tanggal.format(from.parse(apiData[0].startTime))

            // Text Bindings
            binding.tvJudul.text = apiData[0].title
            binding.tvTanggal.text = date
            binding.tvPenjelasan.text = "Kajian akan di mulai pukul ${hour} sampai selesai " +
                    "Lokasi Kajian berada di daerah ${apiData[0].city}, ${apiData[0].addressDetail}"
            binding.tvNamaustadz.text = apiAuthor[0].fullName
            binding.tvFollowers.text = "${apiCountFollowers[0].count} Followers"
            binding.tvCountuserskajian.text = "${apiCount[0].count} User(s) Joined"

            binding.btnJoinKajian.setOnClickListener {
                GeneralPreferences(requireContext()).KajianID = 0
                Toast.makeText(requireContext(), "Anda Batal Mengikuti Kajian '${apiData[0].title}' ", Toast.LENGTH_SHORT).show()
                CallbackKajian?.invoke()
                dialog?.dismiss()
            }

            binding.btnfindlocation.setOnClickListener {
                val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=${apiData[0].city}, ${apiData[0].addressDetail}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }

        }




    }
}