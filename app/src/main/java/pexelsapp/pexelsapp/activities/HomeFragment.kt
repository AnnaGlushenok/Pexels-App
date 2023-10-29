package pexelsapp.pexelsapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pexelsapp.pexelsapp.R
import pexelsapp.pexelsapp.Resource
import pexelsapp.pexelsapp.adapters.PhotoAdapter
import pexelsapp.pexelsapp.viewModels.PhotoViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewModel: PhotoViewModel
    private lateinit var photoAdapter1: PhotoAdapter
    private lateinit var photoAdapter2: PhotoAdapter
    private lateinit var clearButton: ImageButton
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private val buttons = mutableListOf<Button>()
    private var prevButton: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        recyclerView1 = view.findViewById(R.id.photo_listView1)
        recyclerView2 = view.findViewById(R.id.photo_listView2)
        initButtons(view)
        setupRecycleView()
        viewModel.featuredCollections.observe(viewLifecycleOwner, Observer { resp ->
            when (resp) {
                is Resource.Success -> {
                    resp.data?.let { collResp ->
                        val featured = view.findViewById<LinearLayout>(R.id.featured)
                        val collections = collResp.collections
                        for (i in 0..6)
                            createButton(
                                view,
                                featured,
                                collections[i].title
                            )
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    resp.message?.let { msg ->
                        Log.e("err", msg)
                        Toast.makeText(this.context, "Internet", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> showProgressBar()
            }
        })
        viewModel.photos.observe(viewLifecycleOwner, Observer { resp ->
            when (resp) {
                is Resource.Success -> {
                    hideProgressBar()
                    resp.data?.let { photosResp ->
                        val p = photosResp.photos
                        photoAdapter1.differ.submitList(p.subList(0, p.size / 2))
                        photoAdapter2.differ.submitList(p.subList(p.size / 2, p.size))
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    resp.message?.let { msg ->
                        Log.e("err", msg)
                        Toast.makeText(this.context, "Internet", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> showProgressBar()
            }
        })
        viewModel.searchPhotos.observe(viewLifecycleOwner, Observer { resp ->
            when (resp) {
                is Resource.Success -> {
                    hideProgressBar()
                    resp.data?.let { photosResp ->
                        val p = photosResp.photos
                        photoAdapter1.differ.submitList(p.subList(0, p.size / 2))
                        photoAdapter2.differ.submitList(p.subList(p.size / 2, p.size))
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    resp.message?.let { msg ->
                        Log.e("err", msg)
                    }
                }

                is Resource.Loading -> showProgressBar()
            }
        })
    }

    private fun createButton(view: View, container: LinearLayout, name: String) {
        val button = Button(view.context)
        button.id = View.generateViewId()
        button.text = name
        val layoutParams =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 20, 0)
        button.layoutParams = layoutParams
        button.setBackgroundResource(R.drawable.feature_button_inactive)
        button.isAllCaps = false
        button.setOnClickListener {
            button.setBackgroundResource(R.drawable.feature_button_active)
            button.setTextColor(
                ContextCompat.getColor(
                    view.context,
                    R.color.light_background
                )
            )
            if (prevButton != null) {
                prevButton?.setBackgroundResource(R.drawable.feature_button_inactive)
                prevButton?.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.light_text
                    )
                )
            }
            viewModel.searchPhotos(button.text.toString())
            prevButton = button
        }

        container.addView(button)
        buttons.add(button)
    }

    private fun setupRecycleView() {
        photoAdapter1 = PhotoAdapter()
        photoAdapter2 = PhotoAdapter()

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

    private fun hideProgressBar() {
        Log.e("", "hide")
//        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        Log.e("", "show")
//        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun initButtons(view: View) {
        var job: Job? = null
        clearButton = view.findViewById(R.id.clear_button)
        searchEditText = view.findViewById(R.id.search_text)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = View.VISIBLE
                if (s?.isEmpty() == true) {
                    clearButton.visibility = View.INVISIBLE
                    viewModel.getPhotos()
                }
                job?.cancel()
                job = MainScope().launch {
                    delay(1000)
                    s?.let {
                        val str = s.toString()
                        if (str.isNotEmpty())
                            viewModel.searchPhotos(str)
                    }
                }
            }
        })

        clearButton.setOnClickListener(View.OnClickListener {
            searchEditText.text.clear()
        })
    }
}