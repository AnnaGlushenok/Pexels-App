package pexelsapp.pexelsapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pexelsapp.pexelsapp.R
import pexelsapp.pexelsapp.adapters.BookmarkAdapter
import pexelsapp.pexelsapp.viewModels.PhotoViewModel

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private lateinit var viewModel: PhotoViewModel
    private lateinit var photoAdapter1: BookmarkAdapter
    private lateinit var photoAdapter2: BookmarkAdapter
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var stubLayout: LinearLayout
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var exploreTextButton: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        recyclerView1 = view.findViewById(R.id.photo_listView1)
        recyclerView2 = view.findViewById(R.id.photo_listView2)
        progressBar = view.findViewById(R.id.progressBar)
        nestedScrollView = view.findViewById(R.id.nestedScrollView)
        stubLayout = view.findViewById(R.id.stub_layout)
        exploreTextButton = view.findViewById(R.id.explore_text_button)

        setupRecycleView()
        viewModel.getSavedPhoto().observe(viewLifecycleOwner, Observer { photos ->
            if (photos.isEmpty())
                showNoDataStub()
            else {
                photoAdapter1.differ.submitList(photos.subList(0, photos.size / 2))
                photoAdapter2.differ.submitList(photos.subList(photos.size / 2, photos.size))
                showPage()
                hideProgressBar()
            }
        })

        exploreTextButton.setOnClickListener {
            val fragment = HomeFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun showNoDataStub() {
        progressBar.visibility = View.INVISIBLE
        nestedScrollView.visibility = View.INVISIBLE
        stubLayout.visibility = View.VISIBLE
    }

    private fun showPage() {
        stubLayout.visibility = View.GONE
        nestedScrollView.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        nestedScrollView.visibility = View.VISIBLE
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