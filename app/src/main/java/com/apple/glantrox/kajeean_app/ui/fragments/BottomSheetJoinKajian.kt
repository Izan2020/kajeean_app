package com.apple.glantrox.kajeean_app.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.FragmentBottomSheetJoinKajianBinding
import com.apple.glantrox.kajeean_app.models.JoinKajian
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.coroutines.launch

class BottomSheetJoinKajian : BottomSheetDialogFragment() {

    val binding: FragmentBottomSheetJoinKajianBinding by viewBinding()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
     View? = inflater.inflate(R.layout.fragment_bottom_sheet_join_kajian, container, true)
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         //APIs
         val api = SupabaseAPI.create()

         // ID that saved in PreferenceManager
         val userID = GeneralPreferences(requireContext()).UserID

         // Argument Values
         val judul = arguments?.getString(EXTRA_JUDUL)
         val tanggal = arguments?.getString(EXTRA_TANGGAL)
         val waktu = arguments?.getString(EXTRA_WAKTU)
         val alamat = arguments?.getString(EXTRA_ALAMAT)
         val idKajian = arguments?.getInt(EXTRA_IDKAJIAN)
         val idUstadz = arguments?.getInt(EXTRA_IDUSTADZ)
         val kota = arguments?.getInt(EXTRA_KOTA)
         val ustadz = arguments?.getString(EXTRA_NAMAAUTHOR)
         val followers = arguments?.getInt(EXTRA_FOLLOWERS)
         val urlFoto = arguments?.getString(EXTRA_URlFOTO)


         // Text Bindings
         binding.tvJudul.text = judul.toString()
         binding.tvTanggal.text = "Tanggal ${tanggal}"
         binding.tvPenjelasan.text = "Kajian akan di mulai pukul ${waktu} sampai selesai " +
                                     "Lokasi Kajian berada di daerah ${kota}, ${alamat}"
         binding.tvNamaustadz.text = "Ustadz ${ustadz}"
         binding.tvFollowers.text = "$followers Followers"
         binding.imageUstadz.load(urlFoto)

         lifecycleScope.launch {
             binding.tvCountuserskajian.loadSkeleton()
            val apiCount = api.countUsersKajian("count","eq.${idKajian}")
             binding.tvCountuserskajian.hideSkeleton()
             binding.tvCountuserskajian.text = "${apiCount[0].count} People Joined"
         }

         // Search in Google
         binding.btnfindlocation.setOnClickListener {
             val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=${kota}, ${alamat}")
             val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
             mapIntent.setPackage("com.google.android.apps.maps")
             startActivity(mapIntent)
         }

         // Insert User in Kajian
         binding.btnJoinKajian.setOnClickListener {
             lifecycleScope.launch {
                 val join = JoinKajian(
                     idKajian,
                     userID
                 )
                 Log.d("cekdatajoin",join.toString())
                 val apiInsert = api.joinKajian(join)
                 GeneralPreferences(requireContext()).KajianID = apiInsert[0].kajianId
                 Toast.makeText(requireContext(), "Anda Telah Terdaftar!", Toast.LENGTH_SHORT).show()

                 dialog?.dismiss()
             }
         }








    }

    companion object {
        const val EXTRA_JUDUL = "key_judul"
        const val EXTRA_TANGGAL = "key_tanggal"
        const val EXTRA_WAKTU = "key_waktu"
        const val EXTRA_ALAMAT = "key_alamat"
        const val EXTRA_IDKAJIAN = "key_idkajian"
        const val EXTRA_IDUSTADZ = "key_idustadz"
        const val EXTRA_KOTA = "key_kota"

        const val EXTRA_NAMAAUTHOR = "key_ustadz"
        const val EXTRA_FOLLOWERS = "key_followers"
        const val EXTRA_URlFOTO = "key_urlfoto"


    }

}