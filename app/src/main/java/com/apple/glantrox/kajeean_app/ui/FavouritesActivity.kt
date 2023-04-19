package com.apple.glantrox.kajeean_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.adapters.FavouritesAdapter
import com.apple.glantrox.kajeean_app.databinding.ActivityFavouritesBinding
import com.apple.glantrox.kajeean_app.localdatabase.FavouriteDatabase
import com.apple.glantrox.kajeean_app.localdatabase.Favourites
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavouritesActivity : AppCompatActivity() {

    val binding: ActivityFavouritesBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        // Status Bar Color
        window.statusBarColor = ContextCompat.getColor(this, R.color.gray2)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        val localdb = FavouriteDatabase.getInstance(this@FavouritesActivity)
        val recyclerView = binding.rvFavourites

        binding.avSettingsImgBtnBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        lifecycleScope.launch {
            val favourites: Flow<List<Favourites>> = localdb.FavouriteDAO().getAllFavourites()
            favourites.collect{
                val adapter = FavouritesAdapter(it)
                recyclerView.adapter = adapter

                if(it.isEmpty()) {
                    binding.textEmpty.isVisible = true
                } else {
                    binding.textEmpty.isGone = true
                }

                adapter.itemClickListener = {
                    val intent = Intent(this@FavouritesActivity, DetailKajianActivity::class.java)
                    intent.putExtra("id", it.kajianId)
                    startActivity(intent)
                }
                adapter.deleteClickListener = {
                    MaterialAlertDialogBuilder(this@FavouritesActivity)
                        .setTitle("Remove from Favourites?")
                        .setMessage("Are you sure want to remove this from Favourite")
                        .setNegativeButton("No") {_,_ -> }
                        .setPositiveButton("Yes") {_,_ ->
                            lifecycleScope.launch {
                                val delete = localdb.FavouriteDAO().deleteFavourites(it)
                                Toast.makeText(this@FavouritesActivity, "Deleted", Toast.LENGTH_SHORT).show()
                                adapter.notifyDataSetChanged()
                            }
                        }
                        .show()
                        .create()
                }

            }

        }



    }
}