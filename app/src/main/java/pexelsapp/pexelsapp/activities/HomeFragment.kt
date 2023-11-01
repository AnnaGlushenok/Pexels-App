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
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pexelsapp.pexelsapp.Error
import pexelsapp.pexelsapp.LastRequest
import pexelsapp.pexelsapp.R
import pexelsapp.pexelsapp.State
import pexelsapp.pexelsapp.adapters.PhotoAdapter
import pexelsapp.pexelsapp.data.Photos
import pexelsapp.pexelsapp.viewModels.PhotoViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewModel: PhotoViewModel
    private lateinit var photoAdapter1: PhotoAdapter
    private lateinit var photoAdapter2: PhotoAdapter
    private lateinit var clearButton: ImageButton
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var stubLayout: LinearLayout
    private lateinit var noResultLayout: LinearLayout
    private lateinit var noInternetLayout: LinearLayout
    private lateinit var exploreTextButton: TextView
    private lateinit var tryAgainTextButton: TextView
    private lateinit var lastRequestStr: String
    private var lastRequest: LastRequest = LastRequest.POPULAR
    private val buttons = mutableListOf<Button>()
    private var prevButton: Button? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        recyclerView1 = view.findViewById(R.id.photo_listView1)
        recyclerView2 = view.findViewById(R.id.photo_listView2)
        progressBar = view.findViewById(R.id.progressBar)
        nestedScrollView = view.findViewById(R.id.nestedScrollView)
        stubLayout = view.findViewById(R.id.stub_layout)
        noResultLayout = view.findViewById(R.id.no_result)
        noInternetLayout = view.findViewById(R.id.no_internet)
        exploreTextButton = view.findViewById(R.id.explore_text_button)
        tryAgainTextButton = view.findViewById(R.id.try_again_text_button)
        initButtons(view)
        setupRecycleView()
        viewModel.featuredCollections.observe(viewLifecycleOwner, Observer { resp ->
            when (resp) {
                is State.Success -> {
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

                is State.Error -> {
                    resp.message?.let { msg ->
                        val enum = try {
                            Error.valueOf(msg)
                        } catch (e: IllegalArgumentException) {
                            null
                        }
                        when (enum) {
                            Error.NO_INTERNET_CONNECTION -> showNoInternetStub(view)
                            Error.NO_DATA -> {
                                hideProgressBar()
                                showNoDataStub()
                            }

                            null -> Log.e("err", msg)
                        }
                    }
                }

                is State.Loading -> showProgressBar()
            }
        })
        viewModel.isLoadingProgressBar.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading)
                showProgressBar()
        })
        viewModel.photos.observe(viewLifecycleOwner, Observer { resp ->
            observeResponse(resp, view)
        })
        viewModel.searchPhotos.observe(viewLifecycleOwner, Observer { resp ->
            observeResponse(resp, view)
        })
    }

    private fun observeResponse(resp: State<Photos>, view: View) {
        when (resp) {
            is State.Success -> {
                if (resp.message != null)
                    Toast.makeText(
                        view.context,
                        "No internet connection, using cache",
                        Toast.LENGTH_LONG
                    ).show()
                resp.data?.let { photosResp ->
                    val p = photosResp.photos
                    photoAdapter1.differ.submitList(p.subList(0, p.size / 2))
                    photoAdapter2.differ.submitList(p.subList(p.size / 2, p.size))
                }
                showPage()
                hideProgressBar()
            }

            is State.Error -> {
                resp.message?.let { msg ->
                    val enum = try {
                        Error.valueOf(msg)
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                    when (enum) {
                        Error.NO_INTERNET_CONNECTION -> showNoInternetStub(view)
                        Error.NO_DATA -> {
                            hideProgressBar()
                            showNoDataStub()
                        }

                        null -> Log.e("err", msg)
                    }
                }
            }

            is State.Loading -> showProgressBar()
        }
    }

    private fun showNoInternetStub(view: View) {
        nestedScrollView.visibility = View.INVISIBLE
        stubLayout.visibility = View.VISIBLE
        noInternetLayout.visibility = View.VISIBLE
        noResultLayout.visibility = View.GONE
        Toast.makeText(
            view.context,
            "No internet connection",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showNoDataStub() {
        nestedScrollView.visibility = View.INVISIBLE
        stubLayout.visibility = View.VISIBLE
        noResultLayout.visibility = View.VISIBLE
        noInternetLayout.visibility = View.GONE
    }

    private fun showPage() {
        stubLayout.visibility = View.GONE
        nestedScrollView.visibility = View.VISIBLE
    }

    private fun setActive(button: Button, view: View) {
        button.setBackgroundResource(R.drawable.feature_button_active)
        button.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.light_background
            )
        )
    }

    private fun setInactive(button: Button?, view: View) {
        button?.setBackgroundResource(R.drawable.feature_button_inactive)
        button?.setTextColor(
            ContextCompat.getColor(
                view.context,
                R.color.light_text
            )
        )
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
            lastRequest = LastRequest.FEATURED_BUTTON
            setActive(button, view)
            lastRequestStr = button.text.toString()
            if (prevButton != null)
                setInactive(prevButton, view)

            searchEditText.setText(button.text.toString())
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
        progressBar.visibility = View.INVISIBLE
        nestedScrollView.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        nestedScrollView.visibility = View.INVISIBLE
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
                lastRequest = LastRequest.SEARCH
                if (s?.isEmpty() == true) {
                    clearButton.visibility = View.INVISIBLE
                    viewModel.getPhotos()
                    setInactive(prevButton, view)
                }
                job?.cancel()
                job = MainScope().launch {
                    delay(1000)
                    s?.let {
                        lastRequestStr = s.toString()
                        if (lastRequestStr.isNotEmpty())
                            viewModel.searchPhotos(lastRequestStr)
                        val button = buttons.find { b -> b.text == lastRequestStr }
                        if (button != null) {
                            setActive(button, view)
                            prevButton = button
                        } else {
                            setInactive(prevButton, view)
                        }
                    }
                }
            }
        })

        clearButton.setOnClickListener(View.OnClickListener {
            searchEditText.text.clear()
        })

        exploreTextButton.setOnClickListener {
            viewModel.getPhotos()
            searchEditText.text.clear()
        }

        tryAgainTextButton.setOnClickListener {
            when (lastRequest) {
                LastRequest.POPULAR -> viewModel.getPhotos()
                LastRequest.SEARCH -> viewModel.searchPhotos(lastRequestStr)
                LastRequest.FEATURED_BUTTON -> viewModel.searchPhotos(lastRequestStr)
            }
            viewModel.getFeaturedCollections()
        }
    }
}