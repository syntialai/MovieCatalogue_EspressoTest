package com.syntia.moviecatalogue.features.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.syntia.moviecatalogue.core.model.detail.Cast
import com.syntia.moviecatalogue.core.utils.ImageUtils
import com.syntia.moviecatalogue.databinding.LayoutCastItemBinding
import com.syntia.moviecatalogue.features.base.adapter.BaseDiffCallback

class DetailCastAdapter : ListAdapter<Cast, DetailCastAdapter.DetailCastViewHolder>(diffCallback) {

  companion object {
    private val diffCallback = object : BaseDiffCallback<Cast>() {
      override fun contentEquality(oldItem: Cast, newItem: Cast): Boolean {
        return oldItem.id == newItem.id
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailCastViewHolder {
    return DetailCastViewHolder(
        LayoutCastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }

  override fun onBindViewHolder(holder: DetailCastViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class DetailCastViewHolder(private val binding: LayoutCastItemBinding) :
      RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Cast) {
      binding.apply {
        textViewCastItemName.text = data.name
        textViewCastItemCharacter.text = data.character

        ImageUtils.loadImage(root.context, data.profilePath, imageViewCastItem,
            ImageUtils.PLACEHOLDER_TYPE_CAST)
      }
    }
  }
}