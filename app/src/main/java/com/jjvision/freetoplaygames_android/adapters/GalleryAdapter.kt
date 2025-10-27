package com.jjvision.freetoplaygames_android.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jjvision.freetoplaygames_android.R
import com.jjvision.freetoplaygames_android.data.Screenshots
import com.jjvision.freetoplaygames_android.databinding.ItemGalleryBinding
import com.squareup.picasso.Picasso

class GalleryAdapter(
    var items: List<Screenshots>,
    var selectedPosition: Int,
    val onClickListener: (Int) -> Unit
) : RecyclerView.Adapter<GalleryViewHolder>() {
    // ¿Cuál es la vista para los elementos?
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGalleryBinding.inflate(layoutInflater, parent, false)
        return GalleryViewHolder(binding)
    }

    // ¿Cuáles son los datos para el elemento?
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = items[position]
        holder.render(item)
        holder.setSelected(position == selectedPosition)
        holder.itemView.setOnClickListener {
            onClickListener(position)
        }
    }

    // ¿Cuántos elementos se quieren listar?
    override fun getItemCount(): Int {
        return items.size
    }

    fun changeSelectedPosition(position: Int) {
        val oldSelected = selectedPosition
        selectedPosition = position
        notifyItemChanged(oldSelected)
        notifyItemChanged(selectedPosition)
    }

}

class GalleryViewHolder(val binding: ItemGalleryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(screenshot: Screenshots) {
        Picasso.get().load(screenshot.image).placeholder(R.drawable.bg_image_placeholder)
            .into(binding.screenshotImageView)
    }

    fun setSelected(selected: Boolean) {
        if (selected) {
            itemView.setBackgroundResource(R.color.md_theme_primary)
        } else {
            itemView.setBackgroundResource(0)
        }
    }
}