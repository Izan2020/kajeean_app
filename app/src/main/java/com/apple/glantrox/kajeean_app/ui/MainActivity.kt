package com.apple.glantrox.kajeean_app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.apple.glantrox.kajeean_app.KajianListViewModel
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.adapters.KajianListAdapter
import com.apple.glantrox.kajeean_app.adapters.UstadzListAdapter
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ActivityMainBinding
import com.apple.glantrox.kajeean_app.models.PostKajianResponseItem
import com.apple.glantrox.kajeean_app.models.UstadzDataResponseItem
import com.apple.glantrox.kajeean_app.ui.fragments.BottomSheetKajian
import com.apple.glantrox.kajeean_app.ui.ui_tambahkajian.tamkajJudul
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    val binding: ActivityMainBinding  by viewBinding()
    val viewModel: KajianListViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_main)

        BottomSheetKajian().CallbackKajian = {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            finish()
        }


        // Status Bar Color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        // ID that saved in PreferenceManager
        val userID = GeneralPreferences(this@MainActivity).UserID
        val kajianID = GeneralPreferences(this@MainActivity).KajianID
        Log.d("userID", "$userID")
        Log.d("userID", "$kajianID")

        //APIs
        val api = SupabaseAPI.create()



        // Additional Bindings
        val refreshLayout = binding.swipeContainer
        refreshLayout.setOnRefreshListener {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            finish()

        }

        //  Settings Top Binding
        val gotoSettings = binding.cardGotosettings
        val imgTopRight = binding.imgTopGotosettings
        try {
            lifecycleScope.launch {
                binding.swipeContainer.isRefreshing = true
                val apiShowData = api.getCurrentUser("*", "eq.${userID}")
                imgTopRight.load(apiShowData[0].profilePicture)


                if (apiShowData[0].role == "ustadz") {
                    binding.btnGotoAddKajian.isVisible = true
                }

                binding.swipeContainer.isRefreshing = false
            }
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
        }
        gotoSettings.setOnClickListener {
            // Goto Settings Activity
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }

        // Hides Kajian Card when you're not attended to Kajian






        // Goto Favourites
        binding.gotoFavourites.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavouritesActivity::class.java))
        }



        // Get Ustadzlist API
        try {
            lifecycleScope.launch {
                binding.swipeContainer.isRefreshing = true
                binding.rvListasatidz.loadSkeleton(R.layout.item_avmain_listustadz) {
                    itemCount(7)
                }

                try {
                    val apiRvUstadz = api.getAllAsatidz()
                    binding.rvListasatidz.hideSkeleton()
                    binding.swipeContainer.isRefreshing = false
                    setUstadzAdapter(apiRvUstadz, lifecycleScope)
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e:Exception) {
            Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
        }

        // Get Kajianlist API
        binding.rvKajian.loadSkeleton(R.layout.item_avmain_listkajian) {
            itemCount(12)
        }
        try {
            viewModel.listKajian.observe(this) {
                setKajianAdapter(it.reversed(),lifecycleScope)
            }
        }catch (e:Exception) {
            Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
        }
        viewModel.loadingKajian.observe(this@MainActivity) { isLoading->
            if (isLoading){
                binding.rvKajian.loadSkeleton(R.layout.item_avmain_listkajian) {
                    itemCount(12)
                }
            } else {
                binding.rvKajian.hideSkeleton()
            }
        }


        // Goto Add Kajian
        binding.btnGotoAddKajian.setOnClickListener {
            startActivity(Intent(this@MainActivity, tamkajJudul::class.java))
        }

        binding.cardTopLayout.isGone = true

        if (kajianID >= 0) {
            binding.cardTopLayout.isGone = false
            binding.cardTopLayout.isVisible = true

            binding.tvUstadznameTopcard.loadSkeleton()
            binding.tvJudulkajianawok.loadSkeleton()
            binding.tvHour.loadSkeleton()
            binding.tvDate.loadSkeleton()
            binding.cardTopLayout.isVisible = true
            try {
                lifecycleScope.launch {
                    val apiCurrentKajian = api.getKajianDetails("*","eq.$kajianID")
                    Log.d("authorDatas", apiCurrentKajian.toString())
                    val authorID = apiCurrentKajian[0].authorId
                    val apiAuthorKajian = api.getCurrentUser("*","eq.${authorID}")

                    binding.tvUstadznameTopcard.hideSkeleton()
                    binding.tvJudulkajianawok.hideSkeleton()
                    binding.tvHour.hideSkeleton()
                    binding.tvDate.hideSkeleton()


                    binding.tvUstadznameTopcard.text = "Ustadz ${apiAuthorKajian[0].fullName}"
                    binding.tvJudulkajianawok.text = apiCurrentKajian[0].title

                    val from = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val toDate = SimpleDateFormat("dd MMMM, yyyy")
                    val toHour = SimpleDateFormat("HH:mm")
                    val resultHour = toHour.format(from.parse(apiCurrentKajian[0].startTime))
                    val resultDate = toDate.format(from.parse(apiCurrentKajian[0].startTime))

                    binding.tvDate.text = resultDate
                    binding.tvHour.text = resultHour

                    binding.cardTop.setOnClickListener {
                        val bottomSheet = BottomSheetKajian().apply {}
                            .show(supportFragmentManager, "bottomsheethome")
                    }

                }
            } catch (e:Exception) {
                Toast.makeText(this@MainActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }


    }


    // Kajian List Adapter Function
    fun setKajianAdapter(
        kajianList: List<PostKajianResponseItem>,
        lifecycleCoroutineScope: LifecycleCoroutineScope
    ) {
        val recyclerView = binding.rvKajian
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        val adapter = KajianListAdapter(kajianList, lifecycleCoroutineScope)
        recyclerView.adapter = adapter

        // Starts Kajian Detail Activity
        adapter.itemClickListener = {
            val intent = Intent(this@MainActivity, DetailKajianActivity::class.java)
            intent.putExtra("id",it.id)
            startActivity(intent)
        }


    }

    // Ustadz List Adapter Function
    fun setUstadzAdapter(
        ustadList: List<UstadzDataResponseItem>,
        lifecycleCoroutineScope: LifecycleCoroutineScope
    ) {
        val recyclerView = binding.rvListasatidz
        val adapter = UstadzListAdapter(ustadList, lifecycleCoroutineScope)
        recyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        adapter.itemClickListener = {
            val intent = Intent(this@MainActivity, DetailUstadzActivity::class.java)
                intent.putExtra("idustat",it.id)
            startActivity(intent)
        }

    }
}