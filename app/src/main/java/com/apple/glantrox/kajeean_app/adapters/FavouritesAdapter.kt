package com.apple.glantrox.kajeean_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.databinding.ItemFavouritesBinding
import com.apple.glantrox.kajeean_app.localdatabase.Favourites

class FavouritesAdapter(val listFavourites: List<Favourites>): RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    var itemClickListener: ((Favourites) -> Unit)? = null
    var deleteClickListener: ((Favourites) -> Unit)? = null

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val binding: ItemFavouritesBinding by viewBinding()

        fun bindView(listFavourites: Favourites) {

            binding.tvJudul.text = listFavourites.title
            binding.tvDeskripsi.text = "${listFavourites.description} " +
                    "\nID Kajian ${listFavourites.kajianId}" +
                    "ID ${listFavourites.id}"
            binding.tvNameustadz.text =listFavourites.ustadzName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourites, parent, false)
        return FavouritesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favourite = listFavourites[position]
        holder.bindView(favourite)
        holder.binding.cardItem.setOnClickListener {
            itemClickListener?.invoke(favourite)
        }
        holder.binding.btnDelete.setOnClickListener {
            deleteClickListener?.invoke(favourite)
        }

    }

    override fun getItemCount(): Int {
        return listFavourites.size
    }
}