package pexelsapp.pexelsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule

@GlideModule
class BookmarkAdapter : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {
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
                R.layout.card_element,
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
            findViewById<TextView>(R.id.author).text = photo.photographer
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
