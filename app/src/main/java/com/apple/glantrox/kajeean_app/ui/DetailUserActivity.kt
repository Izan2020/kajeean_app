package com.apple.glantrox.kajeean_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {

    val binding: ActivityDetailUserBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)






    }
}