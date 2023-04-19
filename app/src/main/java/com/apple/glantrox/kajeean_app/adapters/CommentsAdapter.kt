package com.apple.glantrox.kajeean_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.apple.glantrox.kajeean_app.R
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.databinding.ItemAvkajianCommentsBinding
import com.apple.glantrox.kajeean_app.models.CommentsResponseItem
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat



class CommentsAdapter(val commentList : List<CommentsResponseItem>, val lifecycleCoroutineScope: LifecycleCoroutineScope): RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    var gotoUserClickListener: ((CommentsResponseItem) -> Unit)? = null

    class CommentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val binding: ItemAvkajianCommentsBinding by viewBinding()


        fun bindView(commentList: CommentsResponseItem) {
            binding.commentstvIsikomentar.text = commentList.comment

            val from = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val to = SimpleDateFormat("dd MMMM, yyyy")
            val parsedDate = to.format(from.parse(commentList.createdAt))
            binding.commentstvTanggalcomment.text = parsedDate

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_avkajian_comments, parent, false)
        return CommentsAdapter.CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comments = commentList[position]
        holder.bindView(comments)
        lifecycleCoroutineScope.launch {
            val apiGetUsers = SupabaseAPI.create().getCurrentUser("*","eq.${comments.userId}")

            holder.binding.commentstvNamauser.text = apiGetUsers[0].fullName
            holder.binding.imgUser.load(apiGetUsers[0].profilePicture)

        }
        holder.binding.commentscardPfpusers.setOnClickListener {
            gotoUserClickListener?.invoke(comments)
        }


    }

    override fun getItemCount(): Int {
        return commentList.size
    }


}