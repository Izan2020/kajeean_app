package com.apple.glantrox.kajeean_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import by.kirich1409.viewbindingdelegate.viewBindingLazy
import by.kirich1409.viewbindingdelegate.viewBindingWithLifecycle
import coil.load
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ItemAvmainListustadzBinding
import com.apple.glantrox.kajeean_app.models.UstadzDataResponse
import com.apple.glantrox.kajeean_app.models.UstadzDataResponseItem
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.util.*

class UstadzListAdapter(val listUstadz: List<UstadzDataResponseItem>, val lifecycleCoroutineScope: LifecycleCoroutineScope): RecyclerView.Adapter<UstadzListAdapter.UstadzListViewHolder>() {


    var countUstadz : ((UstadzDataResponseItem) -> Unit)? = null
    var itemClickListener: ((UstadzDataResponseItem) -> Unit)? = null

    class UstadzListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val binding: ItemAvmainListustadzBinding by viewBinding()

        val ustadzName = binding.tvNameustadz
        val ustadzIcon = binding.iconUstadz
        val totalKajian = binding.totalKajian
        val cardUstadz = binding.cardUstadz

        fun bindView(ustadz: UstadzDataResponseItem) {
            ustadzName.text = "Ustadz ${ustadz.fullName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }}"
            ustadzIcon.load(ustadz.profilePicture)

        }





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UstadzListViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_avmain_listustadz, parent, false)
        return UstadzListAdapter.UstadzListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UstadzListViewHolder, position: Int) {
        val ustadz = listUstadz[position]
        holder.bindView(ustadz)

        lifecycleCoroutineScope.launch {
            holder.totalKajian.loadSkeleton()
            val api = SupabaseAPI.create().countAsatidzKajian(
                "count",
                "eq.${ustadz.id}"
            )
            holder.totalKajian.hideSkeleton()
            holder.totalKajian.text = "${api[0].count} Kajian"
        }
        holder.cardUstadz.setOnClickListener {
            itemClickListener?.invoke(ustadz)
        }


    }

    override fun getItemCount(): Int {
        return listUstadz.size
    }
}