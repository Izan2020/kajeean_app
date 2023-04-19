package com.apple.glantrox.kajeean_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.databinding.ItemAvdetailustadzListkajianBinding
import com.apple.glantrox.kajeean_app.models.PostKajianResponseItem
import java.text.SimpleDateFormat

class KajianListUstadzAdapter(val listKajianUstadz: List<PostKajianResponseItem>): RecyclerView.Adapter<KajianListUstadzAdapter.ViewHolder>() {

    var itemClickListener: ((PostKajianResponseItem) -> Unit)? = null

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {




        val binding: ItemAvdetailustadzListkajianBinding by viewBinding()

        fun bindView(ustadz: PostKajianResponseItem) {

            val from = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val to = SimpleDateFormat("dd MMMM, yyyy")

            val date = to.format(from.parse(ustadz.createdAt))

            binding.tvWaktu.text = date
            binding.tvJudul.text = ustadz.title
            binding.tvDeskripsi.text = ustadz.description
            binding.imgPreview.load(ustadz.posterUrl)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_avdetailustadz_listkajian, parent, false)
        return KajianListUstadzAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ustadz = listKajianUstadz[position]
        holder.bindView(ustadz)
        holder.binding.cardItem.setOnClickListener {
            itemClickListener?.invoke(ustadz)
        }
    }

    override fun getItemCount(): Int {
        return listKajianUstadz.size
    }
}