package com.yb.part4_chapter07

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yb.part4_chapter07.data.models.PhotoResponse
import com.yb.part4_chapter07.databinding.ItemPhotoBinding

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    var photos: List<PhotoResponse> = emptyList()
    var onClickPhoto: (PhotoResponse) -> Unit = {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int = photos.size


    inner class ViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClickPhoto(photos[adapterPosition])
            }
        }

        fun bind(photo: PhotoResponse) = with(binding) {
            val dimensionRatio = photo.height / photo.width.toFloat()
            val targetWidth = root.resources.displayMetrics.widthPixels -
                    (root.paddingStart + root.paddingEnd)
            val targetHeight = (targetWidth * dimensionRatio).toInt()

            contentsContainer.layoutParams.height = targetHeight

            Glide.with(root)
                .load(photo.urls?.regular)
                .thumbnail(
                    Glide.with(root)
                        .load(photo.urls?.thumb)
                        .transition(DrawableTransitionOptions.withCrossFade())
                )
                .override(targetWidth, targetHeight)
                .into(photoImageView)

            Glide.with(root)
                .load(photo.user?.profileImageUrls?.small)
                .placeholder(R.drawable.shape_profile_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(profileImageView)

            if (photo.user?.name.isNullOrBlank()) {
                authorTextView.visibility = View.GONE
            } else {
                authorTextView.visibility = View.VISIBLE
                authorTextView.text = photo.user?.name
            }

            if (photo.description.isNullOrBlank()) {
                descriptionTextView.visibility = View.GONE
            } else {
                descriptionTextView.visibility = View.VISIBLE
                descriptionTextView.text = photo.description
            }
        }
    }

}