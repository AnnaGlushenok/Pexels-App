package pexelsapp.pexelsapp.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import pexelsapp.pexelsapp.PhotoViewModel
import pexelsapp.pexelsapp.R

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private lateinit var viewModel: PhotoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val image: ImageView = view.findViewById(R.id.imageView2)
        Glide.with(this)
//        https://www.pexels.com/photo/a-close-up-of-some-red-berries-on-a-tree-18855407/
            .load("https://images.pexels.com/photos/18857014/pexels-photo-18857014.jpeg")
            .into(image)
    }
}