package pexelsapp.pexelsapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pexelsapp.pexelsapp.BookmarkAdapter
import pexelsapp.pexelsapp.PhotoViewModel
import pexelsapp.pexelsapp.R

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private lateinit var viewModel: PhotoViewModel
    private lateinit var photoAdapter1: BookmarkAdapter
    private lateinit var photoAdapter2: BookmarkAdapter
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        recyclerView1 = view.findViewById(R.id.photo_listView1)
        recyclerView2 = view.findViewById(R.id.photo_listView2)
        setupRecycleView()
        viewModel.getSavedPhoto().observe(viewLifecycleOwner, Observer { photos ->
            photoAdapter1.differ.submitList(photos.subList(0, photos.size / 2))
            photoAdapter2.differ.submitList(photos.subList(photos.size / 2, photos.size))
        })
    }

    private fun setupRecycleView() {
        photoAdapter1 = BookmarkAdapter()
        photoAdapter2 = BookmarkAdapter()

        photoAdapter1.setOnItemClickListener {
            startActivity(Intent(this.context, PictureActivity::class.java).apply {
                putExtra("photo", it)
            })
        }
        photoAdapter2.setOnItemClickListener {
            startActivity(Intent(this.context, PictureActivity::class.java).apply {
                putExtra("photo", it)
            })
        }
        recyclerView1.apply {
            adapter = photoAdapter1
            layoutManager = LinearLayoutManager(activity)
        }
        recyclerView2.apply {
            adapter = photoAdapter2
            layoutManager = LinearLayoutManager(activity)
        }
    }
}