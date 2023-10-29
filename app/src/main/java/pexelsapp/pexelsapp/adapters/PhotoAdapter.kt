package pexelsapp.pexelsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import pexelsapp.pexelsapp.R
import pexelsapp.pexelsapp.data.Photo

@GlideModule
class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.image_element,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(photo.src.original)
                .into(findViewById(R.id.imageView))
            setOnClickListener {
                onItemClickListener?.let { it(photo) }
            }
        }
    }

    private var onItemClickListener: ((Photo) -> Unit)? = null
    fun setOnItemClickListener(listener: (Photo) -> Unit) {
        onItemClickListener = listener
    }
}
