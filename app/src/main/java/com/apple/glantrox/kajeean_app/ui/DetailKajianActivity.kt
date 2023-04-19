package com.apple.glantrox.kajeean_app.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.apple.glantrox.kajeean_app.CommentsViewModel
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.SharedPrerences.GeneralPreferences
import com.apple.glantrox.kajeean_app.adapters.CommentsAdapter
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ActivityDetailKajianBinding
import com.apple.glantrox.kajeean_app.databinding.FragmentBottomSheetJoinKajianBinding
import com.apple.glantrox.kajeean_app.localdatabase.FavouriteDatabase
import com.apple.glantrox.kajeean_app.localdatabase.Favourites
import com.apple.glantrox.kajeean_app.models.Comment
import com.apple.glantrox.kajeean_app.models.CommentsResponseItem
import com.apple.glantrox.kajeean_app.ui.fragments.BottomSheetJoinKajian
import com.google.android.material.internal.ContextUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

import java.net.URI

import java.text.SimpleDateFormat

class DetailKajianActivity : AppCompatActivity() {

    val binding: ActivityDetailKajianBinding by viewBinding()
    val viewModel: CommentsViewModel by viewModels()

    override fun onStart() {
        super.onStart()

        val id = intent.getIntExtra("id",0)
        Log.d("iddatalmao",id.toString())
        viewModel.getListComments("eq.$id")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_kajian)

        binding.cardIkutikajian.isVisible = false



        ContextUtils.getActivity(this@DetailKajianActivity)?.let {
            ContextUtils.getActivity(this)?.window?.
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        binding.avAttendImgBtnBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }

        binding.swipeContainer.setOnRefreshListener {
            recreate()
        }

        val id = intent.getIntExtra("id",0)

        // APIs
        val api = SupabaseAPI.create()

        // ID that saved in PreferenceManager
        val userID = GeneralPreferences(this@DetailKajianActivity).UserID
        val kajianID = GeneralPreferences(this@DetailKajianActivity).KajianID




        binding.tvTanggalKajian.loadSkeleton()
        binding.tvJamkajian.loadSkeleton()
        binding.tvNamaKajian.loadSkeleton()
        binding.tvDeskripsiKajian.loadSkeleton()
        binding.tvAlamatKajian.loadSkeleton()
        binding.topliveDivider.loadSkeleton()
        binding.tvtopLivevideo.loadSkeleton()
        binding.cardLive.loadSkeleton()
        binding.tvCountfollowers.loadSkeleton()
        binding.tvNamaustadz.loadSkeleton()
        binding.tvTotalusers.loadSkeleton()


