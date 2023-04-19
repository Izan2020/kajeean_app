package com.apple.glantrox.kajeean_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.adevinta.leku.LocationPickerActivity
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ItemAvmainListkajianBinding
import com.apple.glantrox.kajeean_app.models.PostKajianResponse
import com.apple.glantrox.kajeean_app.models.PostKajianResponseItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class KajianListAdapter(val listKajian: List<PostKajianResponseItem>, val lifecycleCoroutineScope: LifecycleCoroutineScope): RecyclerView.Adapter<KajianListAdapter.KajianListViewHolder>() {

    var itemClickListener : ((PostKajianResponseItem) -> Unit)? = null

    class KajianListViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {


        val binding: ItemAvmainListkajianBinding by viewBinding()

        val cardItem = binding.cardItem

        fun bindView(listKajian: PostKajianResponseItem) {
            binding.tvJudul.text = (listKajian.title)
            binding.tvDeskripsi.text = (listKajian.description)
            binding.tvCity.text = (listKajian.city)

            val from = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val into = SimpleDateFormat("HH:mm")
            val hour = into.format(from.parse(listKajian.startTime))
            binding.tvTimestart.text = hour

            binding.imgPoster.load(listKajian.posterUrl)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KajianListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_avmain_listkajian, parent, false)
        return KajianListAdapter.KajianListViewHolder(view)
    }

    override fun onBindViewHolder(holder: KajianListViewHolder, position: Int) {
        val kajian = listKajian[position]
        holder.bindView(kajian)

        lifecycleCoroutineScope.launch {
            val apiGetUstadz = SupabaseAPI.create().getCurrentUser("*","eq.${kajian.authorId}")
            holder.binding.imgUstadz.load(apiGetUstadz[0].profilePicture)
            holder.binding.tvNameustadz.text = "Ustadz ${apiGetUstadz[0].fullName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }}"
        }
        
        holder.cardItem.setOnClickListener {
            itemClickListener?.invoke(kajian)
        }




    }

    override fun getItemCount(): Int {
        return listKajian.size
    }
}