package com.apple.glantrox.kajeean_app.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.adapters.KajianListUstadzAdapter
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ActivityDetailUstadzBinding
import com.apple.glantrox.kajeean_app.models.InsertFollower
import com.apple.glantrox.kajeean_app.models.PostKajianResponseItem
import com.google.android.material.internal.ContextUtils
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.coroutines.launch
import java.util.*

class DetailUstadzActivity : AppCompatActivity() {

    val binding: ActivityDetailUstadzBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_ustadz)

        ContextUtils.getActivity(this@DetailUstadzActivity)?.let {
            ContextUtils.getActivity(this)?.window?.
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        // ID that saved in PreferenceManager
        val userID = GeneralPreferences(this@DetailUstadzActivity).UserID
        val kajianID = GeneralPreferences(this@DetailUstadzActivity).KajianID

        // ID From Intent
        val idUstadz = intent.getIntExtra("idustat", 0)

        // Back Button
        binding.avAttendImgBtnBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }


        binding.tvTotalkajian.loadSkeleton()
        binding.tvNamaKajian.loadSkeleton()
        binding.tvFollowers.loadSkeleton()
        binding.tvCity.loadSkeleton()
        binding.tvAboutme.loadSkeleton()
        binding.swipeContainer.isRefreshing = true


        // Information Bindings
        lifecycleScope.launch {
            val apiUstadz = SupabaseAPI.create().getCurrentUser("*","eq.${idUstadz}")
            val apiCountFollowers = SupabaseAPI.create().countFollowers("count","eq.${idUstadz}")
            val apiCountKajian = SupabaseAPI.create().getAllKajianUstadz("*","eq.${idUstadz}")
            binding.swipeContainer.isRefreshing = false


            if(apiUstadz[0].aboutMe.isNullOrEmpty()) {
                binding.tvAboutme.text = "This user hasnt describe about them yet."
            }

            binding.imgCover.load(apiUstadz[0].profilePicture)
            binding.tvAboutme.text = apiUstadz[0].aboutMe
            binding.tvTotalkajian.text = "${apiCountKajian.size} Kajian"
            binding.tvNamaKajian.text = "Ustadz \n${apiUstadz[0].fullName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }}"
            binding.tvFollowers.text = "${apiCountFollowers[0].count} Followers"
            binding.tvCity.text = apiUstadz[0].city


            binding.tvTotalkajian.hideSkeleton()
            binding.tvNamaKajian.hideSkeleton()
            binding.tvFollowers.hideSkeleton()
            binding.tvCity.hideSkeleton()
            binding.tvAboutme.hideSkeleton()

        }

        // Follow Ustadz Binding
        lifecycleScope.launch {
            val check = SupabaseAPI.create().getAllFollowers("eq.${idUstadz}")
            val isFollowed = check.filter {
                it.followerId == userID
            }
            if (idUstadz == userID) {
                binding.cardFollow.isGone = true
            }


//            binding.cardFollow.setCardBackgroundColor(Color.parseColor("#FA5B3F"))
//            binding.imageFollow.setColorFilter(ContextCompat
//                .getColor(this@DetailUstadzActivity, R.color.white),
//                android.graphics.PorterDuff.Mode.MULTIPLY)

            if (isFollowed.size == 1) {
                binding.cardFollow.isClickable
                binding.cardFollow.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
                binding.imageFollow.setColorFilter(ContextCompat
                    .getColor(this@DetailUstadzActivity, R.color.mainColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY)
            }

            binding.cardFollow.setOnClickListener {
                if (isFollowed.size == 1) {
                   lifecycleScope.launch {
                       val apiunFollow = SupabaseAPI.create().unfollowUser("eq.${idUstadz}","eq.${userID}")
                        recreate()
                   }

                    return@setOnClickListener
                }
                lifecycleScope.launch {
                    val follower = InsertFollower(
                        userID,
                        idUstadz
                    )
                    val apiFollow = SupabaseAPI.create().insertFollower(follower)
                    recreate()
                }
            }

        }






        lifecycleScope.launch {
            val api = SupabaseAPI.create().getAllKajianUstadz("*","eq.${idUstadz}")
            setAdapter(api)

        }


    }
    // RecyclerView Kajians
    fun setAdapter(kajianListUsadz: List<PostKajianResponseItem>) {
        val recyclerView = binding.rvListkajian
        val adapter = KajianListUstadzAdapter(kajianListUsadz)
        recyclerView.adapter = adapter

        adapter.itemClickListener = {
            val intent = Intent(this@DetailUstadzActivity, DetailKajianActivity::class.java)
            intent.putExtra("id",it.id)
            startActivity(intent)
        }


    }
}