        try {
            lifecycleScope.launch {
                binding.swipeContainer.isRefreshing = true
                val apis = api.getKajianDetails("*","eq.$id")
                Log.d("apidatas", apis.toString())


                if(apis[0].authorId == userID) {
                    binding.btnGotoEditKajian.isGone = false
                    binding.btnGotoEditKajian.isVisible = true
                    binding.btnGotoEditKajian.setOnClickListener {
                        val intent = Intent(this@DetailKajianActivity, UpdateKajianActivity::class.java)
                        intent.putExtra("id", id)
                        startActivity(intent)
                    }
                }






                val startTime = apis[0].startTime
                val city = apis[0].city
                val addressDetail = apis[0].addressDetail
                val authorId = apis[0].authorId.toString()
                val createdAt = apis[0].createdAt
                val description = apis[0].description
                val posterUrl = apis[0].posterUrl
                val title = apis[0].title
                val linkYt = apis[0].livestreamingLink
                Log.d("cekdatalagi","Desc :$description\n Lokasi: $addressDetail")

                binding.swipeContainer.isRefreshing = false
                binding.tvTanggalKajian.hideSkeleton()
                binding.tvJamkajian.hideSkeleton()
                binding.tvNamaKajian.hideSkeleton()
                binding.tvDeskripsiKajian.hideSkeleton()
                binding.tvAlamatKajian.hideSkeleton()
                binding.topliveDivider.hideSkeleton()
                binding.tvtopLivevideo.hideSkeleton()
                binding.cardLive.hideSkeleton()
                binding.tvCountfollowers.hideSkeleton()
                binding.tvNamaustadz.hideSkeleton()
                binding.tvTotalusers.hideSkeleton()


                // Goto Ustadz
                binding.cardGotoustadz.setOnClickListener {
                    val intent = Intent(this@DetailKajianActivity, DetailUstadzActivity::class.java)
                    intent.putExtra("idustat", apis[0].authorId)
                    startActivity(intent)
                }


                // Tanggal  Jam Formatting
                val from = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val tanggal = SimpleDateFormat("MMMM dd, yyyy")
                val jam = SimpleDateFormat("HH:mm")

                val dateFormatted = tanggal.format(from.parse(apis[0].startTime))
                val hourFormatted = jam.format(from.parse(apis[0].startTime))

                // Tanggal Jam Binding
                binding.tvTanggalKajian.text = dateFormatted
                binding.tvJamkajian.text = "$hourFormatted - Done"

                // Information Binding
                binding.tvNamaKajian.text = apis[0].title
                binding.tvDeskripsiKajian.text = apis[0].description
                binding.tvAlamatKajian.text = apis[0].addressDetail
                binding.imgCover.load(apis[0].posterUrl)

                // Current User Comment Image
                lifecycleScope.launch {
                    val apiUser = api.getCurrentUser("*","eq.$userID")
                    binding.imgUserComment.load(apiUser[0].profilePicture)
                }

                // Count users in Kajian
                val apiCount = SupabaseAPI.create().countUsersKajian("count","eq.${id}")
                if(apiCount[0].count < 1) {
                    binding.tvTotalusers.text = "No one.. "
                } else {
                    binding.tvTotalusers.text = "${apiCount[0].count} Joined"
                }


                // Youtube Video Binding
                if(apis[0].livestreamingLink.isNullOrEmpty()) {
                    binding.layoutVideoyt.isGone = true
                } else {
                    binding.layoutVideoyt.isVisible = true
                    val urlParsedtoId = apis[0].livestreamingLink?.removePrefix("https://www.youtube.com/watch?v=")
                    binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            val videoId = urlParsedtoId.toString()
                            youTubePlayer.loadVideo(videoId, 0f)
                        }
                    })
                }



                // Author Binding
                binding.tvCountfollowers.loadSkeleton()
                binding.tvNamaustadz.loadSkeleton()
                val apiAuthor = api.getCurrentUser("*","eq.$authorId")
                val apiCountFollowers = api.countFollowers("count","eq.$authorId")
                Log.d("getustadzinfo","$apiCountFollowers $apiAuthor")
                binding.tvCountfollowers.text = "${apiCountFollowers[0].count} Followers"
                binding.tvNamaustadz.text = "Ustadz ${apiAuthor[0].fullName}"
                binding.imgUstadz.load(apiAuthor[0].profilePicture)
                binding.tvCountfollowers.hideSkeleton()
                binding.tvNamaustadz.hideSkeleton()



                // BottomSheetKajian
                val guik = SupabaseAPI.create().getUsersKajian("*","eq.${id}")
                binding.cardIkutikajian.isVisible = true
                Log.d("guik", guik.toString())
                val isTerdafter = guik.filter {
                    it.userId == userID
                }

                if(isTerdafter.size == 1 ) {
                    binding.tvIkutikajian.text = "Anda Telah Terdaftar"
                    binding.tvIkutikajian.setTextColor(Color.parseColor("#FA5B3F"))
                    binding.cardIkutikajian.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
                } else {
                    binding.tvIkutikajian.text = "Ikuti Kajian"
                }

                binding.cardIkutikajian.setOnClickListener {

                    if(isTerdafter.size == 1) {
                        Toast.makeText(this@DetailKajianActivity, "Anda Sudah Terdaftar", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    val bottomSheet = BottomSheetJoinKajian().apply {
                        arguments = bundleOf(
                            BottomSheetJoinKajian.EXTRA_ALAMAT to apis[0].addressDetail,
                            BottomSheetJoinKajian.EXTRA_IDKAJIAN to id,
                            BottomSheetJoinKajian.EXTRA_IDUSTADZ to apis[0].authorId,
                            BottomSheetJoinKajian.EXTRA_JUDUL to apis[0].title,
                            BottomSheetJoinKajian.EXTRA_TANGGAL to dateFormatted,
                            BottomSheetJoinKajian.EXTRA_WAKTU to hourFormatted,
                            BottomSheetJoinKajian.EXTRA_KOTA to city,
                            BottomSheetJoinKajian.EXTRA_NAMAAUTHOR to apiAuthor[0].fullName,
                            BottomSheetJoinKajian.EXTRA_FOLLOWERS to apiCountFollowers[0].count,
                            BottomSheetJoinKajian.EXTRA_URlFOTO to apiAuthor[0].profilePicture

                        )

                    }.show(supportFragmentManager,"joinkajian")
                }


            }
        } catch (e: Exception) {
            Toast.makeText(this@DetailKajianActivity, "${e.message}", Toast.LENGTH_SHORT).show()
        }


        // Add to Favourites Gitulah
        lifecycleScope.launch {
            val localDatabase = FavouriteDatabase.getInstance(this@DetailKajianActivity)

            val apis = api.getKajianDetails("*","eq.$id")
            val check = localDatabase.FavouriteDAO().checkFavourites()
            val isInserted = check.filter {
                it.kajianId == apis[0].id
            }

            // FA5B3F orange

            if(isInserted.size == 1) {
                binding.cardFavourites.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"))
                binding.iconFavourites.setColorFilter(
                    ContextCompat
                    .getColor(this@DetailKajianActivity, R.color.mainColor),
                    android.graphics.PorterDuff.Mode.MULTIPLY)
            } else if(isInserted.isEmpty()) {
                binding.cardFavourites.setCardBackgroundColor(Color.parseColor("#FA5B3F"))
                binding.iconFavourites.setColorFilter(
                    ContextCompat
                        .getColor(this@DetailKajianActivity, R.color.white),
                    android.graphics.PorterDuff.Mode.MULTIPLY)
            }

            binding.cardFavourites.setOnClickListener {

                if(isInserted.size == 1) {
                    lifecycleScope.launch {
                        val apis = api.getKajianDetails("*","eq.$id")
                        val favourites = Favourites(
                            id,
                            apis[0].id,
                            binding.tvNamaKajian.text.toString(),
                            binding.tvDeskripsiKajian.text.toString(),
                            binding.tvNamaustadz.text.toString()
                        )
                        val localDatabase = FavouriteDatabase.getInstance(this@DetailKajianActivity)
                        localDatabase.FavouriteDAO().deleteFavourites(favourites)
                        recreate()
                    }
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    val apis = api.getKajianDetails("*","eq.$id")
                    val favourites = Favourites(
                        null,
                        apis[0].id,
                        binding.tvNamaKajian.text.toString(),
                        binding.tvDeskripsiKajian.text.toString(),
                        binding.tvNamaustadz.text.toString()
                    )
                    localDatabase.FavouriteDAO().insertFavourites(favourites)
                    Toast.makeText(this@DetailKajianActivity, "Added to Favourite!", Toast.LENGTH_SHORT).show()
                    recreate()
                }

            }

        }



        // Comments API

        binding.commentstvIsikomentar.setOnEditorActionListener { textView, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE) {
                val query = "${textView.text}"
                try {
                    lifecycleScope.launch(Dispatchers.Main) {
                        val comment = Comment(
                            query,
                            id,
                            userID
                        )
                        Log.d("comment",comment.toString())
                        binding.swipeContainer.isRefreshing = true
                        val apiComment = api.postComment(comment)
                        Log.d("comment",apiComment.toString())
                        binding.swipeContainer.isRefreshing = false
                        textView.text = ""
                        recreate()
                    }
                } catch (e:Exception) {
                    Toast.makeText(this@DetailKajianActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }


            true
        }

        // Comments List API
        viewModel.listComments.observe(this) {
            setCommentsAdapter(it,lifecycleScope)
            Log.d("datacomments", it.toString())
        }




        
        
        
    }
    fun setCommentsAdapter(commentsList: List<CommentsResponseItem>, lifecycleCoroutineScope: LifecycleCoroutineScope) {
        val recyclerView = binding.rvComments
        val adapter = CommentsAdapter(commentsList.reversed(), lifecycleCoroutineScope)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)




        adapter.gotoUserClickListener = {
            try {
                lifecycleScope.launch {
                    val apiShowData = SupabaseAPI.create().getCurrentUser("*", "eq.${it.userId}")
                    if (apiShowData[0].role == "ustadz") {
                        val intent = Intent(this@DetailKajianActivity, DetailUstadzActivity::class.java)
                        intent.putExtra("idustat",it.userId)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@DetailKajianActivity, DetailUserActivity::class.java)
                        intent.putExtra("user_id",it.userId)
                        startActivity(intent)
                    }
                }
            } catch (e:Exception) {
                Toast.makeText(this@DetailKajianActivity, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }



    }
    fun getDomainName(url: String?): String? {
        val uri = URI(url)
        val domain: String? = uri.host
        return domain?.removePrefix("https://www.youtube.com/watch?v=")
    }
}